package com.dao;

import com.objects.Employee;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
  public Employee findById(int id) {
    Employee employee = null;
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      employee = session.get(Employee.class, id);
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return employee;
  }

  public void save(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.save(employee);
      transaction.commit();
    } catch (Exception sqlException) {
      if (session.getTransaction() != null) {
        session.getTransaction().rollback();
      }
      sqlException.printStackTrace();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public void update(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.update(employee);
      transaction.commit();
    } catch (Exception sqlException) {
      if (session.getTransaction() != null) {
        session.getTransaction().rollback();
      }
      sqlException.printStackTrace();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public void delete(Employee employee) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.delete(employee);
      transaction.commit();
    } catch (Exception sqlException) {
      if (session.getTransaction() != null) {
        session.getTransaction().rollback();
      }
      sqlException.printStackTrace();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public List<Employee> getAll() {
    List<Employee> employees = new ArrayList<>();
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      employees = session.createQuery("" +
          " FROM Employee E " +
          " JOIN FETCH E.department " +
          " ORDER BY E.id", Employee.class).list();
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return employees;
  }
}
