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
@Entity(listener = AddressListener.class, metamodel = @Metamodel)
@Table(name = "address")
public class Address extends AbstractAddress {

  /** */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "address_address_id")
  @Column(name = "address_id")
  Integer addressId;

  /** */
  @Column(name = "street")
  String street;

  /** */
  @Version
  @Column(name = "version")
  Integer version;

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
   * Returns the street.
   *
   * @return the street
   */
  public String getStreet() {
    return street;
  }

  /**
   * Sets the street.
   *
   * @param street the street
   */
  public void setStreet(String street) {
    this.street = street;
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
