package cookbook.model;

import cookbook.database.Database;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

/**
 * The Cookbook facade class.
 */
public class CookbookFacade {
  private User user;
  private Database database;
  private ArrayList<Recipe> recipes;

  /**
   * Cookbook Constructor.
   */
  public CookbookFacade(Database database) {
    recipes = new ArrayList<Recipe>();
    this.database = database;
  }

  public void loadAllRecipes() {
    recipes = database.loadAllRecipes(user.getUsername());
  }

  public boolean checkIfUserNameExists(String userName) {
    return database.checkIfUserNameExists(userName);
  }

  public boolean userLogin(String userName, String password) {
    return database.userLogin(userName, password);
  }

  public boolean userSignUp(String userName, String password, String displayName) {
    return database.userSignUp(userName, password, displayName);
  }

  public void setCurrentUser(String userName) {
    user = new User(database.getUserId(userName), userName, database.getUserDisplayName(userName));
  }

  public String getUserDisplayName() {
    System.out.println("Current user: " + user.getUsername());
    return user.getDisplayName();
  }

  /**
   * Get the private tags of the user from the database.
   *
   * @return the user's private tags
   */
  public ArrayList<String> getPrivateTagsForUser() {
    if (user == null) {
      return new ArrayList<>();
    } else {
      return database.getPrivateTagsForUser(user.getUsername());
    }
  }

  public boolean saveRecipeToDatabase(String[] recipe, ArrayList<String[]> ingredients,
      ArrayList<String> tags) {
    return database.saveRecipeToDatabase(recipe, ingredients, tags, user.getUsername());
  }

  public void userLogout() {
    user = null;
  }

  /**
   * Add a recipe to the cookbook.
   *
   * @param recipe the information about the recipe in a string array
   * @param ingredients the ingredients of the recipe in an arraylist of string arrays
   * @param tags the tags of the recipe in a string array
   */
  public void addRecipe(String[] recipe, ArrayList<String[]> ingredients, ArrayList<String> tags) {
    recipes.add(new Recipe(recipe[0], recipe[1], recipe[2], ingredients, tags));
  }

  /**
   * Add a pre-made recipe object to the cookbook.
   *
   * @param recipe the recipe object.
   */
  public void addRecipe(Recipe recipe) {
    recipes.add(recipe);
  }

  /**
   * Set the recipes of the cookbook.
   *
   * @param recipes the recipe objects
   */
  public void setRecipes(ArrayList<Recipe> recipes) {
    this.recipes.addAll(recipes);
  }

  /**
   * Delete a recipe from the cookbook.
   *
   * @param name the name of the recipe to remove.
   */
  public void removeRecipe(String name) {
    recipes.removeIf(recipe -> Objects.equals(name, recipe.getName()));
  }

  /**
   * Get the recipes of the cookbook.
   *
   * @return the recipe objects
   */
  public ArrayList<Recipe> getRecipes() {
    ArrayList<Recipe> copyRecipes = new ArrayList<Recipe>();
    for (Recipe recipe : recipes) {
      copyRecipes.add(recipe);
    }
    return copyRecipes;
  }

  /**
   * Add tags to a recipe.
   *
   * @param tags the tags in an ArrayList
   * @param recipe the recipe
   */
  public void addTagsToRecipe(ArrayList<String> tags, Recipe recipe) {
    if (recipes.contains(recipe)) {
      recipe.setTags(tags);
    }
  }

  public void updateTagToDatabase(ArrayList<String> tags, Recipe recipe) {
    database.updateTagToDatabase(tags, recipe.getId(), user.getUsername());
  }

  /**
   * Get the recipes of the cookbook containing the given keywords in the name.
   *
   * @param keywords the keywords to search for in the recipes
   * @return the recipes containing the keywords
   */
  public ArrayList<Recipe> getRecipesWithName(ArrayList<String> keywords) {
    // return a list of recipes that contain any of the keywords in their name
    return recipes.stream()
        // change the characters to lower case and to find the match use the stream
        // collecter
        .filter(recipe -> keywords.stream()
            .allMatch(keyword -> recipe.getName().toLowerCase().contains(keyword.toLowerCase())))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Get the recipes of the cookbook containing the given ingredients.
   *
   * @param ingredients the ingredients to search for in the recipes
   * @return the recipes containing the ingredients
   */
  public ArrayList<Recipe> getRecipesWithIngredients(ArrayList<String> ingredients) {
    // return a list of recipes that contain all the ingredients
    return recipes.stream()
        // as above
        .filter(recipe -> ingredients.stream()
            .allMatch(ingredient -> recipe.getIngredients().stream()
                .anyMatch(i -> i.getName().toLowerCase().contains(ingredient.toLowerCase()))))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Get the recipes of the cookbook containing the given tags.
   *
   * @param tags the tags to search for in the recipes
   * @return the recipes containing the tags
   */
  public ArrayList<Recipe> getRecipesWithTags(ArrayList<String> tags) {
    // return a list of recipes that contain all the tags
    return recipes.stream()
        .filter(recipe -> recipe.getTags().containsAll(tags))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Get the recipes of the cookbook containing the given filters.
   *
   * @param keywords the keywords to search for in the recipes names
   * @param ingredients the ingredients to search for in the recipes
   * @param tags the tags to search for in the recipes
   * @return the recipes containing the all the keywords, ingredients, and tags
   */
  public ArrayList<Recipe> getRecipesWithFilters(ArrayList<String> keywords, 
      ArrayList<String> ingredients, ArrayList<String> tags) {
    ArrayList<Recipe> filteredRecipes = new ArrayList<Recipe>();

    // Apply keywords filter
    if (!keywords.isEmpty()) {
      filteredRecipes.addAll(getRecipesWithName(keywords));
      // If no keywords are given add all the recipes
    } else {
      filteredRecipes.addAll(recipes);
    }

    // Apply ingredients filter and retain mutual recipes
    if (!ingredients.isEmpty()) {
      filteredRecipes.retainAll(getRecipesWithIngredients(ingredients));
    }

    // Apply tags filter and retain mutual recipes
    if (!tags.isEmpty()) {
      filteredRecipes.retainAll(getRecipesWithTags(tags));
    }

    return filteredRecipes;
  }

  /**
   * Add a recipe to the user's dinner list.
   *
   * @param date the date of the dinner
   * @param recipe the recipe to add
   * @return false if the recipe is already in the dinner on the given date, otherwise true
   */
  public boolean addRecipeToDinnerList(LocalDate date, Recipe recipe) {
    return user.addWeeklyDinner(date, recipe);
  }

  /**
   * Get the weekly dinners of the user.
   *
   * @return an arraylist with the dinners of the user
   */
  public ArrayList<Dinner> getDinnerList() {
    return user.getWeeklyDinners();
  }

  public boolean saveWeeklyDinnerToDatabase() {
    return database.saveWeeklyDinnerToDatabase(getDinnerList(), user.getUsername());
  }

  /**Load the weekly dinners of the user from the database.
   *  
   */
  public void loadWeeklyDinnerFromDatabase() {
    ArrayList<Dinner> dinnerList = database.loadWeeklyDinnerListFromDatabase(user.getUsername());
    for (Dinner dinner : dinnerList) {
      for (Recipe recipe : recipes) {
        for (Recipe dinnerRecipe : dinner.getRecipes()) {
          if (recipe.getName().equals(dinnerRecipe.getName())) {
            user.addWeeklyDinner(dinner.getDate(), recipe);
          }
        }
      }
    } 
  }


  /** Method check if a recipe name is already in the cookbook.
   *
   * @param recipeName the name of the recipe
   * @return true if the recipe name is already in the cookbook, otherwise false
   */
  public boolean checkRecipeName(String recipeName) {
    for (Recipe recipe : recipes) {
      if (recipe.getName().equals(recipeName)) {
        return true;
      }
    }
    return false;
  }

  //Check if a user has a weekly dinner on current week.
  public boolean checkWeeklyDinner() {
    return user.checkWeeklyDinner();
  }

  //check if a user has a weekly dinner for next week
  public boolean checkNextWeekDinner() {
    return user.checkNextWeekDinner();
  }

  /** Load the favorite recipes of the user from the database.
  *
  */
  public void loadFavoriteRecipes() {
    ArrayList<Recipe> favoriteRecipes =  database.loadFavoriteRecipes(user.getUsername());
    //Set attribute isstar to true for all the favorite recipes in the cookbook
    for (Recipe r : favoriteRecipes) {
      for (Recipe recipe : recipes) {
        if (r.getName().equals(recipe.getName())) {
          recipe.star();
          user.addToFavorites(recipe);
        }
      }
    }
  }
  
  /** Add a recipe to the user's favorite recipes.
   *
   * @param recipe the recipe to add
   * @return true if the recipe is added to the favorite recipes, otherwise false
   */
  public boolean addRecipeToFavorites(Recipe recipe) {
    System.out.println(recipe);
    user.addToFavorites(recipe);
    //change the recipe attribute starred to true 
    //in the arraylist of recipes in this cookbookfacade class
    for (Recipe r : recipes) {
      if (r.getName().equals(recipe.getName())) {
        r.star();
        System.out.println(r);
      }
    }
    return database.addRecipeToFavorites(user.getUsername(), recipe.getName());
  }
  
  /** Remove a recipe from the user's favorite recipes.
   *
   * @param recipe the recipe to remove
   * @return true if the recipe is removed from the favorite recipes, otherwise false
   */
  public boolean removeRecipeFromFavorites(Recipe recipe) {
    System.out.println(recipe);
    user.removeFromFavorites(recipe);
    //change the recipe attribute starred to false 
    //in the arraylist of recipes in this cookbookfacade class
    for (Recipe r : recipes) {
      if (r.getName().equals(recipe.getName())) {
        r.unstar();
        System.out.println(r);
      }
    }
    return database.removeRecipeFromFavorites(user.getUsername(), recipe.getName());
  }
  

  /**
   * Get the user's favorite recipes.
   *
   * @return an arraylist with the user's favorite recipes
   */
  public ArrayList<Recipe> getFavoriteRecipes() {
    return user.getFavorites();
  }

  public void removeRecipeFromWeeklyDinner(LocalDate dayDate, Recipe recipe) {
    user.removeRecipeFromWeeklyDinner(dayDate, recipe);
    database.removeRecipeFromWeeklyDinnerInDatabase(user.getUsername(), dayDate, recipe.getName());
  }

  /** Edit a recipe in the cookbook.
   *
   * @param recipe the recipe to edit
   * @param name is the new name of the recipe
   * @param description is the new description of the recipe
   * @param instructions is the new instructions of the recipe
   * @param ingredients is the new ingredients of the recipe
   * @param tags is the new tags of the recipe
   */

  public void editRecipe(Recipe recipe, String name, String description, String instructions, 
      ArrayList<String[]> ingredients, ArrayList<String> tags) {
    recipe.setName(name);
    recipe.setShortDesc(description);
    recipe.setDirection(instructions);
    recipe.setIngredients(ingredients);
    recipe.setTags(tags);
    database.editRecipeInDatabase(recipe.getId(), name, description, instructions, ingredients, tags, user.getUsername());
  }

  public void deleteRecipeFromShoppingList(Recipe recipe, int weekNumber) {
    database.deleteRecipeFromShoppingList(user.getUsername(), recipe.getId(), weekNumber);
  }


  public void addRecipeToShoppingList(Recipe recipe, int weekNumber) {
    database.addRecipeToShoppingList(user.getUsername(), recipe.getId(), weekNumber);
  }


  public void editIngredientInShoppingList(String ingredientName, float newQuantity, int weekNumber) {
    database.editIngredientQuantity(user.getUsername(), ingredientName, newQuantity, weekNumber);


  }

  public void deleteIngredientInShoppingList(String ingredientName, int weekNumber) {
    database.deleteIngredientFromShoppingList(user.getUsername(), ingredientName, weekNumber);
  }

  

  public ArrayList<ShoppingList> getShoppingList() {
    ArrayList<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();

    // Add weekly dinner list to shopping list, first check the date, then add the ingredients of each recipe
    for (Dinner dinner : user.getWeeklyDinners()) {
        int weekNumber = dinner.getWeekNumber();
        ShoppingList shoppingList = null;

        // Find the existing shopping list for the week, if it exists
        for (ShoppingList existingList : shoppingLists) {
            if (existingList.getWeekNumber() == weekNumber) {
                shoppingList = existingList;
                break;
            }
        }

        // If the shopping list for the week doesn't exist, create a new one and add it to the shoppingLists
        if (shoppingList == null) {
            shoppingList = new ShoppingList(weekNumber, new ArrayList<Ingredient>());
            shoppingLists.add(shoppingList);
        }

        // Add the ingredients of each recipe to the shopping list, or update the quantity if the ingredient already exists
        for (Recipe recipe : dinner.getRecipes()) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                boolean ingredientExists = false;

                for (Ingredient existingIngredient : shoppingList.getIngredients()) {
                    if (existingIngredient.getName().equals(ingredient.getName()) &&
                            existingIngredient.getMeasurementUnit().equals(ingredient.getMeasurementUnit())) {
                        existingIngredient.setQuantity(existingIngredient.getQuantity() + ingredient.getQuantity());
                        ingredientExists = true;
                        break;
                    }
                }

                if (!ingredientExists) {
                    shoppingList.addIngredient(new Ingredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasurementUnit()));
                }
            }
        }
    }

    return shoppingLists;
}

  public ArrayList<ShoppingList> loadShoppingListsFromDatabase() {
    return database.loadShoppingListsFromDatabase(user.getUsername());
  }

  public ArrayList<User> loadAllUsers() {
    ArrayList<User> users = new ArrayList<User>();
    for (User user : users) {
      users.add(new User(0, "hi", "hihi"));
      
    }return users;
  }


  


}
