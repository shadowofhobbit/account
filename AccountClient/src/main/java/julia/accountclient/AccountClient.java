package julia.accountclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountClient {
    private final RestTemplate restTemplate;
    @Value("${accountclient.serverUrl}")
    private String url;
    @Value("${accountclient.rCount}")
    private Integer rCount;
    @Value("${accountclient.wCount}")
    private Integer wCount;
    @Value("${accountclient.idList}")
    private List<Integer> idList;
    private static final Logger log = LogManager.getLogger(AccountClient.class);

    @Autowired
    public AccountClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    void testServer() throws InterruptedException {
        int threads = rCount + wCount;
        var executor = Executors.newFixedThreadPool(threads);
        var tasks = new ArrayList<Callable<Void>>(threads);
        for (int i = 0; i < rCount; i++) {
            var idIndex = ThreadLocalRandom.current().nextInt(0, idList.size());
            String requestUrl = url + idList.get(idIndex) + "/";
            tasks.add(new ReaderCallable(restTemplate, requestUrl));
        }
        for (int i = 0; i < wCount; i++) {
            var idIndex = ThreadLocalRandom.current().nextInt(0, idList.size());
            var amount = 100;
            String requestUrl = url + "?id=" + idList.get(idIndex) + "&value=" + amount;
            tasks.add(new WriterCallable(restTemplate, requestUrl));
        }
        executor.invokeAll(tasks);
        executor.shutdown();
    }

    private static class ReaderCallable implements Callable<Void> {
        private final RestTemplate restTemplate;
        private final String url;

        ReaderCallable(RestTemplate restTemplate, String url) {
            this.restTemplate = restTemplate;
            this.url = url;
        }

        @Override
        public Void call() {
            var response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error getting amount " + response.getStatusCode());
            }
            return null;
        }
    }

    private static class WriterCallable implements Callable<Void> {
        private final RestTemplate restTemplate;
        private final String url;

        WriterCallable(RestTemplate restTemplate, String url) {
            this.restTemplate = restTemplate;
            this.url = url;
        }

        @Override
        public Void call() {
            var response = restTemplate.postForEntity(url, null, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error adding amount " + response.getStatusCode());
            }
            return null;
        }
    }
}
