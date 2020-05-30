package sample.entity;

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
@Entity(listener = DepartmentListener.class, metamodel = @Metamodel)
@Table(name = "department")
public class Department extends AbstractDepartment {

  /** */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "department_department_id")
  @Column(name = "department_id")
  Integer departmentId;

  /** */
  @Column(name = "department_no")
  Integer departmentNo;

  /** */
  @Column(name = "department_name")
  String departmentName;

  /** */
  @Column(name = "location")
  String location;

  /** */
  @Version
  @Column(name = "version")
  Integer version;

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
   * Returns the departmentNo.
   *
   * @return the departmentNo
   */
  public Integer getDepartmentNo() {
    return departmentNo;
  }

  /**
   * Sets the departmentNo.
   *
   * @param departmentNo the departmentNo
   */
  public void setDepartmentNo(Integer departmentNo) {
    this.departmentNo = departmentNo;
  }

  /**
   * Returns the departmentName.
   *
   * @return the departmentName
   */
  public String getDepartmentName() {
    return departmentName;
  }

  /**
   * Sets the departmentName.
   *
   * @param departmentName the departmentName
   */
  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  /**
   * Returns the location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the location.
   *
   * @param location the location
   */
  public void setLocation(String location) {
    this.location = location;
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
