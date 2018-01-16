package org.tafia.admin.modules.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tafia.admin.modules.employee.model.Attendance;

/**
 * Created by Dason on 2018/1/13.
 */
public interface AttendanceDao extends MongoRepository<Attendance, String> {
}
