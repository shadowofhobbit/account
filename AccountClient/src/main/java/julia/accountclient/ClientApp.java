package julia.accountclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp implements CommandLineRunner {
    private final AccountClient accountClient;

    public ClientApp(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        accountClient.testServer();
    }
}
