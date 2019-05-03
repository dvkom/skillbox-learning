package com.dao;

import com.objects.Vacation;
import com.utils.HibernateSessionFactory;
import java.util.List;

public class VacationDao {

  public Vacation findById(int id) {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .get(Vacation.class, id);
  }

  public List<Vacation> findByEmployeeId(int id) {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .createQuery("" +
        " FROM Vacation V " +
        " WHERE V.employee_id = :id", Vacation.class)
        .setParameter("id", id)
        .list();
  }

  public void save(Vacation vacation) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().save(vacation);
  }

  public void update(Vacation vacation) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().update(vacation);
  }

  public void delete(Vacation vacation) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().delete(vacation);
  }

  public List<Vacation> getAll() {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .createQuery("" +
        " FROM Vacation V " +
        " JOIN FETCH V.employee E" +
        " JOIN FETCH E.department " +
        " ORDER BY V.id", Vacation.class).list();
  }
}
