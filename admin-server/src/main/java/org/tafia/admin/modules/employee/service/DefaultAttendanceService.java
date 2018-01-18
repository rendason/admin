package org.tafia.admin.modules.employee.service;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tafia.admin.Application;
import org.tafia.admin.modules.common.model.ResponseMessage;
import org.tafia.admin.modules.common.util.RequestUtils;
import org.tafia.admin.modules.employee.dao.AttendanceDao;
import org.tafia.admin.modules.employee.dao.AuthorizedAddressDao;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.Attendance;
import org.tafia.admin.modules.employee.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultAttendanceService implements AttendanceService {

    private UserDao userDao;

    private AttendanceDao attendanceDao;

    private AuthorizedAddressDao authorizedAddressDao;

    @Value("${spring.punch.url.interval:60000}")
    private long validInterval;

    private Map<String, String> userNonceMap = new ConcurrentHashMap<>();

    public DefaultAttendanceService(UserDao userDao, AttendanceDao attendanceDao, AuthorizedAddressDao authorizedAddressDao) {
        this.userDao = userDao;
        this.attendanceDao = attendanceDao;
        this.authorizedAddressDao = authorizedAddressDao;
    }

    @Override
    public String generatePunchUrl(String userId) {
        String nonce = generateNonce();
        userNonceMap.put(userId, nonce);
        String token = userId + nonce + System.currentTimeMillis();
        return RequestUtils.getBasePath() + "/attendance/punch/" + encodePunchToken(token);
    }

    @Override
    public ResponseMessage punch(String token) {
        token = decodePunchToken(token);
        if (StringUtils.length(token) < 41) {
            return new ResponseMessage("无效链接，请重新获取");
        }
        String userId = token.substring(0, 24);
        String nonce = token.substring(24, 28);
        String timestamp = token.substring(28);
        if (!userDao.exists(userId) || !nonce.equals(userNonceMap.remove(userId))
                || !StringUtils.isNumeric(timestamp)
                || System.currentTimeMillis() - Long.valueOf(timestamp) > validInterval) {
            return new ResponseMessage("链接已失效，请重新获取");
        }
        if (!authorizedAddressDao.existsByIp(RequestUtils.getIp()))
            return new ResponseMessage("请使用指定网络重新访问");
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setAction("PUNCH");
        attendance.setTimestamp(System.currentTimeMillis());
        return new ResponseMessage("打卡成功", attendanceDao.save(attendance));
    }

    @Override
    public ResponseMessage leave(String startTime, String endTime) {
        User user = Application.currentUser();
        if (user == null) return new ResponseMessage("用户未登录");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start, end;
        try {
            if (StringUtils.isAnyEmpty(startTime, endTime) || startTime.compareTo(endTime) >= 0) {
                throw new IllegalArgumentException();
            }
            start = simpleDateFormat.parse(startTime).getTime();
            end = simpleDateFormat.parse(endTime).getTime();
        } catch (ParseException | IllegalArgumentException e) {
            return new ResponseMessage("时间格式错误");
        }
        String userId = user.getId();
        List<Attendance> attendances = Arrays.asList(createLeaveAttendance(userId, start), createLeaveAttendance(userId, end));
        return new ResponseMessage("请假成功", attendanceDao.save(attendances));
    }

    private Attendance createLeaveAttendance(String userId, long timestamp) {
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setAction("LEAVE");
        attendance.setTimestamp(timestamp);
        return attendance;
    }

    private String generateNonce() {
        return String.valueOf(RandomUtils.nextInt(1000, 10000));
    }

    private String encodePunchToken(String token) {
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    private String decodePunchToken(String token) {
        return new String(Base64.getDecoder().decode(token));
    }
}
