package julia.accountservice.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Cacheable(cacheNames = "amounts", key="#id")
    @Transactional(readOnly = true)
    public Long getAmount(Integer id) {
        return accountRepository.findById(id)
                .map(Account::getBalance)
                .orElse(0L);
    }

    @CachePut(cacheNames = "amounts", key="#id")
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = SQLException.class)
    @Retryable(value = SQLException.class)
    public Long addAmount(Integer id, Long value)  {
        Account account = accountRepository.findById(id)
                .map(found -> {
                    found.setBalance(found.getBalance() + value);
                    return found;
                })
                .orElseGet(() -> accountRepository.save(new Account(id, value)));
        return account.getBalance();
    }
}
