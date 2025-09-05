package com.act.ldk.domain.repository;

import com.act.ldk.domain.entity.MemberPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberPrincipalRepository extends JpaRepository<MemberPrincipal, Long> {
    Optional<MemberPrincipal> findByUsername(String username);
}