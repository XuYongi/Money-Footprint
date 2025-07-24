package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.entity.AccountType;
import com.fx.billsoft.domain.billnote.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountTypeAppService {
    private final AccountTypeService accountTypeService;

    @Autowired
    public AccountTypeAppService(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    public AccountType createAccountType(AccountType accountType) {
        return accountTypeService.createAccountType(accountType);
    }

    public Optional<AccountType> getAccountTypeById(Long id) {
        return accountTypeService.getAccountTypeById(id);
    }

    public AccountType updateAccountType(AccountType accountType) {
        return accountTypeService.updateAccountType(accountType);
    }

    public void deleteAccountType(Long id) {
        accountTypeService.deleteAccountType(id);
    }

    public List<AccountType> getAccountTypesByUserId(Long userId) {
        return accountTypeService.getAccountTypesByUserId(userId);
    }

    public List<AccountType> getAccountTypesByUserIdAndCategory(Long userId, Integer category) {
        return accountTypeService.getAccountTypesByUserIdAndCategory(userId, category);
    }
}