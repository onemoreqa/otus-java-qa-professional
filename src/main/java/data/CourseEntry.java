package data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseEntry {
  String title;
  CourseSourceData courseTypeData;
  LocalDate beginDate;
  String description;
}
