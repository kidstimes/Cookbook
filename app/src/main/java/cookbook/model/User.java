package cookbook.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The User class.
 */
public class User {

  private String username;
  private String displayName;
  private ArrayList<Dinner> weeklyDinners;

  /**
   * User Constructor.
   *
   * @param username the unique username of the user
   * @param displayName the display name of the user
   */
  public User(int id, String username, String displayName) {
    this.username = username;
    this.displayName = displayName;
    this.weeklyDinners = new ArrayList<>();
  }

  /**
   * Modify the username of the user.
   *
   * @param username the new username
   */
  public void modifyUsername(String username) {
    this.username = username;
  }

  /**
   * Modify the display name of the user.
   *
   * @param displayName the new display name
   */
  public void modifyDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Add a recipe to a dinner on a specific date.
   *
   * @param date the date of the dinner
   * @param recipe the recipe to add to the dinner
   * @return false if recipe is alredy in the dinner on the given date, otherwise true
   */
  public boolean addWeeklyDinner(LocalDate date, Recipe recipe) {
    for (Dinner dinner : weeklyDinners) {
      if (dinner.getDate().isEqual(date)) {
        if (dinner.getRecipes().contains(recipe)) {
          return false;
        }
        dinner.addRecipe(recipe);
        return true;
      }
    }
    weeklyDinners.add(new Dinner(date, recipe));
    return true;
  }

  /**
   * Get the username of the user.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Get the display naem of the user.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Get the weekly dinners of the user.
   *
   * @return an arraylist with the dinners
   */
  public ArrayList<Dinner> getWeeklyDinners() {
    ArrayList<Dinner> copyDinners = new ArrayList<>();
    for (Dinner dinner : weeklyDinners) {
      copyDinners.add(dinner);
    }
    return copyDinners;
  }

  /**
   * Delete the user.
   */
  public void deleteUser() {
    // delete user from the db
  }

  public void setWeeklyDinners(ArrayList<Dinner> dinnerList) {
    this.weeklyDinners = dinnerList;
  }

}
