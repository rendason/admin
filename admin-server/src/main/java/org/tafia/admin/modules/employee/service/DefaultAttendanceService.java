package org.tafia.admin.modules.employee.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tafia.admin.modules.common.util.RequestUtils;
import org.tafia.admin.modules.employee.dao.AttendanceDao;
import org.tafia.admin.modules.employee.dao.AuthorizedAddressDao;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.Attendance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultAttendanceService implements AttendanceService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDao userDao;

    private AttendanceDao attendanceDao;

    private AuthorizedAddressDao authorizedAddressDao;

    @Value("${spring.punch.url.interval:60000}")
    private long validInterval;

    public DefaultAttendanceService(UserDao userDao, AttendanceDao attendanceDao, AuthorizedAddressDao authorizedAddressDao) {
        this.userDao = userDao;
        this.attendanceDao = attendanceDao;
        this.authorizedAddressDao = authorizedAddressDao;
    }

    @Override
    public String punch(String userId, long timestamp) {
        if (StringUtils.isEmpty(userId) || !userDao.exists(userId)) return "无效链接，请重新获取";
        if (!authorizedAddressDao.existsByIp(RequestUtils.getIp())) return "请使用指定网络重新访问";
        if (System.currentTimeMillis() - timestamp > validInterval) return "链接失效，请重新获取";
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setAction("PUNCH");
        attendance.setTimestamp(System.currentTimeMillis());
        attendanceDao.save(attendance);
        return "打卡成功";
    }

    @Override
    public List<Attendance> leave(String userId, String startTime, String endTime) {
        if (StringUtils.isNoneEmpty(userId, startTime, endTime) && startTime.compareTo(endTime) < 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long start = simpleDateFormat.parse(startTime).getTime();
                long end = simpleDateFormat.parse(endTime).getTime();
                Attendance attendance1 = new Attendance();
                attendance1.setUserId(userId);
                attendance1.setAction("LEAVE");
                attendance1.setTimestamp(start);
                Attendance attendance2 = new Attendance();
                attendance2.setUserId(userId);
                attendance2.setAction("LEAVE");
                attendance2.setTimestamp(end);
                List<Attendance> attendances = Arrays.asList(attendance1, attendance2);
                return attendanceDao.save(attendances);
            } catch (ParseException e) {
                logger.warn("Error occurs on parsing date {} or {}", startTime, endTime, e);
            }
        }
        return Collections.emptyList();
    }
}
