package backend.spectrum.dguonoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DguonoffApplication {

	public static void main(String[] args) {
		SpringApplication.run(DguonoffApplication.class, args);
	}

}
