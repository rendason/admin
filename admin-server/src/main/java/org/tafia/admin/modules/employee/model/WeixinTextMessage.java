package org.tafia.admin.modules.employee.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Dason on 2018/1/13.
 */
@XmlRootElement(name = "xml")
public class WeixinTextMessage {

    private String toUserName;

    private String fromUserName;

    private Integer createTime;

    private String msgType;

    private String content;

    private Long msgId;

    public String getToUserName() {
        return toUserName;
    }

    @XmlElement(name = "ToUserName")
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    @XmlElement(name = "FromUserName")
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    @XmlElement(name = "CreateTime")
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    @XmlElement(name = "MsgType")
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    @XmlElement(name = "Content")
    public void setContent(String content) {
        this.content = content;
    }

    public Long getMsgId() {
        return msgId;
    }

    @XmlElement(name = "MsgId")
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
}
