package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.*;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 玩家账号 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IPlayerService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    AuthPlayerService authService;

    @Resource
    IBallSystemConfigService systemConfigService;

    @Override
    @Cacheable(value = "ball_player_by_id", key = "#id", unless = "#result == null")
    public BallPlayer findById(Long id) {
        return getById(id);
    }

    @Cacheable(value = "ball_player_by_username", key = "#username", unless = "#result == null")
    @Override
    public BallPlayer findByUsername(String username) {
        QueryWrapper query = new QueryWrapper();
        query.eq("username", username);
        BallPlayer ballPlayer = getOne(query);
        return ballPlayer;
    }

    @Override
    @Cacheable(value = "ball_player_by_invitation_code", key = "#invitationCode", unless = "#result == null")
    public BallPlayer findByInvitationCode(String invitationCode) {
        QueryWrapper query = new QueryWrapper();
        query.eq("invitation_code", invitationCode);
        BallPlayer ballPlayer = getOne(query);
        return ballPlayer;
    }

    @Override
    public BallPlayer getCurrentUser(String token) {
        Long userId;
        try {
            List<String> audience = JWT.decode(token).getAudience();
            userId = Long.parseLong(audience.get(0));
            BallPlayer byId = getById(userId);
            return byId;
        } catch (JWTDecodeException j) {
            throw new RuntimeException("内部错误");
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean editPwd(Long id, String password) {
        UpdateWrapper<BallPlayer> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        BallPlayer dao = new BallPlayer();
        dao.setPassword(password);
        return update(dao, wrapper);
    }

    @Override
    public BaseResponse registPlayer(PlayerRegistRequest registRequest, String ipAddress) {
        Long parentPlayerId = 0L;
        //先判断验证码是否正确
        BaseResponse baseResponse = checkVerifyCode(registRequest.getVerifyKey(), registRequest.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        //检查数据库里面是否有用户名
        BallPlayer ballPlayer = findByUsername(registRequest.getUsername());
        if (ballPlayer != null) {
            return BaseResponse.failedWithMsg("账号已存在~");
        }
        //判断两次密码是否相等
        if (!registRequest.getPassword().equals(registRequest.getTwoPassword())) {
            return BaseResponse.failedWithMsg("两次密码不对");
        }
        //是否有输入邀请码,判定当前系统是否开启了邀请码
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        if (systemConfig.getRegisterIfNeedVerificationCode() == 1) {
            //开启了邀请码,必须输入邀请码
            if (StringUtils.isEmpty(registRequest.getInvitationCode())) {
                //没有邀请码
                List<Map<String, Object>> errorList = new ArrayList<>();
                Map<String, Object> data = new HashMap<>();
                data.put("name", "invitationCode");
                data.put("msg", "必须输入邀请码");
                errorList.add(data);
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
            } else {
                //邀请码是否正确
                BallPlayer parentPlayer = findByInvitationCode(registRequest.getInvitationCode());
                if (parentPlayer == null) {
                    List<Map<String, Object>> errorList = new ArrayList<>();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", "invitationCode");
                    data.put("msg", "邀请码不正确");
                    errorList.add(data);
                    return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
                } else {
                    //邀请码正确,注册账号上级为邀请码关联账号
                    parentPlayerId = parentPlayer.getId();
                }
            }
        } else {
            //TODO 当前没有指定要邀请码,查询系统配置的邀请码
        }
        BallPlayer save = BallPlayer.builder()
                .username(registRequest.getUsername())
                .password(PasswordUtil.genPasswordMd5(registRequest.getPassword()))
                .invitationCode("")
                .token("")
                .theNewIp(ipAddress)
                .theLastIp(ipAddress)
                .superiorId(parentPlayerId)
                .build();
        MapUtil.setCreateTime(save);

        boolean res = save(save);
        if (res) {
            redisUtil.del(RedisKeyContant.VERIFY_KEY+registRequest.getVerifyKey());
        }
        return res ? BaseResponse.successWithMsg("注册成功~") : BaseResponse.failedWithMsg("注册失败~");
    }


    @Override
    public BaseResponse login(PlayerAuthLoginRequest req) {
        //判定账号密码输入错误次数
        String redisKey = RedisKeyContant.PLAYER_LOGIN_FAIL_COUNT + req.getUsername();
        Object failCountRedis = redisUtil.get(redisKey);
        int failCount = 0;
        if(failCountRedis!=null){
            failCount = Integer.parseInt(failCountRedis.toString());
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            if(failCount==systemConfig.getPasswordMaxErrorTimes()){
                Map<String, Object> data = MapUtil.newMap("time", redisUtil.getExpire(redisKey));
                data.put("msg","密码错误次数已达上限~");
                data.put("failCount",failCount);
                return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_MAX,data);
            }
        }
        BaseResponse baseResponse = checkVerifyCode(req.getVerifyKey(), req.getCode());
        if (baseResponse.getCode() != StatusCodes.OK) {
            return baseResponse;
        }
        BallPlayer ballPlayer = null;
        try {
            ballPlayer = findByUsername(req.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ballPlayer == null || !ballPlayer.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            //缓存当前输入错误次数
            BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
            redisUtil.incr(redisKey,1);
            redisUtil.expire(redisKey,systemConfig.getPasswordErrorLockTime());
            failCount++;
            Map<String, Object> respData = MapUtil.newMap("failCount", failCount);
            if(failCount == systemConfig.getPasswordMaxErrorTimes()){
                //指定秒后解锁
                redisUtil.expire(redisKey,systemConfig.getPasswordErrorLockTime());
                respData.put("time",systemConfig.getPasswordErrorLockTime());
                respData.put("msg","密码错误次数已达上限~");
                return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_MAX,respData);
            }
            respData.put("msg","密码错误次数"+failCount+"次");
            return BaseResponse.failedWithData(BaseResponse.FAIL_LOGIN_ERROR_COUNT,respData);
        }


        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballPlayer.getId());
        userBean.setUserName(ballPlayer.getUsername());

        Object hget = redisUtil.hget(PlayerAuthServiceImpl.REDIS_PLAYER_AUTH_KEY, ballPlayer.getId().toString());
        if (hget == null || StringUtils.isEmpty(hget.toString())) {
            userBean.setToken(authService.buildToken(ballPlayer));
        } else {
            userBean.setToken(hget.toString());
        }
        return new BaseResponse<>(userBean);
    }

    @Override
    public BaseResponse getVerifyCode() throws IOException {
        //使用验证码工具类生成4位验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //将验证码放入redis
        String key = UUIDUtil.getUUID();
        redisUtil.set(RedisKeyContant.VERIFY_KEY+key, code, VerifyCodeUtils.TIME_OUT);
        //将验证码放入图片
        String imageBase64 = VerifyCodeUtils.getImageBase64(220, 60, code);
        Map<String, Object> data = new HashMap<>();
        data.put("verifyKey", key);
        data.put("time", VerifyCodeUtils.TIME_OUT);
        data.put("img", imageBase64);
        return BaseResponse.successWithData(data);
    }

    @Override
    public BaseResponse checkVerifyCode(String verifyKey, String code) {
        Object hget = redisUtil.get(RedisKeyContant.VERIFY_KEY+verifyKey);
        if (hget == null) {
            return BaseResponse.failedWithMsg("验证码已过期~");
        }
        if (hget.equals(code)) {
            return BaseResponse.successWithMsg("");
        }
        return BaseResponse.failedWithMsg("验证码不正确~");
    }
}
