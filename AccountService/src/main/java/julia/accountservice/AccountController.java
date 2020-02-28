package julia.accountservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * Retrieves current balance or zero if addAmount() method was not called before for specified id
     *  
     * @param id balance identifier
     */
    @GetMapping("{id}/")
    public Long getAmount(@PathVariable Integer id) {
        return accountService.getAmount(id);
    }

    /**
     * Increases balance or set if addAmount() method was called first time
     */
    @PostMapping
    public void addAmount(@RequestBody AccountInvoice accountInvoice) {
        accountService.addAmount(accountInvoice.getId(), accountInvoice.getAmount());
    }
}
