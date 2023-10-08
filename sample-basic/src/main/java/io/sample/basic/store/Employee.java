package io.sample.basic.store;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {

    public enum EmployeePosition {
        MANAGER,
        CASHIER
    }

    @JsonProperty("employeeId")
    private Integer id;
    @JsonProperty("employeeName")
    private String name;
    @JsonProperty("employeePosition")
    private EmployeePosition position;

    public Employee() {
    }

    public Employee(Integer id, String name, EmployeePosition position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }
}
