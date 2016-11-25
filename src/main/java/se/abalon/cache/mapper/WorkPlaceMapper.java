package se.abalon.cache.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import se.abalon.cache.dao.WorkPlaceDAO;

public class WorkPlaceMapper implements RowMapper<WorkPlaceDAO>
{
    public WorkPlaceDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkPlaceDAO workPlaceDAO = new WorkPlaceDAO();
        workPlaceDAO.setWorkplace_id(rs.getLong("workplace_id"));
        workPlaceDAO.setWorkplace_number(rs.getString("workplace_number"));
        workPlaceDAO.setTerminal_terminalnumber(rs.getString("terminal_terminalnumber"));
        return workPlaceDAO;
    }
}
