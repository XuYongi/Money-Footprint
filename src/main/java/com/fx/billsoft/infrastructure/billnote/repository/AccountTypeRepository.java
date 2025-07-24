package com.fx.billsoft.infrastructure.billnote.repository;

import com.fx.billsoft.domain.billnote.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    List<AccountType> findByUserId(Long userId);
    List<AccountType> findByUserIdAndCategory(Long userId, Integer category);
}