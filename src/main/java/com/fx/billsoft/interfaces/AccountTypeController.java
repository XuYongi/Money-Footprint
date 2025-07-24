package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.AccountTypeAppService;
import com.fx.billsoft.domain.billnote.entity.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account-types")
public class AccountTypeController {
    private final AccountTypeAppService accountTypeAppService;

    @Autowired
    public AccountTypeController(AccountTypeAppService accountTypeAppService) {
        this.accountTypeAppService = accountTypeAppService;
    }

    @PostMapping
    public ResponseEntity<AccountType> createAccountType(@RequestBody AccountType accountType) {
        AccountType createdAccountType = accountTypeAppService.createAccountType(accountType);
        return ResponseEntity.ok(createdAccountType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountType> getAccountTypeById(@PathVariable Long id) {
        Optional<AccountType> accountType = accountTypeAppService.getAccountTypeById(id);
        return accountType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<AccountType> updateAccountType(@RequestBody AccountType accountType) {
        AccountType updatedAccountType = accountTypeAppService.updateAccountType(accountType);
        return ResponseEntity.ok(updatedAccountType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountType(@PathVariable Long id) {
        accountTypeAppService.deleteAccountType(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountType>> getAccountTypesByUserId(@PathVariable Long userId) {
        List<AccountType> accountTypes = accountTypeAppService.getAccountTypesByUserId(userId);
        return ResponseEntity.ok(accountTypes);
    }

    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<AccountType>> getAccountTypesByUserIdAndCategory(@PathVariable Long userId, @PathVariable Integer category) {
        List<AccountType> accountTypes = accountTypeAppService.getAccountTypesByUserIdAndCategory(userId, category);
        return ResponseEntity.ok(accountTypes);
    }
}