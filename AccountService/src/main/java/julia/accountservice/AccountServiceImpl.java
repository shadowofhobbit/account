package julia.accountservice;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Cacheable(cacheNames = "amounts", key="#id")
    @Transactional
    public Long getAmount(Integer id) {
        return accountRepository.findById(id)
                .map(Account::getBalance)
                .orElse(0L);
    }

    @CachePut(cacheNames = "amounts", key="#id")
    @Transactional
    public Long addAmount(Integer id, Long value)  {
        Account account = accountRepository.findById(id)
                .map(found -> {
                    found.setBalance(found.getBalance() + value);
                    return found;
                })
                .orElse(new Account(id, value));
        accountRepository.save(account);
        return account.getBalance();
    }
}
