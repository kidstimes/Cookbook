package cookbook.model;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Dinner class.
 */
public class Dinner {

  private LocalDate date;
  private ArrayList<Recipe> dinnerRecipes;

  /**
   * Dinner Constructor.
   *
   * @param date the date of the dinner
   * @param recipe the recipe to add to the dinner
   */
  public Dinner(LocalDate date, Recipe recipe) {
    this.date = date;
    this.dinnerRecipes = new ArrayList<Recipe>(
      Arrays.asList(recipe)
    );
  }

  /**
   * Add a recipe to the dinner.
   *
   * @param recipe the recipe to add
   */
  public void addRecipe(Recipe recipe) {
    dinnerRecipes.add(recipe);
  }


  /**
   * Get the dinner recipes.
   *
   * @return an arraylist with the recipes
   */
  public ArrayList<Recipe> getRecipes() {
    return dinnerRecipes;
  }

  /**
   * Get the date of the dinner.
   *
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }
  
  /**
   * Remove a recipe from the dinner.
   *
   * @param recipe the recipe to remove
   */
  public void removeRecipe(Recipe recipe) {
    dinnerRecipes.remove(recipe);
  }

  public int getWeekNumber() {
    int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
    return weekNumber;
  }
}
