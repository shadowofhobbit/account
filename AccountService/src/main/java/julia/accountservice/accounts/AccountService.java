package julia.accountservice.accounts;

public interface AccountService {

    Long getAmount(Integer id);

    Long addAmount(Integer id, Long value);

}
