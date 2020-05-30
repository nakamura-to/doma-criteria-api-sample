package sample.entity;

/** */
public abstract class AbstractEmployee {
  private Department department;
  private Address address;

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
