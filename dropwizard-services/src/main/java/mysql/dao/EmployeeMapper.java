package mysql.dao;

import mysql.entity.Employee;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements ResultSetMapper<Employee> {
    public Employee map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return  new Employee(resultSet.getString("id")
                , resultSet.getString("name"),resultSet.getString("department") );
    }

}
