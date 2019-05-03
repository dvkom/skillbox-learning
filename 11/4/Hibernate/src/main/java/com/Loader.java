package com;

import com.dao.DepartmentDao;
import com.dao.EmployeeDao;
import com.dao.VacationDao;
import com.objects.*;
import com.services.DepartmentService;
import com.services.EmployeeService;
import com.services.VacationService;
import com.utils.HibernateSessionFactory;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Loader {

  public static void main(String[] args) {
//    departmentCRUDDemonstration();
//    employeeCRUDDemonstration();

    fillVacationTable();
    printAllVacations();
    findAndPrintVacationProblems();
  }

  private static void fillVacationTable() {
    VacationService vacationService = new VacationService(new VacationDao());
    EmployeeService employeeService = new EmployeeService(new EmployeeDao());
    Map<Integer, Employee> employees = employeeService.getAllEmployees()
        .stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
    ArrayList<Vacation> vacations = new ArrayList<>();
    // Если таблица пуста, заполняем ее
    if (isVacationTableEmpty()) {
      // Отдел продаж, есть пересечения
      vacations.add(new Vacation(employees.get(2),
          LocalDate.of(2019, 1, 12),
          LocalDate.of(2019, 1, 30)));
      vacations.add(new Vacation(employees.get(2),
          LocalDate.of(2019, 3, 1),
          LocalDate.of(2019, 3, 14)));
      vacations.add(new Vacation(employees.get(4),
          LocalDate.of(2019, 1, 20),
          LocalDate.of(2019, 2, 20)));
      vacations.add(new Vacation(employees.get(5),
          LocalDate.of(2019, 3, 10),
          LocalDate.of(2019, 3, 25)));
      vacations.add(new Vacation(employees.get(5),
          LocalDate.of(2019, 6, 20),
          LocalDate.of(2019, 7, 5)));
      vacations.add(new Vacation(employees.get(15),
          LocalDate.of(2019, 8, 1),
          LocalDate.of(2019, 8, 30)));

      // Отдел маркетинга, без пересечений
      vacations.add(new Vacation(employees.get(1),
          LocalDate.of(2019, 2, 1),
          LocalDate.of(2019, 2, 14)));
      vacations.add(new Vacation(employees.get(1),
          LocalDate.of(2019, 8, 1),
          LocalDate.of(2019, 8, 14)));
      vacations.add(new Vacation(employees.get(3),
          LocalDate.of(2019, 2, 15),
          LocalDate.of(2019, 2, 28)));
      vacations.add(new Vacation(employees.get(3),
          LocalDate.of(2019, 4, 1),
          LocalDate.of(2019, 4, 16)));
      vacations.add(new Vacation(employees.get(11),
          LocalDate.of(2019, 5, 1),
          LocalDate.of(2019, 5, 30)));

      // Отдел разработки, без пересечений
      vacations.add(new Vacation(employees.get(8),
          LocalDate.of(2019, 2, 1),
          LocalDate.of(2019, 2, 14)));
      vacations.add(new Vacation(employees.get(8),
          LocalDate.of(2019, 8, 1),
          LocalDate.of(2019, 8, 14)));
      vacations.add(new Vacation(employees.get(9),
          LocalDate.of(2019, 2, 15),
          LocalDate.of(2019, 2, 28)));
      vacations.add(new Vacation(employees.get(9),
          LocalDate.of(2019, 4, 1),
          LocalDate.of(2019, 4, 16)));

      // Отдел кадров, один сотрудник
      vacations.add(new Vacation(employees.get(7),
          LocalDate.of(2019, 5, 1),
          LocalDate.of(2019, 2, 14)));
      vacations.add(new Vacation(employees.get(7),
          LocalDate.of(2019, 9, 1),
          LocalDate.of(2019, 8, 14)));

      // Бухгалтерия, есть пересечения
      vacations.add(new Vacation(employees.get(6),
          LocalDate.of(2019, 3, 1),
          LocalDate.of(2019, 3, 20)));
      vacations.add(new Vacation(employees.get(6),
          LocalDate.of(2019, 5, 1),
          LocalDate.of(2019, 5, 10)));
      vacations.add(new Vacation(employees.get(13),
          LocalDate.of(2019, 10, 1),
          LocalDate.of(2019, 10, 15)));
      vacations.add(new Vacation(employees.get(13),
          LocalDate.of(2019, 12, 15),
          LocalDate.of(2019, 12, 30)));
      vacations.add(new Vacation(employees.get(14),
          LocalDate.of(2019, 3, 15),
          LocalDate.of(2019, 3, 25)));
      vacations.add(new Vacation(employees.get(14),
          LocalDate.of(2019, 7, 1),
          LocalDate.of(2019, 7, 14)));

      // IT Отдел, есть пересечения
      vacations.add(new Vacation(employees.get(10),
          LocalDate.of(2019, 2, 1),
          LocalDate.of(2019, 2, 14)));
      vacations.add(new Vacation(employees.get(10),
          LocalDate.of(2019, 8, 1),
          LocalDate.of(2019, 8, 14)));
      vacations.add(new Vacation(employees.get(12),
          LocalDate.of(2019, 4, 1),
          LocalDate.of(2019, 4, 12)));
      vacations.add(new Vacation(employees.get(12),
          LocalDate.of(2019, 6, 15),
          LocalDate.of(2019, 6, 28)));
      vacations.add(new Vacation(employees.get(12),
          LocalDate.of(2019, 8, 5),
          LocalDate.of(2019, 8, 10)));

      vacationService.saveVacations(vacations);
    }
  }

  private static void findAndPrintVacationProblems() {
    VacationService vacationService = new VacationService(new VacationDao());
    List<Vacation> vacations = vacationService.getAllVacations();
    Map<Department, Set<String>> intersections = new HashMap<>();

    // Предполагам, что у сотрудника нет пересечения отпусков с самим собой
    for (int i = 0; i < vacations.size(); i++) {
      for (int j = i + 1; j < vacations.size(); j++) {
        Employee first = vacations.get(i).getEmployee();
        Employee second = vacations.get(j).getEmployee();
        // Определяем принадлежность к одному отделу
        if (first.getDepartment().getId().equals(second.getDepartment().getId())) {
          LocalDate firstStart = vacations.get(i).getStartDate();
          LocalDate firstEnd = vacations.get(i).getEndDate();
          LocalDate secondStart = vacations.get(j).getStartDate();
          LocalDate secondEnd = vacations.get(j).getEndDate();
          // Определяем факт пересечения
          if (firstStart.isBefore(secondEnd) && firstEnd.isAfter(secondStart)) {
            // Ищем даты пересечения
            List<LocalDate> firstRange = firstStart.datesUntil(firstEnd)
                .collect(Collectors.toList());
            firstRange.add(firstEnd);
            List<LocalDate> secondRange = secondStart.datesUntil(secondEnd)
                .collect(Collectors.toList());
            secondRange.add(secondEnd);
            firstRange.retainAll(secondRange);
            // Сохраняем диапазон
            String intersection = String.format("С %s по %s: %s и %s", firstRange.get(0),
                firstRange.get(firstRange.size() - 1), first.getName(), second.getName());
            if (intersections.get(first.getDepartment()) == null) {
              Set<String> vacationSet = new HashSet<>();
              vacationSet.add(intersection);
              intersections.put(first.getDepartment(), vacationSet);
            } else {
              intersections.get(first.getDepartment()).add(intersection);
            }
          }
        }
      }
    }
    //Выводим результаты
    if (intersections.size() != 0) {
      System.out.println("Найдены следующие пересечения отпусков в отделах:");
      for (Map.Entry<Department, Set<String>> entry : intersections.entrySet()) {
        System.out.println(entry.getKey().getName());
        for (String s : entry.getValue()) {
          System.out.println(s);
        }
      }
    }
  }

  private static void printAllVacations() {
    VacationService vacationService = new VacationService(new VacationDao());
    vacationService.printAllVacations();
  }

  private static boolean isVacationTableEmpty() {
    Session session = HibernateSessionFactory.getSessionFactory().openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Vacation> criteria = builder.createQuery(Vacation.class);
    criteria.from(Vacation.class);
    return session.createQuery(criteria).getResultList().size() == 0;
  }


  private static void departmentCRUDDemonstration() {
    DepartmentService departmentService = new DepartmentService(new DepartmentDao());
    departmentService.printAllDepartmentsWithEmployee();
//    departmentService.printAllDepartments();

    Department newDepartment = new Department("Отдел сына директора");
    newDepartment.setHeadId(4);
    newDepartment.setDescription("Сын хотел быть начальником, пришлось создать отдел");
    departmentService.saveDepartment(newDepartment);
    departmentService.printAllDepartments();

    Department updDepartment = departmentService.findDepartmentById(5);
    if (updDepartment != null) {
      updDepartment.setDescription("Новое описание отдела");
      departmentService.updateDepartment(updDepartment);
      departmentService.printAllDepartments();
    }
/*
    Department delDepartment = departmentService.findDepartmentById(5);
    if (delDepartment != null) {
      departmentService.deleteDepartment(delDepartment);
      departmentService.printAllDepartments();
    }
*/
  }

  private static void employeeCRUDDemonstration() {
    EmployeeService employeeService = new EmployeeService(new EmployeeDao());
    DepartmentService departmentService = new DepartmentService(new DepartmentDao());
    employeeService.printAllEmployees();

    Employee newEmployee = new Employee();
    newEmployee.setName("Иванов Иван");
    newEmployee.setDepartment(departmentService.findDepartmentById(1));
    newEmployee.setSalary(120000);
    employeeService.saveEmployee(newEmployee);
    employeeService.printAllEmployees();

    Employee updEmployee = employeeService.findEmployeeById(10);
    if (updEmployee != null) {
      updEmployee.setName("Иванов Петр");
      employeeService.updateEmployee(updEmployee);
      employeeService.printAllEmployees();
    }
/*
    Employee delEmployee = employeeService.findEmployeeById(4);
    if (delEmployee != null) {
      employeeService.deleteEmployee(delEmployee);
      employeeService.printAllEmployees();
    }
*/
  }

}
