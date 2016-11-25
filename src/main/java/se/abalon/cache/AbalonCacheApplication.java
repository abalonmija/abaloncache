package se.abalon.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AbalonCacheApplication{

	public static void main(String[] args) {
		SpringApplication.run(AbalonCacheApplication.class, args);
	}
}
/*
@SpringBootApplication
public class AbalonCacheApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(AbalonCacheApplication.class, args);
	}


	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... arg0) throws Exception {
		String sql = "SELECT beskr FROM land ORDER BY beskr";
		jdbcTemplate.setFetchSize(10);
		List<Map<String, Object>> countryList =  jdbcTemplate.queryForList(sql);

		for (Map country : countryList) {
			System.out.println("Land: " + country.get("beskr"));
		}
	}
}*/
