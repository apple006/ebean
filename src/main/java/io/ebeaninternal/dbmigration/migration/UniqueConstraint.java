package io.ebeaninternal.dbmigration.migration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="columnNames" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "uniqueConstraint")
public class UniqueConstraint {

  @XmlAttribute(name = "name", required = true)
  protected String name;

  @XmlAttribute(name = "columnNames", required = true)
  protected String columnNames;

  @XmlAttribute(name = "oneToOne", required = false)
  protected Boolean oneToOne;

  @XmlAttribute(name = "nullableColumns", required = true)
  protected String nullableColumns;
  
  /**
   * Gets the value of the name property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the value of the columnNames property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getColumnNames() {
    return columnNames;
  }

  /**
   * Sets the value of the columnNames property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setColumnNames(String value) {
    this.columnNames = value;
  }
  
  /**
   * Gets the value of the oneToOne property.
   *
   * @return true if oneToOne was set
   */
  public boolean isOneToOne() {
    return Boolean.TRUE.equals(oneToOne);
  }
  
  /**
   * Sets the value of the oneToOne property.
   *
   * @param value boolean
   */
  public void setOneToOne(boolean oneToOne) {
    this.oneToOne = oneToOne;
  }

  /**
   * Gets the value of the nullableColumns property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getNullableColumns() {
    return nullableColumns;
  }

  /**
   * Sets the value of the nullableColumns property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setNullableColumns(String value) {
    this.nullableColumns = value;
  }

}
