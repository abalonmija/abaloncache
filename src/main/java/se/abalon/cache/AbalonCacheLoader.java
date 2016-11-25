package se.abalon.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import se.abalon.cache.dao.WorkPlaceDAO;
import se.abalon.cache.loader.MainCacheLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by mija on 2016-11-17.
 */
@Component
public class AbalonCacheLoader implements CommandLineRunner{
    @Autowired
    AbalonCacheTemplate abalonCacheTemplate;

    /*
    @Autowired
    JdbcTemplate jdbcTemplate;
*/
    @Override
    public void run(String... strings) throws Exception {
        //ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
       // ApplicationContext context = new AnnotationConfigApplicationContext(AbalonCacheTemplate.class);
        System.out.println("Hej hopp");

        List<Map<String, Object>> countryList = abalonCacheTemplate.test();

        for (Map country : countryList) {
            System.out.println(country.get("beskr"));
        }

        List<WorkPlaceDAO> workPlaceDAOs = abalonCacheTemplate.testwork("select id workplace_id,nummer workplace_number,id terminal_terminalnumber from arbetsstalle");
        for (WorkPlaceDAO workPlaceDAO : workPlaceDAOs) {
            System.out.println("ID      : " + workPlaceDAO.getWorkplace_id());
            System.out.println("NUMMBER : " + workPlaceDAO.getWorkplace_number());
            System.out.println("TERMINAL: " + workPlaceDAO.getTerminal_terminalnumber());
        }


    }
}




