package org.tafia.admin.modules.weixin.service;

import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;

public interface WeixinService {

    String signature(long timestamp, String nonce);

    WeixinReplyMessage onMessage(String message);
}
