package org.tafia.admin.modules.weixin.service;

import org.tafia.admin.modules.weixin.model.WeixinReceiveMessage;
import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;

public interface WeixinMessageHandler <T extends WeixinReceiveMessage> {

    String supportMessageType();

    WeixinReplyMessage handleMessage(T receiveMessage);
}
