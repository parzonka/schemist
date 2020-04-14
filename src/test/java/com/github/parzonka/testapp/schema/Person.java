package com.github.parzonka.testapp.schema;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.*;

/**
 * Person
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"firstName", "lastName", "age"})
public class Person {

  /**
   * textShort
   * <p>
   * A short text (Can be null)
   *
   */
  @Nullable
  @JsonProperty("firstName")
  @JsonPropertyDescription("A short text")
  private String firstName;
  /**
   * The person's last name. (Can be null)
   *
   */
  @Nullable
  @JsonProperty("lastName")
  @JsonPropertyDescription("The person's last name.")
  private String lastName;
  /**
   * Age in years which must be equal to or greater than zero. (Can be null)
   *
   */
  @Nullable
  @JsonProperty("age")
  @JsonPropertyDescription("Age in years which must be equal to or greater than zero.")
  private Integer age;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * No args constructor for use in serialization
   *
   */
  public Person() {}

  /**
   *
   * @param firstName
   * @param lastName
   * @param age
   */
  public Person(String firstName, String lastName, Integer age) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  /**
   * textShort
   * <p>
   * A short text
   *
   */
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  /**
   * textShort
   * <p>
   * A short text
   *
   */
  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Person withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * The person's last name.
   *
   */
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  /**
   * The person's last name.
   *
   */
  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Person withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Age in years which must be equal to or greater than zero.
   *
   */
  @JsonProperty("age")
  public Integer getAge() {
    return age;
  }

  /**
   * Age in years which must be equal to or greater than zero.
   *
   */
  @JsonProperty("age")
  public void setAge(Integer age) {
    this.age = age;
  }

  public Person withAge(Integer age) {
    this.age = age;
    return this;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  public Person withAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Person.class.getName())
        .append('@')
        .append(Integer.toHexString(System.identityHashCode(this)))
        .append('[');
    sb.append("firstName");
    sb.append('=');
    sb.append(((this.firstName == null) ? "<null>" : this.firstName));
    sb.append(',');
    sb.append("lastName");
    sb.append('=');
    sb.append(((this.lastName == null) ? "<null>" : this.lastName));
    sb.append(',');
    sb.append("age");
    sb.append('=');
    sb.append(((this.age == null) ? "<null>" : this.age));
    sb.append(',');
    sb.append("additionalProperties");
    sb.append('=');
    sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = ((result * 31) + ((this.firstName == null) ? 0 : this.firstName.hashCode()));
    result = ((result * 31) + ((this.lastName == null) ? 0 : this.lastName.hashCode()));
    result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
    result = ((result * 31) + ((this.age == null) ? 0 : this.age.hashCode()));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Person) == false) {
      return false;
    }
    Person rhs = ((Person) other);
    return (((((this.firstName == rhs.firstName) || ((this.firstName != null) && this.firstName.equals(rhs.firstName)))
        && ((this.lastName == rhs.lastName) || ((this.lastName != null) && this.lastName.equals(rhs.lastName))))
        && ((this.additionalProperties == rhs.additionalProperties)
            || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties))))
        && ((this.age == rhs.age) || ((this.age != null) && this.age.equals(rhs.age))));
  }

}
