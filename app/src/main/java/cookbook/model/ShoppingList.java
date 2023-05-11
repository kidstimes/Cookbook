package cookbook.model;


import java.util.ArrayList;



/**
 * The ShoppingList class.
 */
public class ShoppingList {

  private int weekNumber;
  private ArrayList<Ingredient> ingredients;

  /**
   * Shopping List Constructor.
   *
   * @param weekNumber the week number of the shopping list
   * @param ingredients the ingredients of the shopping list
   */
  public ShoppingList(int weekNumber, ArrayList<Ingredient> ingredients) {
    this.weekNumber = weekNumber;
    this.ingredients = new ArrayList<>();
  }

  /**
   * Get the ingredients of the shopping list.
   *
   * @return an arraylist of ingredients
   */
  public ArrayList<Ingredient> getIngredients() {
    ArrayList<Ingredient> copyIngredients = new ArrayList<>();
    for (Ingredient ingredient : ingredients) {
      copyIngredients.add(ingredient);
    }

    return copyIngredients;
  }

  /**
   * Add an ingredient to the shopping list.
   * If the ingredient already exists add the quantities together.
   *
   * @param newIngredient the ingredient to add
   */
  public void addIngredient(Ingredient newIngredient) {
    for (Ingredient ingredient : this.ingredients) {
      if (newIngredient.getName() == ingredient.getName()) {
        ingredient.setQuantity(ingredient.getQuantity() + newIngredient.getQuantity());
        return;
      }
    }
    ingredients.add(new Ingredient(newIngredient.getName(),
        newIngredient.getQuantity(), newIngredient.getMeasurementUnit()));
  }

  /**
   * Add ingredients to the shopping list.
   *
   * @param newIngredients an ararylist of ingredients
   */
  public void addIngredients(ArrayList<Ingredient> newIngredients) {
    for (Ingredient newIngredient : newIngredients) {
      addIngredient(newIngredient);
    }
  }

  /**
   * Get the week number of the shopping list.
   *
   * @return the number of the week
   */
  public int getWeekNumber() {
    return weekNumber;
  }

}

