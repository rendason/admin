package org.tafia.admin.modules.finance.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tafia.admin.modules.finance.model.Salary;

/**
 * Created by Dason on 2018/1/13.
 */
public interface SalaryDao extends MongoRepository<Salary, String> {
}
