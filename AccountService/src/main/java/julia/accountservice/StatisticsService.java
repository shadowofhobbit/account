package julia.accountservice;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsService {
    private final MeterRegistry registry;
    private AtomicLong getCounter = new AtomicLong(0);
    private AtomicLong addCounter = new AtomicLong(0);

    public StatisticsService(MeterRegistry registry) {
        this.registry = registry;
//        Gauge.builder("counter.amount.get.minute", getCounter, AtomicLong::get)
//                .register(registry);
        Gauge.builder("counter.amount.get.total", getCounter, AtomicLong::get)
                .register(registry);
//        Gauge.builder("counter.amount.add.minute", addCounter, AtomicLong::get)
//                .register(registry);
        Gauge.builder("counter.amount.add.total", addCounter, AtomicLong::get)
                .register(registry);
    }

    public void updateGetAmountCounter() {
        getCounter.getAndIncrement();
    }

    public void updateAddAmountCounter() {
        addCounter.getAndIncrement();
    }

    public void reset() {
        addCounter.set(0);
        getCounter.set(0);
    }
}
