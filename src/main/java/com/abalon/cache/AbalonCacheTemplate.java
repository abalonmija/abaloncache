package com.abalon.cache;


import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;
import java.util.Map;


public class AbalonCacheTemplate extends JdbcDaoSupport {

    private DataSource dataSource;

    protected JdbcTemplate jdbcTemplateObject;

    public List<Map<String, Object>> test() {
        /*
        String sql = "SELECT beskr FROM land WHERE kod = ?";
        String beskr = jdbcTemplateObject.queryForObject(sql,
                new Object[]{"SE"}, String.class);
        return beskr;
        */

        String sql = "SELECT beskr FROM land ORDER BY beskr";
        getJdbcTemplate().setFetchSize(10);
        List<Map<String, Object>> rows =  getJdbcTemplate().queryForList(sql);
        return rows;
    }
}
