package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditRequest {

  private boolean isSalaryProject;

  private boolean isTrustedClient;

  private boolean isRetiree;

  private int creditSum;

  private int creditTerm;

  private String lastName;

  private String firstName;

  private String middleName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate birthday;

  private String email;

  private String phone;

  private String gender;

  private String passportSeries;

  private String passportNumber;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate passportDateOfIssue;

  private String passportCode;

  private String city;

  private String street;

  private String address;

  private String flat;

  private boolean isPrivacyAccepted;

  public boolean getIsSalaryProject() {
    return isSalaryProject;
  }

  public void setIsSalaryProject(boolean salaryProject) {
    isSalaryProject = salaryProject;
  }

  public boolean getIsTrustedClient() {
    return isTrustedClient;
  }

  public void setIsTrustedClient(boolean trustedClient) {
    isTrustedClient = trustedClient;
  }

  public boolean getIsRetiree() {
    return isRetiree;
  }

  public void setIsRetiree(boolean retiree) {
    isRetiree = retiree;
  }

  public int getCreditSum() {
    return creditSum;
  }

  public void setCreditSum(int creditSum) {
    this.creditSum = creditSum;
  }

  public int getCreditTerm() {
    return creditTerm;
  }

  public void setCreditTerm(int creditTerm) {
    this.creditTerm = creditTerm;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPassportSeries() {
    return passportSeries;
  }

  public void setPassportSeries(String passportSeries) {
    this.passportSeries = passportSeries;
  }

  public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public LocalDate getPassportDateOfIssue() {
    return passportDateOfIssue;
  }

  public void setPassportDateOfIssue(LocalDate passportDateOfIssue) {
    this.passportDateOfIssue = passportDateOfIssue;
  }

  public String getPassportCode() {
    return passportCode;
  }

  public void setPassportCode(String passportCode) {
    this.passportCode = passportCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getFlat() {
    return flat;
  }

  public void setFlat(String flat) {
    this.flat = flat;
  }

  public boolean getIsPrivacyAccepted() {
    return isPrivacyAccepted;
  }

  public void setIsPrivacyAccepted(boolean isPrivacyAccepted) {
    this.isPrivacyAccepted = isPrivacyAccepted;
  }

  @Override
  public String toString() {
    return "CreditRequest{" +
        "isSalaryProject=" + isSalaryProject +
        ", isTrustedClient=" + isTrustedClient +
        ", isRetiree=" + isRetiree +
        ", creditSum=" + creditSum +
        ", creditTerm=" + creditTerm +
        ", lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", middleName='" + middleName + '\'' +
        ", birthday=" + birthday +
        ", email='" + email + '\'' +
        ", phone='" + phone + '\'' +
        ", gender='" + gender + '\'' +
        ", passportSeries='" + passportSeries + '\'' +
        ", passportNumber='" + passportNumber + '\'' +
        ", passportDateOfIssue='" + passportDateOfIssue + '\'' +
        ", passportCode='" + passportCode + '\'' +
        ", city='" + city + '\'' +
        ", street='" + street + '\'' +
        ", address='" + address + '\'' +
        ", flat='" + flat + '\'' +
        ", isPrivacyAccepted=" + isPrivacyAccepted +
        '}';
  }
}
