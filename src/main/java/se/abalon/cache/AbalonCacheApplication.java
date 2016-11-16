package se.abalon.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AbalonCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbalonCacheApplication.class, args);
		ApplicationContext context =
				new ClassPathXmlApplicationContext("beans.xml");
		System.out.println("Hej hopp");

		AbalonCacheTemplate abalonCacheTemplate =
				(AbalonCacheTemplate)context.getBean("abalonCacheTemplate");

		List<Map<String, Object>> countryList = abalonCacheTemplate.test();

		for (Map country : countryList) {
			System.out.println("Land: " + country.get("beskr"));
		}
	}
}
