package com.oxo.ball.service.impl;

import com.oxo.ball.bean.dao.BallLoggerBet;
import com.oxo.ball.bean.dao.BallLoggerLogin;
import com.oxo.ball.bean.dao.BallLoggerOper;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.queue.MessageQueueBet;
import com.oxo.ball.bean.dto.queue.MessageQueueDTO;
import com.oxo.ball.bean.dto.queue.MessageQueueLogin;
import com.oxo.ball.bean.dto.queue.MessageQueueOper;
import com.oxo.ball.service.IMessageQueueService;
import com.oxo.ball.service.admin.IBallLoggerBetService;
import com.oxo.ball.service.admin.IBallLoggerOperService;
import com.oxo.ball.service.admin.IBallLoggerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class MessageQueueServiceImpl implements IMessageQueueService {

    @Resource
    IBallLoggerService loggerService;
    BlockingQueue<MessageQueueDTO> messageQueue;
    @Resource
    private BasePlayerService basePlayerService;
    @Resource
    IBallLoggerOperService loggerOperService;
    @Resource
    IBallLoggerBetService loggerBetService;
    @Override
    public void putMessage(MessageQueueDTO message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startQueue() {
        messageQueue = new LinkedBlockingQueue<>();
        while (true){
            try {
                MessageQueueDTO message = messageQueue.take();
                switch (message.getType()){
                    case MessageQueueDTO.TYPE_LOG_LOGIN:
                        loginLog(message);
                        break;
                    case MessageQueueDTO.TYPE_LOG_OPER:
                        logOper(message);
                        break;
                    case MessageQueueDTO.TYPE_LOG_BET:
                        logBet(message);
                        break;
                    default:
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void logBet(MessageQueueDTO message) {
        MessageQueueBet data = (MessageQueueBet) message.getData();
        String superName = "";
        BallPlayer ballPlayer = data.getBallPlayer();
        if(!StringUtils.isEmpty(ballPlayer.getSuperTree())){
            //有上线
            superName = getSuperPlayerName(superName, ballPlayer);
        }
        loggerBetService.insert(BallLoggerBet.builder()
                .createdAt(System.currentTimeMillis())
                .betIp(data.getIp())
                .betContent(data.getBetContent())
                .betOrderNo(data.getOrderId())
                .playerName(data.getBallPlayer().getUsername())
                .superPlayerName(superName)
                .build());
    }

    private String getSuperPlayerName(String superName, BallPlayer ballPlayer) {
        if(!StringUtils.isEmpty(ballPlayer.getSuperTree()) || !("_".equals(ballPlayer.getSuperTree()))){
            //有上线
            String superTree =ballPlayer.getSuperTree().startsWith("_")?ballPlayer.getSuperTree().substring(1):ballPlayer.getSuperTree();
            String[] superId = superTree.split("_");
            try {
                BallPlayer superPlayer = basePlayerService.findById(Long.parseLong(superId[0]));
                superName = superPlayer.getUsername();
            }catch (Exception ex){
            }
        }
        return superName;
    }

    private void logOper(MessageQueueDTO message) {
        MessageQueueOper data = (MessageQueueOper) message.getData();
        loggerOperService.insert(BallLoggerOper.builder()
                .createdAt(System.currentTimeMillis())
                .ip(data.getIp())
                .mainFunc(data.getMainOper())
                .subFunc(data.getSubOper())
                .remark(data.getRemark())
                .username(data.getUsername())
                .build());
    }

    private void loginLog(MessageQueueDTO message) {
        MessageQueueLogin data = (MessageQueueLogin) message.getData();
        BallPlayer ballPlayer = data.getBallPlayer();
        String superName = "";
        superName = getSuperPlayerName(superName, ballPlayer);
        //登录日志
        try {
            loggerService.insert(BallLoggerLogin.builder()
                    .devices(data.getDevice())
                    .createdAt(System.currentTimeMillis())
                    .ip(data.getIp())
                    .playerName(ballPlayer.getUsername())
                    .superPlayerName(superName)
                    .build());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //修改最新一次登录IP
        BallPlayer edit = BallPlayer.builder()
                //上次登录IP
                .theLastIp(ballPlayer.getTheLastIp())
                //最新登录IP
                .theNewIp(data.getIp())
                .theNewLoginTime(System.currentTimeMillis())
                .build();
        edit.setId(ballPlayer.getId());
        basePlayerService.editAndClearCache(edit,ballPlayer);
    }
}
