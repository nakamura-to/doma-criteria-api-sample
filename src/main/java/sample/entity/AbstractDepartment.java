package sample.entity;

import java.util.ArrayList;
import java.util.List;

/** */
public abstract class AbstractDepartment {
  private final List<Employee> employeeList = new ArrayList<>();

  public List<Employee> getEmployeeList() {
    return employeeList;
  }
}
