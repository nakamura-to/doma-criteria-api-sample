package sample.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

/** */
@Entity(listener = EmployeeListener.class, metamodel = @Metamodel)
@Table(name = "employee")
public class Employee extends AbstractEmployee {

  /** */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "employee_employee_id")
  @Column(name = "employee_id")
  Integer employeeId;

  /** */
  @Column(name = "employee_no")
  Integer employeeNo;

  /** */
  @Column(name = "employee_name")
  String employeeName;

  /** */
  @Column(name = "manager_id")
  Integer managerId;

  /** */
  @Column(name = "hiredate")
  LocalDate hiredate;

  /** */
  @Column(name = "salary")
  BigDecimal salary;

  /** */
  @Column(name = "department_id")
  Integer departmentId;

  /** */
  @Column(name = "address_id")
  Integer addressId;

  /** */
  @Version
  @Column(name = "version")
  Integer version;

  /**
   * Returns the employeeId.
   *
   * @return the employeeId
   */
  public Integer getEmployeeId() {
    return employeeId;
  }

  /**
   * Sets the employeeId.
   *
   * @param employeeId the employeeId
   */
  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  /**
   * Returns the employeeNo.
   *
   * @return the employeeNo
   */
  public Integer getEmployeeNo() {
    return employeeNo;
  }

  /**
   * Sets the employeeNo.
   *
   * @param employeeNo the employeeNo
   */
  public void setEmployeeNo(Integer employeeNo) {
    this.employeeNo = employeeNo;
  }

  /**
   * Returns the employeeName.
   *
   * @return the employeeName
   */
  public String getEmployeeName() {
    return employeeName;
  }

  /**
   * Sets the employeeName.
   *
   * @param employeeName the employeeName
   */
  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  /**
   * Returns the managerId.
   *
   * @return the managerId
   */
  public Integer getManagerId() {
    return managerId;
  }

  /**
   * Sets the managerId.
   *
   * @param managerId the managerId
   */
  public void setManagerId(Integer managerId) {
    this.managerId = managerId;
  }

  /**
   * Returns the hiredate.
   *
   * @return the hiredate
   */
  public LocalDate getHiredate() {
    return hiredate;
  }

  /**
   * Sets the hiredate.
   *
   * @param hiredate the hiredate
   */
  public void setHiredate(LocalDate hiredate) {
    this.hiredate = hiredate;
  }

  /**
   * Returns the salary.
   *
   * @return the salary
   */
  public BigDecimal getSalary() {
    return salary;
  }

  /**
   * Sets the salary.
   *
   * @param salary the salary
   */
  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  /**
   * Returns the departmentId.
   *
   * @return the departmentId
   */
  public Integer getDepartmentId() {
    return departmentId;
  }

  /**
   * Sets the departmentId.
   *
   * @param departmentId the departmentId
   */
  public void setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
  }

  /**
   * Returns the addressId.
   *
   * @return the addressId
   */
  public Integer getAddressId() {
    return addressId;
  }

  /**
   * Sets the addressId.
   *
   * @param addressId the addressId
   */
  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  /**
   * Returns the version.
   *
   * @return the version
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * Sets the version.
   *
   * @param version the version
   */
  public void setVersion(Integer version) {
    this.version = version;
  }
}
