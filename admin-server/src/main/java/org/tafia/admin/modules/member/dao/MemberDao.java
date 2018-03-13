package org.tafia.admin.modules.member.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.member.model.Member;

@Repository
@PreAuthorize("hasAnyAuthority('owner', 'manager', 'ordinary')")
public interface MemberDao extends MongoRepository<Member, String> {
}
