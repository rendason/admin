package org.tafia.admin.modules.employee.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tafia.admin.modules.common.model.ResponseMessage;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/punch/{token}")
    public ResponseMessage punch(@PathVariable("token") String token) {
        return attendanceService.punch(token);
    }

    @GetMapping("/leave/{startTime}/{endTime}")
    public ResponseMessage leave(@PathVariable("startTime") String startTime,
                                 @PathVariable("endTime") String endTime) {
        return attendanceService.leave(startTime, endTime);
    }
}
