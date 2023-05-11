package cookbook.model;


import java.util.ArrayList;



/**
 * The ShoppingList class.
 */
public class ShoppingList {
  private int weekNumber;
  private ArrayList<Ingredient> ingredients;

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

  //getter
  public int getWeekNumber() {
    return weekNumber;
  }

}

