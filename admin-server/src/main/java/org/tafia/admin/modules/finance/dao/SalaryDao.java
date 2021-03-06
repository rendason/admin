package org.tafia.admin.modules.finance.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.finance.model.Salary;

/**
 * Created by Dason on 2018/1/13.
 */
@Repository
public interface SalaryDao extends MongoRepository<Salary, String> {
}
