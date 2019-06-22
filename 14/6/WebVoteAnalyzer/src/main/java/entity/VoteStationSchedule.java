package entity;

import java.util.*;
import java.util.stream.Collectors;

public class VoteStationSchedule {
  private Set<String> workDays = new TreeSet<>();
  private List<List<String>> schedule = new ArrayList<>();

  private VoteStationSchedule() {
  }

  public static VoteStationSchedule buildSchedule(HashMap<Integer, WorkTime> voteStationWorkTimes) {
    VoteStationSchedule voteStationSchedule = new VoteStationSchedule();
    voteStationSchedule.workDays = voteStationWorkTimes.values()
        .stream()
        .flatMap(v -> v.getPeriods().stream())
        .map(TimePeriod::getDate)
        .collect(Collectors.toSet());

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
      for (String day : voteStationSchedule.workDays) {
        workTimes.add(stationWorkTimes.getValue().getOrDefault(day, "-"));
      }
      voteStationSchedule.schedule.add(workTimes);
    }
    return voteStationSchedule;
  }

  public static VoteStationSchedule emptySchedule() {
    return new VoteStationSchedule();
  }

  public List<List<String>> getSchedule() {
    return schedule;
  }

  public Set<String> getWorkDays() {
    return workDays;
  }
}
