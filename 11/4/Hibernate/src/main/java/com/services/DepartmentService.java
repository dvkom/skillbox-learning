package com.services;

import com.dao.DepartmentDao;
import com.objects.Department;
import com.objects.Employee;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class DepartmentService {
  private DepartmentDao departmentDao;

  public DepartmentService(DepartmentDao departmentDao) {
    this.departmentDao = departmentDao;
  }

  public Department findDepartmentById(int id) {
    Department department;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      department = departmentDao.findById(id);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return department;
  }

  public void saveDepartment(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      departmentDao.save(department);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void updateDepartment(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      departmentDao.update(department);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void deleteDepartment(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      departmentDao.delete(department);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public List<Department> getAllDepartments() {
    List<Department> departments;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      departments = departmentDao.getAll();
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return departments;
  }

  public List<Department> getAllDepartmentsWithEmployee() {
    List<Department> departments;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      departments = departmentDao.getAllWithEmployee();
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return departments;
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
