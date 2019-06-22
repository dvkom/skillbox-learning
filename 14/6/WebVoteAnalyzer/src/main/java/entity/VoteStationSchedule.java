package entity;

import java.util.*;
import java.util.stream.Collectors;

public class VoteStationSchedule {
  private Set<String> workDays = new TreeSet<>();
  private List<List<String>> schedule = new ArrayList<>();

  public void buildSchedule(HashMap<Integer, WorkTime> voteStationWorkTimes) {
    voteStationWorkTimes.forEach((key, value) -> workDays
        .addAll(
            value.getPeriods()
                .stream()
                .map(TimePeriod::getDate)
                .collect(Collectors.toSet())
        )
    );

    Map<Integer, Map<String, String>> workSchedule = voteStationWorkTimes.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue().getPeriods().stream()
                .collect(Collectors.toMap(
                    TimePeriod::getDate,
                    TimePeriod::getTimePeriod
                    )
                )
            )
        );

    for (Map.Entry<Integer, Map<String, String>> stationWorkTimes : workSchedule.entrySet()) {
      List<String> workTimes = new ArrayList<>();
      workTimes.add(stationWorkTimes.getKey().toString());
      for (String day : workDays) {
        workTimes.add(stationWorkTimes.getValue().getOrDefault(day, "-"));
      }
      schedule.add(workTimes);
    }
  }

  public List<List<String>> getSchedule() {
    return schedule;
  }

  public Set<String> getWorkDays() {
    return workDays;
  }
}
