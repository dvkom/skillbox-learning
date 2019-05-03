package com.dao;

import com.objects.Department;
import com.utils.HibernateSessionFactory;
import java.util.List;

public class DepartmentDao {

  public Department findById(int id) {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .get(Department.class, id);
  }

  public void save(Department department) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().save(department);
  }

  public void update(Department department) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().update(department);
  }

  public void delete(Department department) {
    HibernateSessionFactory.getSessionFactory().getCurrentSession().delete(department);
  }

  public List<Department> getAll() {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .createQuery("" +
        " FROM Department", Department.class)
        .list();
  }

  public List<Department> getAllWithEmployee() {
    return HibernateSessionFactory.getSessionFactory().getCurrentSession()
        .createQuery("" +
        " SELECT DISTINCT D " +
        " FROM Department D " +
        " JOIN FETCH D.employees ", Department.class)
        .list();
  }
}
