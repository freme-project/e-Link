package eu.freme.eservices.elink;

import eu.freme.eservices.elink.api.DataEnricher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ELinkConfig {
	@Bean
	public DataEnricher getDataEnricher(){
		return new DataEnricher();
	}
}
