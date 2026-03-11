package db.biometry.biometry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication


@EnableDiscoveryClient
@RefreshScope
@EnableAsync  // ✅ Ajouter cette annotation
public class BiometryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiometryApplication.class, args);
	}

}
