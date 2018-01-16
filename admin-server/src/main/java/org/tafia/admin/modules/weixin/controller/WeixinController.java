package org.tafia.admin.modules.weixin.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;
import org.tafia.admin.modules.weixin.service.WeixinService;

/**
 * Created by Dason on 2018/1/13.
 */
@BasePathAwareController
@ResponseBody
@RequestMapping(value = "/weixin")
public class WeixinController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private WeixinService weixinService;

    public WeixinController(WeixinService weixinService) {
        this.weixinService = weixinService;
    }

    @GetMapping(value = "/entrance", produces = MediaType.TEXT_PLAIN_VALUE)
    public String access(@RequestParam("signature") String signature,
                         @RequestParam("timestamp") Long timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("echostr") String echostr) {
        logger.debug("Receive params : signature={}, timestamp={}, nonce={}, echostr={}",
                signature, timestamp, nonce, echostr);
        String digest = weixinService.signature(timestamp, nonce);
        return StringUtils.equals(signature, digest) ? echostr : "";
    }

    @PostMapping(value = "/entrance", consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public WeixinReplyMessage message(@RequestBody String message) {
        return weixinService.onMessage(message);
    }
}
