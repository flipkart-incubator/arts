package mysql.entity;


import javax.persistence.*;

@Entity(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;
    @Column(name="department")
    private String department;

    public Employee(){}

    public Employee(String id, String name, String department) {
        this.id= id;
        this.department=department;
        this.name=name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
