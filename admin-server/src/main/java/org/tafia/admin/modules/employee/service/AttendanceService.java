package org.tafia.admin.modules.employee.service;

import org.tafia.admin.modules.employee.model.Attendance;

import java.util.List;

public interface AttendanceService {

    /**
     * 打卡
     * @param userId 用户id
     * @param ip ip地址
     * @return 处理结果
     */
    String punch(String userId, long timestamp);

    /**
     * 请假
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 处理结果
     */
    List<Attendance> leave(String userId, String startTime, String endTime);

}
