package com.services;

import com.dao.VacationDao;
import com.objects.Vacation;

import java.util.List;

public class VacationService {
  private VacationDao vacationDAO = new VacationDao();

  public Vacation findVacationById(int id) {
    return vacationDAO.findById(id);
  }

  public void saveVacation(Vacation vacation) {
    vacationDAO.save(vacation);
  }

  public void updateVacation(Vacation vacation) {
    vacationDAO.update(vacation);
  }

  public void deleteVacation(Vacation vacation) {
    vacationDAO.delete(vacation);
  }

  public List<Vacation> getAllVacations() {
    return vacationDAO.getAll();
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
