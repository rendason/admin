package org.tafia.admin.modules.weixin.service;

import org.springframework.stereotype.Service;
import org.tafia.admin.modules.weixin.model.ReceiveTextMessage;
import org.tafia.admin.modules.weixin.model.ReplyTextMessage;
import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;

@Service
public class TextMessageHandler implements WeixinMessageHandler<ReceiveTextMessage> {

    @Override
    public String supportMessageType() {
        return "text";
    }

    @Override
    public WeixinReplyMessage handleMessage(ReceiveTextMessage receiveMessage) {
        ReplyTextMessage  replyMessage = new ReplyTextMessage();
        replyMessage.setFromUserName(receiveMessage.getToUserName());
        replyMessage.setToUserName(receiveMessage.getFromUserName());
        replyMessage.setMsgType("text");
        replyMessage.setCreateTime((int) (System.currentTimeMillis() / 1000));
        replyMessage.setContent(receiveMessage.getContent());
        return replyMessage;
    }
}
