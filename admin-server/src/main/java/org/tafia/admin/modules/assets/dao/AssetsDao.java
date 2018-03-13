package org.tafia.admin.modules.assets.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.assets.model.Assets;

/**
 * Created by Dason on 2018/3/13.
 */
@Repository
public interface AssetsDao extends MongoRepository<Assets, String> {
}
