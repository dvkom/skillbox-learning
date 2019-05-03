package com.services;

import com.dao.VacationDao;
import com.objects.Vacation;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public class VacationService {
  private VacationDao vacationDAO;

  public VacationService(VacationDao vacationDAO) {
    this.vacationDAO = vacationDAO;
  }

  public Vacation findVacationById(int id) {
    Vacation vacation;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacation = vacationDAO.findById(id);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return vacation;
  }

  public void saveVacation(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacationDAO.save(vacation);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void saveVacations(Collection<Vacation> vacations) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacations.forEach(vacationDAO::save);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void updateVacation(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacationDAO.update(vacation);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public void deleteVacation(Vacation vacation) {
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacationDAO.delete(vacation);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
  }

  public List<Vacation> getAllVacations() {
    List<Vacation> vacations;
    Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      vacations = vacationDAO.getAll();
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      session.getTransaction().rollback();
      throw e;
    }
    return vacations;
  }

  public void printAllVacations() {
    List<Vacation> vacations = getAllVacations();
    System.out.println(String.format("%-2s | %-24s | %-20s | %-20s", "id", "Имя сотрудника",
        "Дата начала", "Дата окончания"));
    for (Vacation vacation : vacations) {
      System.out.println(String.format("%-2d | %-24s | %-20s | %-20s", vacation.getId(),
          vacation.getEmployee().getName(), vacation.getStartDate(), vacation.getEndDate()));
    }
  }
}
