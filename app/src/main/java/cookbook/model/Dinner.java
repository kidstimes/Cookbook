package cookbook.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Dinner {
  private LocalDate date;
  private ArrayList<Recipe> dinnerRecipes;

  public Dinner(LocalDate date) {
    this.date = date;
    this.dinnerRecipes = new ArrayList<Recipe>();
  }

  public void addRecipe(Recipe recipe) {
    dinnerRecipes.add(recipe);
  }

  public ArrayList<Recipe> getRecipes() {
    return dinnerRecipes;
  }

  public Object getDate() {
    return date;
  }

  //Setters
  public void setDate(LocalDate date) {
    this.date = date;
  }


  
}
