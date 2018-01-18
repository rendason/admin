package org.tafia.admin.modules.employee.service;

import org.tafia.admin.modules.common.model.ResponseMessage;

public interface AttendanceService {

    String generatePunchUrl(String userId);

    /**
     * 打卡
     * @param token 令牌
     * @return 处理结果
     */
    ResponseMessage punch(String token);

    /**
     * 当前用户请假
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 处理结果
     */
    ResponseMessage leave(String startTime, String endTime);

}
