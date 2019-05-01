package com.dao;

import com.objects.Vacation;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class VacationDao {

  public Vacation findById(int id) {
    Vacation vacation = null;
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      vacation = session.get(Vacation.class, id);
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return vacation;
  }

  public List<Vacation> findByEmployeeId(int id) {
    List<Vacation> vacations = new ArrayList<>();
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      vacations = session.createQuery("" +
          " FROM Vacation V " +
          " WHERE V.employee_id = :id", Vacation.class)
          .setParameter("id", id)
          .list();
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return vacations;
  }

  public void save(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.save(vacation);
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

  public void update(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.update(vacation);
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

  public void delete(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try {
      Transaction transaction = session.beginTransaction();
      session.delete(vacation);
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

  public List<Vacation> getAll() {
    List<Vacation> vacations = new ArrayList<>();
    try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
      session.beginTransaction();
      vacations = session.createQuery("" +
          " FROM Vacation V " +
          " JOIN FETCH V.employee E" +
          " JOIN FETCH E.department " +
          " ORDER BY V.id", Vacation.class).list();
    } catch (Exception sqlException) {
      sqlException.printStackTrace();
    }
    return vacations;
  }
}
