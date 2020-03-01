package julia.accountservice.statistics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsService {
    public static final String COUNTER_AMOUNT_GET_TOTAL = "counter.amount.get.total";
    public static final String COUNTER_AMOUNT_ADD_TOTAL = "counter.amount.add.total";
    private AtomicLong getCounter = new AtomicLong(0);
    private AtomicLong addCounter = new AtomicLong(0);

    @Autowired
    public StatisticsService(MeterRegistry registry) {
        //        Gauge.builder("counter.amount.get.minute", getCounter, AtomicLong::get)
//                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_GET_TOTAL, getCounter, AtomicLong::get)
                .register(registry);
//        Gauge.builder("counter.amount.add.minute", addCounter, AtomicLong::get)
//                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_ADD_TOTAL, addCounter, AtomicLong::get)
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
