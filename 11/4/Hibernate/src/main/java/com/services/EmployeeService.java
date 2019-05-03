package com.services;

import com.dao.EmployeeDao;
import com.objects.Employee;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class EmployeeService {
  private EmployeeDao employeeDao;

  public EmployeeService(EmployeeDao employeeDao) {
    this.employeeDao = employeeDao;
  }

  public Employee findEmployeeById(int id) {
    Employee employee;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      employee = employeeDao.findById(id);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return employee;
  }

  public void saveEmployee(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      employeeDao.save(employee);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void updateEmployee(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      employeeDao.update(employee);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void deleteEmployee(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      employeeDao.delete(employee);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public List<Employee> getAllEmployees() {
    List<Employee> employees;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      employees = employeeDao.getAll();
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return employees;
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
