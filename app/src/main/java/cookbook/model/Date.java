package cookbook.model;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Date {

  private int year;
  private int month;
  private int dayOfMonth;

  /**
   * Date Constructor.
   *
   * @param year the year
   * @param month the month
   * @param dayOfMonth the day of the month
   */
  public Date(int year, int month, int dayOfMonth) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
  }

  /**
   * Get the year.
   *
   * @return the year
   */
  public int getYear() {
    return year;
  }

  /**
   * Get the number of the month.
   *
   * @return the number of the month
   */
  public int getMonth() {
    return month;
  }

  /**
   * Get the name of the month.
   *
   * @return the name of the month
   */
  public String getMonthString() {
    switch (month) {
      case 1:
        return "JANUARY";
      case 2:
        return "FEBRUARY";
      case 3:
        return "MARCH";
      case 4:
        return "APRIL";
      case 5:
        return "MAY";
      case 6:
        return "JUNE";
      case 7:
        return "JULY";
      case 8:
        return "AUGUST";
      case 9:
        return "SEPTEMBER";
      case 10:
        return "OCTOBER";
      case 11:
        return "NOVEMBER";
      case 12:
        return "DECEMBER";
      default:
        return "ERROR: Out of bounds number of month";
    }
  }

  /**
   * Get the day of the month.
   *
   * @return the day of the month
   */
  public int getDayOfMonth() {
    return dayOfMonth;
  }

  /**
   * Get the number of the week that includes the date.
   *
   * @return the week number
   */
  public int getWeekNumber() {
    return LocalDate.of(year, month, dayOfMonth).get(WeekFields.of(Locale.GERMANY).weekOfYear());
  }

  /**
   * Get the name of the day of the week.
   *
   * @return the day of the week
   */
  public String getDayOfWeek() {
    return LocalDate.of(year, month, dayOfMonth).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY);
  }

  /**
   * Get a formatted string of the date.
   *
   * @return the formatted date
   */
  public String toString() {
    return String.format("%d-%d-%d", year, month, dayOfMonth);
  }

  /**
   * Check if this date is before the given date.
   *
   * @param date the given date
   * @return true if this date is before the given date, otherwise false
   */
  private boolean isBefore(LocalDate date) {
    return date.isBefore(LocalDate.of(year, month, dayOfMonth));
  }

  /**
   * Check if this date if after the given date.
   *
   * @param date the given date
   * @return true if this date is after the given date, otherwise false
   */
  private boolean isAfter(LocalDate date) {
    return date.isAfter(LocalDate.of(year, month, dayOfMonth));
  }

  /**
   * Check if this date is between two dates, excluding the given dates from the range.
   *
   * @param startDate the start date
   * @param endDate the end date
   * @return
   */
  public boolean isBetween(LocalDate startDate, LocalDate endDate) {
    return isAfter(startDate) && isBefore(endDate);
  }

}
