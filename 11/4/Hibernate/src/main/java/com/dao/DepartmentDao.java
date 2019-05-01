package com.dao;

import com.objects.Department;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

  public Department findById(int id) {
    Department department = null;
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      department = session.get(Department.class, id);
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return department;
  }

  public void save(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.save(department);
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

  public void update(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.update(department);
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

  public void delete(Department department) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.delete(department);
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

  public List<Department> getAll() {
    List<Department> departments = new ArrayList<>();
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      departments = session.createQuery(" FROM Department", Department.class).list();
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return departments;
  }

  public List<Department> getAllWithEmployee() {
    List<Department> departments = new ArrayList<>();
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      departments = session.createQuery("" +
          " SELECT DISTINCT D " +
          " FROM Department D " +
          " JOIN FETCH D.employees ", Department.class)
          .list();
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return departments;
  }
}
