package org.tafia.admin.modules.finance.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Dason on 2018/3/13.
 */
@Repository
public interface Consumption extends MongoRepository<Consumption, String> {
}
