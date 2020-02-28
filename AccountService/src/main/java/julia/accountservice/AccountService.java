package julia.accountservice;

public interface AccountService {

    Long getAmount(Integer id);

    Long addAmount(Integer id, Long value);

}
