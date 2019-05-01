package com.objects;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "head_id", unique = true)
  private Integer headId;

  @Column(nullable = false)
  private String name;
  private String description;

  @OneToMany(mappedBy = "department", orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>(0);

  public Department() {
    //Used by Hibernate
  }

  public Department(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getHeadId() {
    return headId;
  }

  public void setHeadId(Integer headId) {
    this.headId = headId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(Collection<Employee> employees) {
    this.employees.addAll(employees);
  }
}
