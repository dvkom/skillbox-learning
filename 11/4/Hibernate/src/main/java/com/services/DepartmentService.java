package com.services;

import com.dao.DepartmentDao;
import com.objects.Department;
import com.objects.Employee;

import java.util.List;

public class DepartmentService {
  private DepartmentDao departmentDao = new DepartmentDao();

  public Department findDepartmentById(int id) {
    return departmentDao.findById(id);
  }

  public void saveDepartment(Department department) {
    departmentDao.save(department);
  }

  public void updateDepartment(Department department) {
    departmentDao.update(department);
  }

  public void deleteDepartment(Department department) {
    departmentDao.delete(department);
  }

  public List<Department> getAllDepartments() {
    return departmentDao.getAll();
  }

  public List<Department> getAllDepartmentsWithEmployee() {
    return departmentDao.getAllWithEmployee();
  }

  public void printAllDepartments() {
    for (Department department : getAllDepartments()) {
      System.out.println(String.format("%-20s | %s", department.getName(),
          department.getDescription()));
    }
  }

  public void printAllDepartmentsWithEmployee() {
    for (Department department : getAllDepartmentsWithEmployee()) {
      System.out.println(department.getName());
      for (Employee employee : department.getEmployees()) {
        System.out.println("    " + employee.getName());
      }
    }
  }
}
