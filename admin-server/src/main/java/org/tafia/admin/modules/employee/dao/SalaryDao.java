package org.tafia.admin.modules.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.employee.model.Salary;

@Repository
@PreAuthorize("hasAuthority('owner')")
public interface SalaryDao extends MongoRepository<Salary, String> {
}
