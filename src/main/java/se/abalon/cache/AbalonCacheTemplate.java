package se.abalon.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import se.abalon.cache.dao.WorkPlaceDAO;
import se.abalon.cache.mapper.WorkPlaceMapper;

import java.util.List;
import java.util.Map;

@Component
public class AbalonCacheTemplate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> test() {

        String sql = "SELECT beskr FROM land ORDER BY beskr";
        jdbcTemplate.setFetchSize(10);
        List<Map<String, Object>> rows =  jdbcTemplate.queryForList(sql);
        return rows;
    }

    public List<WorkPlaceDAO> testwork(String sql){
        List<WorkPlaceDAO> workPlaceDAOs  = jdbcTemplate.query(sql,
                new WorkPlaceMapper());

        return workPlaceDAOs;


    }
    public List<Map<String, Object>> getSingleField(String sql) {
        List<Map<String, Object>> rows =  jdbcTemplate.queryForList(sql);
        return rows;
    }
}
