package julia.accountclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountClient {
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${accountclient.serverUrl}")
    private String url;
    @Value("${accountclient.rCount}")
    private Integer rCount;
    @Value("${accountclient.wCount}")
    private Integer wCount;
    @Value("${accountclient.idList}")
    private List<Integer> idList;


    void testServer() throws InterruptedException {
        System.out.println(rCount);
        var executor = Executors.newFixedThreadPool(rCount + wCount);
        var tasks = new ArrayList<Callable<Void>>(rCount + wCount);
        for (int i = 0; i < rCount; i++) {
            var idIndex = ThreadLocalRandom.current().nextInt(0, idList.size());
            String requestUrl = url + idList.get(idIndex) + "/";
            tasks.add(new ReaderCallable(restTemplate, requestUrl));
        }
        for (int i = 0; i < wCount; i++) {
            var idIndex = ThreadLocalRandom.current().nextInt(0, idList.size());
            var amount = ThreadLocalRandom.current().nextLong();
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
            String result = restTemplate.getForObject(url, String.class);
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
            restTemplate.execute(url, HttpMethod.POST, null, null);
            return null;
        }
    }
}
