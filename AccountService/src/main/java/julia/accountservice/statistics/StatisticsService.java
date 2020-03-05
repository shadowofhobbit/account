package julia.accountservice.statistics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    public static final String COUNTER_AMOUNT_GET_TOTAL = "counter.amount.get.total";
    public static final String COUNTER_AMOUNT_ADD_TOTAL = "counter.amount.add.total";
    public static final String COUNTER_AMOUNT_GET_SECOND = "counter.amount.get.second";
    public static final String COUNTER_AMOUNT_ADD_SECOND = "counter.amount.add.second";
    private final Object getLock = new Object();
    private final Object addLock = new Object();
    private long totalGetCounter = 0L;
    private long getCounter = 0L;
    private long totalAddCounter = 0L;
    private long addCounter = 0L;

    @Autowired
    public StatisticsService(MeterRegistry registry) {
        Gauge.builder(COUNTER_AMOUNT_GET_SECOND, this::getGetCounter)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_GET_TOTAL, this::getTotalGetCounter)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_ADD_SECOND, this::getAddCounter)
                .register(registry);
        Gauge.builder(COUNTER_AMOUNT_ADD_TOTAL, this::getTotalAddCounter)
                .register(registry);
    }

    public long getGetCounter() {
        synchronized (getLock) {
            return getCounter;
        }
    }

    public long getTotalGetCounter() {
        synchronized (getLock) {
            return totalGetCounter;
        }
    }

    public long getAddCounter() {
        synchronized (addLock) {
            return addCounter;
        }
    }

    public long getTotalAddCounter() {
        synchronized (addLock) {
            return totalAddCounter;
        }
    }

    public void updateGetAmountCounter() {
        synchronized (getLock) {
            totalGetCounter++;
            getCounter++;
        }
    }

    public void updateAddAmountCounter()  {
        synchronized (addLock) {
            totalAddCounter++;
            addCounter++;
        }
    }

    @Scheduled(fixedRate = 1000)
    private void updateStatsPerSecond() {
        synchronized (getLock) {
            getCounter = 0L;
        }
        synchronized (addLock) {
            addCounter = 0L;
        }
    }

    public void reset() {
        synchronized (getLock) {
            totalGetCounter = 0L;
            getCounter = 0L;
        }
        synchronized (addLock) {
            totalAddCounter = 0L;
            addCounter = 0L;
        }
    }
}
