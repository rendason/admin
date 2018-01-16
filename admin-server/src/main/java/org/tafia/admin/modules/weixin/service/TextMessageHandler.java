package org.tafia.admin.modules.weixin.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.tafia.admin.modules.employee.dao.AttendanceDao;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.Attendance;
import org.tafia.admin.modules.employee.model.User;
import org.tafia.admin.modules.weixin.model.TextReceiveMessage;
import org.tafia.admin.modules.weixin.model.TextReplyMessage;
import org.tafia.admin.modules.weixin.model.WeixinReplyMessage;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TextMessageHandler implements WeixinMessageHandler<TextReceiveMessage> {

    private UserDao userDao;

    private AttendanceDao attendanceDao;

    public TextMessageHandler(UserDao userDao, AttendanceDao attendanceDao) {
        this.userDao = userDao;
        this.attendanceDao = attendanceDao;
    }

    @Override
    public String supportMessageType() {
        return "text";
    }

    @Override
    public WeixinReplyMessage handleMessage(TextReceiveMessage receiveMessage) {
        String question = receiveMessage.getContent();
        String answer = null;
        if (StringUtils.equals(question, "打卡")) {
            User user = userDao.findUserByWeixin(receiveMessage.getFromUserName());
            if (user == null) {
                answer = "请回复“绑定+用户名”进行绑定";
            } else {
                Attendance attendance = new Attendance();
                attendance.setUserId(user.getId());
                attendance.setAction("SignIn");
                attendance.setTimestamp(System.currentTimeMillis());
                attendanceDao.save(attendance);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                answer = "打卡成功 " + user.getUsername() + "\n" + dateFormat.format(new Date(attendance.getTimestamp()));
            }
        } else if (StringUtils.startsWith(question, "绑定")) {
            String username = StringUtils.substring(question, 2);
            User user = userDao.findUserByWeixin(receiveMessage.getFromUserName());
            if (user != null) {
                if (StringUtils.equals(username, user.getUsername())) {
                    answer = "用户已绑定，无需重复绑定";
                } else {
                    answer = "当前微信已绑定其他用户";
                }
            } else {
                user = username == null ? null : userDao.findByUsername(username);
                if (user == null) {
                    answer = "该用户名不存在，请重新输入";
                } else {
                    user.setWeixin(receiveMessage.getFromUserName());
                    userDao.save(user);
                    answer = "绑定成功";
                }
            }
        }
        if (StringUtils.startsWith(question,"解绑")) {
            String username = StringUtils.substring(question, 2);
            User user = userDao.findUserByWeixin(receiveMessage.getFromUserName());
            if (user != null) {
                if (StringUtils.equals(username, user.getUsername())) {
                    user.setWeixin(null);
                    userDao.save(user);
                    answer = "解绑成功";
                } else {
                    answer = "当前微信未绑定" + username;
                }
            } else {
                answer = "当前微信未绑定任何用户";
            }
        }
        return answer == null ? null : createReplyMessage(answer, receiveMessage);
    }

    private TextReplyMessage createReplyMessage(String replyContent, TextReceiveMessage receiveMessage) {
        TextReplyMessage replyMessage = new TextReplyMessage();
        replyMessage.setFromUserName(receiveMessage.getToUserName());
        replyMessage.setToUserName(receiveMessage.getFromUserName());
        replyMessage.setMsgType("text");
        replyMessage.setCreateTime((int) (System.currentTimeMillis() / 1000));
        replyMessage.setContent(replyContent);
        return replyMessage;
    }
}
