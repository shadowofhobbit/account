package julia.accountservice.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class StatisticsFilter extends OncePerRequestFilter {

    private final StatisticsService metricService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws java.io.IOException, ServletException {
        String method = request.getMethod();
        if (method.equals(HttpMethod.POST.name())) {
            metricService.updateAddAmountCounter();
        } else if (method.equals(HttpMethod.GET.name())) {
            metricService.updateGetAmountCounter();
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/accounts");
    }
}
