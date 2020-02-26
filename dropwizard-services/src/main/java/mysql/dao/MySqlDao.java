package mysql.dao;

import mysql.entity.Employee;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;


public interface MySqlDao {

    @SqlQuery("select * from employee")
    @Mapper(EmployeeMapper.class)
    List<Employee> getEmployeeList();

    @SqlUpdate("insert into employee (id,name,department)values(:id,:name,:department);")
    @Mapper(EmployeeMapper.class)
    int addNewEmp(@BindBean Employee employee);

    @SqlQuery("select * from employee where id=:id")
    @Mapper(EmployeeMapper.class)
    Employee getEmpById(@Bind("id")String id);

    @SqlUpdate("update employee set department=:department, name=:name where id=:id")
    @Mapper(EmployeeMapper.class)
    int updateEmp(@Bind("id") String id,@Bind("name") String name,@Bind("department") String department);

    @SqlUpdate("DELETE FROM employee WHERE id = :id")
    void deleteEmp(@Bind("id") String id );
}
