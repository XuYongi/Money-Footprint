package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.domain.billnote.entity.AccountType;
import com.fx.billsoft.infrastructure.billnote.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;

    @Autowired
    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    public AccountType createAccountType(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    public Optional<AccountType> getAccountTypeById(Long id) {
        return accountTypeRepository.findById(id);
    }

    public AccountType updateAccountType(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    public void deleteAccountType(Long id) {
        accountTypeRepository.deleteById(id);
    }

    public List<AccountType> getAccountTypesByUserId(Long userId) {
        return accountTypeRepository.findByUserId(userId);
    }

    public List<AccountType> getAccountTypesByUserIdAndCategory(Long userId, Integer category) {
        return accountTypeRepository.findByUserIdAndCategory(userId, category);
    }
}