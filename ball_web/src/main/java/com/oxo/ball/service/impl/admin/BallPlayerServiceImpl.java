package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.service.impl.BasePlayerService;
import com.oxo.ball.utils.BigDecimalUtil;
import com.oxo.ball.utils.MapUtil;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.TimeUtil;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class BallPlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IBallPlayerService {

    @Resource
    BasePlayerService playerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;

    @Override
    public SearchResponse<BallPlayer> search(BallPlayer paramQuery, Integer pageNo, Integer pageSize) {
        SearchResponse<BallPlayer> response = new SearchResponse<>();

        Page<BallPlayer> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallPlayer> query = new QueryWrapper<>();

        query.select("id","username","balance","the_last_ip","superior_name",
                "invitation_code","directly_subordinate_num","group_size","account_type","vip_level","status","created_at");

        if(!StringUtils.isEmpty(paramQuery.getUsername())){
            query.eq("username",paramQuery.getUsername());
        }
        if(!StringUtils.isEmpty(paramQuery.getInvitationCode())){
            query.eq("invitation_code",paramQuery.getInvitationCode());
        }

        IPage<BallPlayer> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());

        return response;
    }

    @Override
    public BaseResponse insert(BallPlayer registRequest) {
        Long parentPlayerId = 0L;
        String superTree = "";
        String parentPlayerName = "";
        BallPlayer parentPlayer=null;
        //检查数据库里面是否有用户名
        List<Map<String, Object>> errorList = new ArrayList<>();
        BallPlayer ballPlayer = playerService.findByUsername(registRequest.getUsername());
        if (ballPlayer != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", "username");
            data.put("msgKey", "name_exsit");
            errorList.add(data);
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
        }
        //是否有输入上级
        if(!"0".equals(registRequest.getSuperiorName()) && !StringUtils.isEmpty(registRequest.getSuperiorName())) {
            //邀请码是否正确
            parentPlayer = playerService.findByUsername(registRequest.getSuperiorName());
            if (parentPlayer == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", "superiorName");
                data.put("msgKey", "superiorName");
                errorList.add(data);
                return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,errorList);
            } else {
                parentPlayerName = parentPlayer.getUsername();
                //邀请码正确,注册账号上级为邀请码关联账号
                parentPlayerId = parentPlayer.getId();
                //tree
                superTree = (StringUtils.isEmpty(parentPlayer.getSuperTree())?"":parentPlayer.getSuperTree())+parentPlayer.getId()+"_";
            }
        }
        String invitationCode = "";
        while (true){
            invitationCode = String.valueOf(TimeUtil.getRandomNum(1234567, 9876543));
            BallPlayer byInvitationCode = playerService.findByInvitationCode(invitationCode);
            if(byInvitationCode==null){
                break;
            }
        }
        BallPlayer save = BallPlayer.builder()
                .version(1L)
                .username(registRequest.getUsername())
                .password(PasswordUtil.genPasswordMd5(registRequest.getPassword()))
                .invitationCode(invitationCode)
                .token("")
                .superiorId(parentPlayerId)
                .superTree(superTree)
                .accountType(2)
                .status(1)
                .theNewIp("--")
                //TODO 玩家注册送888888
                .balance(888888*BigDecimalUtil.PLAYER_MONEY_UNIT)
                .superiorName(parentPlayerName)
                .build();
        MapUtil.setCreateTime(save);

        boolean res = save(save);
        if (res) {
            //如果保存成功，并且有上级，重新计算上级的团队和下属计数
            if(save.getSuperiorId()!=0){
                //上级直属下级+1,团队人数+1
                while (true){
                    BallPlayer parentPlayerEdit = BallPlayer.builder()
                            .version(parentPlayer.getVersion())
                            .directlySubordinateNum(parentPlayer.getDirectlySubordinateNum()+1)
                            .groupSize(parentPlayer.getGroupSize()+1)
                            .build();
                    parentPlayerEdit.setId(parentPlayerId);
                    boolean isSucc = playerService.editAndClearCache(parentPlayerEdit, parentPlayer);
                    if(isSucc){
                        break;
                    }else{
                        parentPlayer = playerService.findById(parentPlayerId);
                    }
                }
                //上级的上上上。。级团队人数+1
                String treePath = parentPlayer.getSuperTree();
                if(!StringUtils.isEmpty(treePath)&&!treePath.equals("_")){
                    String ids = StringUtils.join(treePath.split("_"), ",").substring(1);
                    playerService.editMultGroupNum(ids,1);
                }
            }
        }
        return res ? BaseResponse.successWithMsg("注册成功~") : BaseResponse.failedWithMsg("注册失败~");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        //修改了上级的话,原上级需要减团队人数和下级人数
        //新上级需要加团队人数和下级人数
        //先判定是否设置了上级,或者自己设置为顶级
        boolean hasOldParent = false;
        //如果是空就不管
        if(!StringUtils.isEmpty(ballPlayer.getSuperiorName())){
            if("0".equals(ballPlayer.getSuperiorName())){
                ballPlayer.setSuperiorId(0L);
                ballPlayer.setSuperiorName("_");
                if(byId.getSuperiorId()!=0){
                    //如果原来有上级
                    hasOldParent = true;
                }
            }else{
                if(!ballPlayer.getSuperiorId().equals(byId.getSuperiorId())
                        && byId.getSuperiorId()!=0
                        ){
                    hasOldParent = true;
                }
            }
            //有旧上级
            if(hasOldParent){
                //原上级的-1
                BallPlayer parentPlayer = playerService.findById(byId.getSuperiorId());
                while (true){
                    BallPlayer parentPlayerEdit = BallPlayer.builder()
                            .version(parentPlayer.getVersion())
                            .directlySubordinateNum(parentPlayer.getDirectlySubordinateNum()-1)
                            .groupSize(parentPlayer.getGroupSize()-1)
                            .build();
                    parentPlayerEdit.setId(parentPlayer.getId());
                    boolean isSucc = playerService.editAndClearCache(parentPlayerEdit, parentPlayer);
                    if(isSucc){
                        break;
                    }else{
                        parentPlayer = playerService.findById(byId.getSuperiorId());
                    }
                }
                //上级的上上上。。级团队人数-1
                String treePath = parentPlayer.getSuperTree();
                if(!StringUtils.isEmpty(treePath)&&!treePath.equals("_")){
                    String ids = StringUtils.join(treePath.split("_"), ",").substring(1);
                    playerService.editMultGroupNum(ids,-1);
                }
            }
            //有新上级
            if(ballPlayer.getSuperiorId()>0){
                //新上级+1
                BallPlayer newParentPlayer = playerService.findById(ballPlayer.getSuperiorId());
                while (true){
                    BallPlayer parentPlayerEdit = BallPlayer.builder()
                            .version(newParentPlayer.getVersion())
                            .directlySubordinateNum(newParentPlayer.getDirectlySubordinateNum()+1)
                            .groupSize(newParentPlayer.getGroupSize()+1)
                            .build();
                    parentPlayerEdit.setId(newParentPlayer.getId());
                    boolean isSucc = playerService.editAndClearCache(parentPlayerEdit, newParentPlayer);
                    if(isSucc){
                        break;
                    }else{
                        newParentPlayer = playerService.findById(byId.getSuperiorId());
                    }
                }
                //上级的上上上。。级团队人数+1
                String treePath = newParentPlayer.getSuperTree();
                if(!StringUtils.isEmpty(treePath)&&!treePath.equals("_")){
                    String ids = StringUtils.join(treePath.split("_"), ",").substring(1);
                    playerService.editMultGroupNum(ids,1);
                }
                ballPlayer.setSuperTree((StringUtils.isEmpty(newParentPlayer.getSuperTree())?"":newParentPlayer.getSuperTree())+newParentPlayer.getId()+"_");
            }
        }
        BallPlayer edit = BallPlayer.builder()
                .phone(ballPlayer.getPhone())
                .eMail(ballPlayer.getEMail())
                .accountType(ballPlayer.getAccountType())
                .remark(ballPlayer.getRemark())
                .superiorName(ballPlayer.getSuperiorName())
                .superiorId(ballPlayer.getSuperiorId())
                .build();
        edit.setId(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(ballPlayer, byId);
        return b;
    }

    @Override
    public Boolean editPwd(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .password(PasswordUtil.genPasswordMd5(ballPlayer.getEditPwd()))
                .build();
        edit.setId(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(edit, byId);
        return b;
    }

    @Override
    public Boolean editPayPwd(BallPlayer ballPlayer) {
//        BallPlayer byId = playerService.findById(ballPlayer.getId());
//        BallPlayer edit = BallPlayer.builder()
//                .pa(PasswordUtil.genPasswordMd5(ballPlayer.getPassword()))
//                .build();
//        edit.setId(ballPlayer.getId());
//        boolean b = playerService.editAndClearCache(edit, byId);
        return false;
    }

    @Override
    public Boolean editStatus(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .status(ballPlayer.getStatus()==1?2:1)
                .build();
        edit.setId(ballPlayer.getId());
        boolean b = playerService.editAndClearCache(edit, byId);
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editAddBalance(BallPlayer ballPlayer) {
        BallPlayer byId = playerService.findById(ballPlayer.getId());
        BallPlayer edit = BallPlayer.builder()
                .balance(byId.getBalance()+ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT)
                .build();
        edit.setId(ballPlayer.getId());
        while(true){
            boolean b = playerService.editAndClearCache(edit, byId);
            if(b){
                //插入账变
                ballBalanceChangeService.insert(BallBalanceChange.builder()
                        .playerId(byId.getId())
                        .initMoney(byId.getBalance())
                        .changeMoney(ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT)
                        .dnedMoney(edit.getBalance())
                        .remark("管理员上分")
                        .createdAt(System.currentTimeMillis())
                        .balanceChangeType(6)
                        .build());
                return BaseResponse.successWithData(playerService.findById(ballPlayer.getId()));
            }else{
                byId = playerService.findById(ballPlayer.getId());
                edit.setVersion(byId.getVersion());
                edit.setBalance(byId.getBalance()+ballPlayer.getBalance()*BigDecimalUtil.PLAYER_MONEY_UNIT);
            }
        }
    }

    @Override
    public Boolean editCaptchaPass(BallPlayer ballPlayer) {
//        BallPlayer byId = playerService.findById(ballPlayer.getId());
//        BallPlayer edit = BallPlayer.builder()
//                .status(ballPlayer.getStatus()==1?2:1)
//                .build();
//        edit.setId(ballPlayer.getId());
//        boolean b = playerService.editAndClearCache(edit, byId);
        return false;
    }

    @Override
    public BaseResponse info(BallPlayer ballPlayer) {
        return BaseResponse.successWithData(playerService.findById(ballPlayer.getId()));
    }
}
