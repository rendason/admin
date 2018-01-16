package org.tafia.admin.modules.employee.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tafia.admin.modules.employee.model.WeixinTextMessage;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Dason on 2018/1/13.
 */
@BasePathAwareController
@ResponseBody
@RequestMapping(value = "/weixin")
public class WeixinController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.weixin.token}")
    private String token;

    @GetMapping(value = "/entrance", produces = MediaType.TEXT_PLAIN_VALUE)
    public String access(@RequestParam("signature") String signature,
                         @RequestParam("timestamp") Long timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("echostr") String echostr) {
        logger.debug("Receive params : signature={}, timestamp={}, nonce={}, echostr={}",
                signature, timestamp, nonce, echostr);
        String paramsString = Stream.of(token, String.valueOf(timestamp), nonce)
                .sorted()
                .collect(Collectors.joining());
        logger.debug("String of parameters : {}", paramsString);
        String digest = DigestUtils.sha1Hex(paramsString);
        logger.debug("Calculate the digest : {}", digest);
        return signature.equals(digest) ? echostr : "";
    }

    @PostMapping(value = "/entrance", consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public WeixinTextMessage message(@RequestBody WeixinTextMessage message) {
        WeixinTextMessage reply = new WeixinTextMessage();
        reply.setFromUserName(message.getToUserName());
        reply.setToUserName(message.getFromUserName());
        reply.setCreateTime((int) Instant.now().getEpochSecond());
        reply.setContent(message.getContent());
        reply.setMsgType(message.getMsgType());
        return reply;
    }
}
