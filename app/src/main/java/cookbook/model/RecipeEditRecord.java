package cookbook.model;

import java.time.LocalDate;

/**
 * The recipeEditRecord class.
 */
public class RecipeEditRecord {
  private String userName;
  private LocalDate date;
  
  /**
   * recipeEditRecord Constructor.
   *
   * @param userName the username of the user who edited the recipe
   * @param date the date the recipe was edited
   */
  public RecipeEditRecord(String userName, LocalDate date) {
    this.userName = userName;
    this.date = date;
  }

  public String getUserName() {
    return userName;
  }


  public LocalDate getDate() {
    return date;
  }

  
}
