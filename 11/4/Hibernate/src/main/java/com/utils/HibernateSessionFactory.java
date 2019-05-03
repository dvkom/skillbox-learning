package com.utils;

import com.objects.Department;
import com.objects.Employee;
import com.objects.Vacation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateSessionFactory {
  private static SessionFactory sessionFactory;

  private HibernateSessionFactory() {
  }

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      // Создаем инстанс конфигурации и загружаем конфиг из файла
      Configuration config = new Configuration();
      config.configure("hibernate.cfg.xml");
      // Добавляем Entity классы
      config.addAnnotatedClass(Department.class);
      config.addAnnotatedClass(Employee.class);
      config.addAnnotatedClass(Vacation.class);
      // Регистрируем сервис
      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
          .applySettings(config.getProperties())
          .build();
      sessionFactory = config.buildSessionFactory(serviceRegistry);
    }
    return sessionFactory;
  }
}
