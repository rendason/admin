package org.tafia.admin.modules.inventory.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.inventory.model.Cargo;

@Repository
@PreAuthorize("hasAuthority('employee')")
public interface InventoryDao extends MongoRepository<Cargo, String> {
}
