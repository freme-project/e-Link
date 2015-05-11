package eu.freme.eservices.elink;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ELinkConfig {
	@Bean
	public DataEnricher getDataEnricher(){
		return new DataEnricher();
	}
}
