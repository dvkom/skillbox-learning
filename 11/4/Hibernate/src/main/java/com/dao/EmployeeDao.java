package com.dao;

import com.objects.Employee;
import com.utils.HibernateSessionFactory;
import java.util.List;

public class EmployeeDao {
  public Employee findById(int id) {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .get(Employee.class, id);
  }

  public void save(Employee employee) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().save(employee);
  }

  public void update(Employee employee) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().update(employee);
  }

  public void delete(Employee employee) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().delete(employee);
  }

  public List<Employee> getAll() {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .createQuery("" +
        " FROM Employee E " +
        " JOIN FETCH E.department " +
        " ORDER BY E.id", Employee.class)
        .list();
  }
}
