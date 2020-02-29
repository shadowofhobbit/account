package julia.accountservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final StatisticsService statisticsService;

    /**
     * Retrieves current balance or zero if addAmount() method was not called before for specified id
     *  
     * @param id balance identifier
     */
    @GetMapping("{id}/")
    public Long getAmount(@PathVariable Integer id) {
        statisticsService.updateGetAmountCounter();
        return accountService.getAmount(id);
    }

    /**
     * Increases balance or set if addAmount() method was called first time
     */
    @PostMapping
    public void addAmount(@RequestBody AccountInvoice accountInvoice) {
        statisticsService.updateAddAmountCounter();
        accountService.addAmount(accountInvoice.getId(), accountInvoice.getAmount());
    }
}
