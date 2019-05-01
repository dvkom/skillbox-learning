package com.services;

import com.dao.EmployeeDao;
import com.objects.Employee;

import java.util.List;

public class EmployeeService {
  private EmployeeDao employeeDao = new EmployeeDao();

  public Employee findEmployeeById(int id) {
    return employeeDao.findById(id);
  }

  public void saveEmployee(Employee employee) {
    employeeDao.save(employee);
  }

  public void updateEmployee(Employee employee) {
    employeeDao.update(employee);
  }

  public void deleteEmployee(Employee employee) {
    employeeDao.delete(employee);
  }

  public List<Employee> getAllEmployees() {
    return employeeDao.getAll();
  }

  public void printAllEmployees() {
    List<Employee> employees = getAllEmployees();
    System.out.println(String.format("%-2s | %-24s | %-20s", "id", "Имя", "Отдел"));
    for (Employee employee : employees) {
      System.out.println(String.format("%-2d | %-24s | %-20s", employee.getId(), employee.getName(),
          employee.getDepartment().getName()));
    }
  }
}
