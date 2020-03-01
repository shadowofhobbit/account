package julia.accountservice.accounts;

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
     * Increases balance or sets if addAmount() method was called first time
     *
     * @param id balance identifier
     * @param value positive or negative value, which must be added to current balance
     */
    @PostMapping
    public void addAmount(@RequestParam Integer id, @RequestParam Long value) {
        accountService.addAmount(id, value);
    }
}
