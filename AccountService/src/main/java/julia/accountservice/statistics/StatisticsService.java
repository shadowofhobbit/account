package julia.accountservice.statistics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsService {
    public static final String COUNTER_AMOUNT_GET_TOTAL = "counter.amount.get.total";
    public static final String COUNTER_AMOUNT_ADD_TOTAL = "counter.amount.add.total";
    public static final String COUNTER_AMOUNT_GET_SECOND = "counter.amount.get.second";
    public static final String COUNTER_AMOUNT_ADD_SECOND = "counter.amount.add.second";
    private AtomicLong totalGetCounter = new AtomicLong(0);
    private AtomicLong getCounter = new AtomicLong(0);
    private AtomicLong totalAddCounter = new AtomicLong(0);
    private AtomicLong addCounter = new AtomicLong(0);

    @Autowired
    public StatisticsService(MeterRegistry registry) {
        Gauge.builder(COUNTER_AMOUNT_GET_SECOND, getCounter, AtomicLong::get)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_GET_TOTAL, totalGetCounter, AtomicLong::get)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_ADD_SECOND, addCounter, AtomicLong::get)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_ADD_TOTAL, totalAddCounter, AtomicLong::get)
                .register(registry);
    }

    public void updateGetAmountCounter() {
        totalGetCounter.getAndIncrement();
        getCounter.getAndIncrement();
    }

    public void updateAddAmountCounter() {
        totalAddCounter.getAndIncrement();
        addCounter.getAndIncrement();
    }

    @Scheduled(fixedRate = 1000)
    private void updateStatsPerSecond() {
        getCounter.set(0);
        addCounter.set(0);
    }

    public void reset() {
        totalAddCounter.set(0);
        totalGetCounter.set(0);
        getCounter.set(0);
        addCounter.set(0);
    }
}
