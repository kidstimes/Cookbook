package cookbook.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The User class.
 */
public class User {

  private String username;
  private String displayName;
  private ArrayList<Dinner> weeklyDinners;
  private ArrayList<Recipe> favorites;
  private ArrayList<ShoppingList> shoppingLists;
  private int id;

  /**
   * User Constructor.
   *
   * @param username the unique username of the user
   * @param displayName the display name of the user
   */
<<<<<<< HEAD
  public User(String username, String displayName) {
=======
  public User(int id, String username, String displayName) {
    this.id = id;
>>>>>>> dceebf9 (add refresh shopping list and edit recipe)
    this.username = username;
    this.displayName = displayName;
    this.weeklyDinners = new ArrayList<>();
    this.favorites = new ArrayList<>(); 
    this.shoppingLists = new ArrayList<>();
  }

  public User(String username, String displayName) {
    this.username = username;
    this.displayName = displayName;
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
        //check it the recipe is already in the dinner by check its name
        for (Recipe recip : dinner.getRecipes()) {
          if (recip.getName().equalsIgnoreCase(recipe.getName())) {
            return false;
          }
        }
        dinner.addRecipe(recipe);
        return true;
      }
    }
    weeklyDinners.add(new Dinner(date, recipe));
    return true;
  }

  /**
   * Remove a recipe from a dinner on a specific date.
   *
   * @param date the date of the dinner
   * @param recipe the recipe to remove from the dinner
   * @return false if the recipe is not in the dinner on the given date, otherwise true
   */
  public boolean removeRecipeFromWeeklyDinner(LocalDate date, Recipe recipe) {
    for (Dinner dinner : weeklyDinners) {
      if (dinner.getDate().isEqual(date)) {
        dinner.getRecipes().remove(recipe);
        return true;
      }
    }
    return false;
  }

  /**
   * Get user id
   * @return user id
   */
  public int getId() {
    return id;
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
  
  /**Set the weekly dinners of the user.
   *
   * @param dinnerList the weekly dinner list of the user 
   */
  public void setWeeklyDinners(ArrayList<Dinner> dinnerList) {
    this.weeklyDinners = dinnerList;
  }

  /**
   * Check if a user has a weekly dinner on current week.
   *
   * @return true if the user has a weekly dinner on current week, otherwise false
   */
  public boolean checkWeeklyDinner() {
    LocalDate now = LocalDate.now();
    LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
    LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);

    for (Dinner dinner : weeklyDinners) {
      LocalDate dinnerDate = dinner.getDate();
      if (!dinnerDate.isBefore(startOfWeek) && !dinnerDate.isAfter(endOfWeek)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Check if a user has a weekly dinner on next week.
   *
   * @return true if the user has a weekly dinner on next week, otherwise false
   */
  public boolean checkNextWeekDinner() {
    LocalDate now = LocalDate.now();
    LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
    LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);
    LocalDate startOfNextWeek = startOfWeek.plusWeeks(1);
    LocalDate endOfNextWeek = endOfWeek.plusWeeks(1);

    for (Dinner dinner : weeklyDinners) {
      LocalDate dinnerDate = dinner.getDate();
      if (!dinnerDate.isBefore(startOfNextWeek) && !dinnerDate.isAfter(endOfNextWeek)) {
        return true;
      }
    }
    return false;
  }

  /**Set the favorite recipes of the user.
   *
   */
  public void setFavorites(ArrayList<Recipe> favoriteRecipes) {
    this.favorites = favoriteRecipes;
  }

  /**
  * Get the favorite recipes of the user.
  *
  * @return an arraylist with the favorite recipes
  */
  public ArrayList<Recipe> getFavorites() {
    ArrayList<Recipe> copyFavorites = new ArrayList<>();
    for (Recipe recipe : favorites) {
      copyFavorites.add(recipe);
    }
    return copyFavorites;
  }

  /** Add a recipe to the user's favorites.
   *
   * @param recipe the recipe to add
   */
  public void addToFavorites(Recipe recipe) {
    //if recipe name is already in, do nothing, if not add it
    for (Recipe recip : favorites) {
      if (recip.getName().equalsIgnoreCase(recipe.getName())) {
        return;
      }
    } 
    recipe.star();
    favorites.add(recipe);
  }

  /**
  * Remove a recipe from the user's favorites.
  *
  * @param recipe the recipe to remove
  */
  public void removeFromFavorites(Recipe recipe) {
    recipe.unstar();
    favorites.remove(recipe);
  }

  /**
   * Add ingredients to the shopping list with a given week number.
   *
   * @param weekNumber the week number of the shopping list
   * @param ingredients the ingredients to add
   */
  public void addIngredientsToShoppingList(int weekNumber, ArrayList<Ingredient> ingredients) {
    for (ShoppingList shoppingList : shoppingLists) {
      if (shoppingList.getWeekNumber() == weekNumber) {
        shoppingList.addIngredients(ingredients);
        return;
      }
    }
    // if there is no shopping list with the given week number create it with the ingredients
    shoppingLists.add(new ShoppingList(weekNumber, ingredients));
  }

  /**
   * Get the shopping list for a given week number.
   *
   * @param weekNumber the week number of the shopping list
   * @return and arraylsit with the ingredients of the shopping list
   */
  public ArrayList<Ingredient> getShoppingList(int weekNumber) {
    for (ShoppingList shoppingList : shoppingLists) {
      if (shoppingList.getWeekNumber() == weekNumber) {
        return shoppingList.getIngredients();
      }
    }
    // if there is no shopping list with the given week number return an empty ararylist
    return new ArrayList<Ingredient>();
  }

  /**
   * Edit the quantity of an ingredient in the shopping list of a given week.
   *
   * @param weekNumber the number of the week
   * @param ingredientName the name of the ingredient to edit
   * @param newQuantity the updated quantity of the ingredient
   */
  public void editIngredientQuantityInShoppingList(int weekNumber,
      String ingredientName, float newQuantity) {
    for (ShoppingList shoppingList : shoppingLists) {
      if (shoppingList.getWeekNumber() == weekNumber) {
        shoppingList.editIngredientQuantity(ingredientName, newQuantity);
      }
    }
  }

  /**
   * Delete an ingredient from the shopping list of the given week.
   *
   * @param weekNumber the number if the week
   * @param ingredientName the name of the ingredient to delete
   */
  public void deleteIngredientFromShoppingList(int weekNumber, String ingredientName) {
    for (ShoppingList shoppingList : shoppingLists) {
      if (shoppingList.getWeekNumber() == weekNumber) {
        shoppingList.deleteIngredient(ingredientName);
      }
    }
  }

  //set shoppinglist
  /**
   * Set the shopping list of the user.
   *
   * @param shoppingLists the shopping list to set
   */
  public void setShoppingLists(ArrayList<ShoppingList> shoppingLists) {
    this.shoppingLists = shoppingLists;
  }

  /**
   * Get the shopping list of the user.
   *
   * @return an arraylist with the shopping lists
   */
  public ArrayList<ShoppingList> getShoppingLists() {
    return shoppingLists;
  }
  
  
}
