package org.tafia.admin.modules.weixin.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.tafia.admin.modules.weixin.model.WeixinReceiveMessage;
import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultWeixinService implements WeixinService, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.weixin.token}")
    private String token;

    private Map<Class<?>, JAXBContext> jaxbContexts = new HashMap<>();

    private Map<String, WeixinMessageHandler> weixinMessageHandlers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        weixinMessageHandlers = Collections.unmodifiableMap(
                applicationContext.getBeansOfType(WeixinMessageHandler.class).values().stream()
                .collect(Collectors.toMap(WeixinMessageHandler::supportMessageType, Function.identity()))
        );
    }

    @Override
    public String signature(long timestamp, String nonce) {
        String paramsString = Stream.of(token, String.valueOf(timestamp), nonce)
                .sorted()
                .collect(Collectors.joining());
        return DigestUtils.sha1Hex(paramsString);
    }

    @Override
    public WeixinReplyMessage onMessage(String message) {
        String messageType = getMessageType(message);
        WeixinMessageHandler<?> messageHandler = weixinMessageHandlers.get(messageType);
        if (messageHandler == null) return null;
        Class<?> receiveMessageClass = getReceiveMessageClass(messageHandler);
        if (receiveMessageClass == null) return null;
        Object receiveMessage = mappingReceiveMessage(message, receiveMessageClass);
        try {
            Method method = messageHandler.getClass().getMethod("handleMessage", receiveMessageClass);
            return (WeixinReplyMessage) method.invoke(messageHandler, receiveMessage);
        } catch (Exception e) {
            logger.warn("Error occurs on handling message", e);
            return null;
        }
    }

    private JAXBContext getJAXBContext(Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            jaxbContexts.putIfAbsent(clazz, (jaxbContext = JAXBContext.newInstance(clazz)));
        }
        return jaxbContext;
    }

    private Unmarshaller getUnmarshaller(Class<?> clazz) throws JAXBException {
        JAXBContext jaxbContext = getJAXBContext(clazz);
        return jaxbContext.createUnmarshaller();
    }

    private Source getSource(String message) {
        InputStream inputStream = IOUtils.toInputStream(message, StandardCharsets.UTF_8);
        return new StreamSource(inputStream);
    }

    private String getMessageType(String message) {
        WeixinReceiveMessage weixinReceiveMessage = (WeixinReceiveMessage) mappingReceiveMessage(message, WeixinReceiveMessage.class);
        return weixinReceiveMessage == null ? null : weixinReceiveMessage.getMsgType();
    }

    private Object mappingReceiveMessage(String message, Class<?> clazz) {
        try {
            Unmarshaller unmarshaller = getUnmarshaller(clazz);
            Source source = getSource(message);
            JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, clazz);
            return jaxbElement.getValue();
        } catch (JAXBException e) {
            logger.warn("Error occurs on creating unmarshaller", e);
            return null;
        }
    }

    private Class<?> getReceiveMessageClass(WeixinMessageHandler<?> messageHandler) {
        Type[] types = messageHandler.getClass().getGenericInterfaces();
        Type type;
        if (types.length != 0 && (type = types[0]) instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }
}
