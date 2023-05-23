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
    this.dinnerRecipes = new ArrayList<>(
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


  public int getWeekNumber() {
    return date.get(WeekFields.ISO.weekOfWeekBasedYear());
  }
}
