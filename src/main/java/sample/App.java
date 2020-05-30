package sample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.tx.TransactionManager;
import sample.entity.Address_;
import sample.entity.Department;
import sample.entity.Department_;
import sample.entity.Employee;
import sample.entity.Employee_;

public class App {

  private final TransactionManager transactionManager;
  private final Entityql entityql;

  public App(Config config) {
    transactionManager = config.getTransactionManager();
    entityql = new Entityql(config);
  }

  public static void main(String[] args) {
    String url = Objects.requireNonNull(args[0]);
    String user = Objects.requireNonNull(args[1]);
    String password = Objects.requireNonNull(args[2]);
    DbConfig config = new DbConfig(url, user, password);
    App app = new App(config);
    app.run();
  }

  private void run() {
    // トランザクションの中で実行
    transactionManager.required(
        () -> {
          // 部署名で従業員をDBから検索
          List<Employee> employees = fetchEmployeesByDepartmentName("ACCOUNTING");

          // 従業員の名前、所属部署、部署内の順序(index)、住所を出力
          employees.forEach(
              employee -> {
                String employeeName = employee.getEmployeeName();
                Department department = employee.getDepartment();
                String departmentName = department.getDepartmentName();
                int index = department.getEmployeeList().indexOf(employee);
                String street = employee.getAddress().getStreet();
                System.out.printf("%s, %s, %d, %s%n", employeeName, departmentName, index, street);
              });

          // 従業員の給料を変更
          employees.forEach(
              employee -> {
                BigDecimal salary = employee.getSalary();
                employee.setSalary(salary.add(new BigDecimal(100)));
              });

          // 従業員の給料の変更をDBへ更新
          update(employees);
        });
  }

  private List<Employee> fetchEmployeesByDepartmentName(String departmentName) {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Address_ a = new Address_();

    return entityql
        .from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .innerJoin(a, on -> on.eq(e.addressId, a.addressId))
        .where(c -> c.eq(d.departmentName, departmentName))
        .orderBy(c -> c.asc(e.employeeName))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .associate(e, a, Employee::setAddress)
        .fetch();
  }

  private void update(List<Employee> employees) {
    Employee_ e = new Employee_();

    entityql.update(e, employees).execute();
  }
}
