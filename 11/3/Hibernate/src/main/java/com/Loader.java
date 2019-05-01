package com;

import com.objects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class Loader {

  private static SessionFactory sessionFactory;
  private static Session session;

  public static void main(String[] args) {
    setUp();
    session = sessionFactory.openSession();
    session.beginTransaction();

    printAllHeads();
    printWrongHeads();
    printLowSalaryHeads();
    printBefore2010Heads();

    session.getTransaction().commit();
    session.close();

    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  private static void setUp() {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .configure("hibernate.cfg.xml")
        .build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      e.printStackTrace();
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }

  private static void printAllHeads() {
    // Общий список отделов с руководителями через связь one-to-one
    System.out.println("Общий список отделов с руководителями");
    List<Department> departments = session.createQuery("" +
        " FROM Department D " +
        " JOIN FETCH D.head", Department.class)
        .list();
    for (Department department : departments) {
      System.out.println(String.format("%-20s | %-2d | %-24s | %-2d", department.getName(),
          department.getHeadId(), department.getHead().getName(), department.getHead().getId()));
    }
  }

  private static void printWrongHeads() {
    // Список ошибочно привязанных сотрудников, которые работают в одних отделах,
    // а руководят другими
    System.out.println("-".repeat(120));
    System.out.println("Список ошибочно привязанных сотрудников, которые работают в" +
        " одних отделах, а руководят другими");
    List<Department> departments = session.createQuery("" +
        " FROM Department D " +
        " JOIN FETCH D.head H " +
        " WHERE D.id != H.departmentId", Department.class)
        .list();
    System.out.println(String.format("%-9s | %-20s | %-7s | %-24s | %-24s", "id отдела",
        "Имя отдела", "head_id", "Руководитель", "id отдела у руководителя"));
    for (Department department : departments) {
      System.out.println(String.format("%-9d | %-20s | %-7d | %-24s | %-24d", department.getId(),
          department.getName(), department.getHeadId(), department.getHead().getName(),
          department.getHead().getDepartmentId()));
    }
  }

  private static void printLowSalaryHeads() {
    // Список руководителей отделов, зарплата которых составляет менее 115 000 рублей в месяц
    System.out.println("-".repeat(120));
    System.out.println("Список руководителей отделов, зарплата которых составляет" +
        " менее 115 000 рублей в месяц");
    List<Department> departments = session.createQuery("" +
        " FROM Department D " +
        " JOIN FETCH D.head H " +
        " WHERE H.salary < 115000", Department.class)
        .list();
    System.out.println(String.format("%-24s | %-10s", "Руководитель", "Зарплата"));
    for (Department department : departments) {
      System.out.println(String.format("%-24s | %-10d", department.getHead().getName(),
          department.getHead().getSalary()));
    }
  }

  private static void printBefore2010Heads() {
    // Список руководителей отделов, которые вышли на работу до марта 2010
    System.out.println("-".repeat(120));
    System.out.println("Список руководителей отделов, которые вышли на работу до марта 2010");
    List<Department> departments = session.createQuery("" +
        " FROM Department D " +
        " JOIN FETCH D.head H " +
        " WHERE H.hireDate < '2010-03-01'", Department.class)
        .list();
    System.out.println(String.format("%-24s | %-10s", "Руководитель", "Дата найма"));
    for (Department department : departments) {
      System.out.println(String.format("%-24s | %-10s", department.getHead().getName(),
          department.getHead().getHireDate()));
    }
  }
}
