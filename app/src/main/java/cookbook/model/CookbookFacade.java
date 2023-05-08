package cookbook.model;

import cookbook.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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
    boolean loginSuccess = database.userLogin(userName, password);
    if (loginSuccess) {
      setCurrentUser(userName);
    }
    return loginSuccess;
  }
  

  public boolean userSignUp(String userName, String password, String displayName) {
    boolean signUpSuccess = database.userSignUp(userName, password, displayName);
    if (signUpSuccess) {
      setCurrentUser(userName);
    }
    return signUpSuccess;
  }
  

  public void setCurrentUser(String userName) {
    user = new User(database.getUserId(userName), userName, database.getUserDisplayName(userName));
  }

  public String getUserDisplayName() {
    if (user == null) {
      return "Unknown User";
    }
    System.out.println("Current user: " + user.getUsername());
    return user.getDisplayName();
  }
  


  public ArrayList<String> getPrivateTagsForUser() {
    if (user == null) {
      return new ArrayList<>();
    } else {
      return database.getPrivateTagsForUser(user.getUsername());
    }
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
  public void addRecipe(String recipe[], ArrayList<String[]> ingredients, ArrayList<String> tags) {
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
   * Add tags to a recipe
   *
   * @param tags the tags in an ArrayList
   * @param recipeName the name of the recipe
   */
  public void addTagsToRecipe(ArrayList<String> tags, String recipeName) {
    for (Recipe recipe : recipes) {
      if (recipe.getName() == recipeName) {
        recipe.setTags(tags);
      }
    }
  }

  public void updateTagToDatabase(ArrayList<String> tags, String recipeName) {
    database.updateTagToDatabase(tags, recipeName, user.getUsername());
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
  public ArrayList<Recipe> getRecipesWithFilters(ArrayList<String> keywords, ArrayList<String> ingredients,
      ArrayList<String> tags) {
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

  public boolean toggleFavorite(Recipe recipe) {
    boolean isFavorite;
    try {
      if (isRecipeFavorite(user.getUsername(), recipe)) {
        removeRecipeFromFavorites(user.getUsername(), recipe);
        isFavorite = false;
      } else {
        addRecipeToFavorites(user.getUsername(), recipe);
        isFavorite = true;
      }
    } catch (SQLException e) {
      // Handle SQLException properly
      e.printStackTrace();
      return false;
    }
  
    return isFavorite;
  }
  

  public ArrayList<Recipe> loadFavoriteRecipes() {
    return database.loadFavoriteRecipes(user.getUsername());
  }
  
  public void addRecipeToFavorites(String username, Recipe recipe) throws SQLException {
    database.addRecipeToFavorites(username, recipe);
  }
  
  
  public void removeRecipeFromFavorites(String username, Recipe recipe) throws SQLException {
    database.removeRecipeFromFavorites(username, recipe);
  }
  
  
  public boolean isRecipeFavorite(String username, Recipe recipe) throws SQLException {
    return database.isRecipeFavorite(username, recipe);
  }

  public String getCurrentUser() {
    if (user != null) {
      return user.getUsername();
    } else {
      return null;
    }
  }
  
  /**
   * Star a recipe.
   *
   * @param recipe the recipe to star
   */
  public void starRecipe(Recipe recipe) {
    user.addToFavorites(recipe);
  }

  /**
   * Unstar recipe.
   *
   * @param recipe the recipe to unstar
   */
  public void unstarRecipe(Recipe recipe) {
    user.removeFromFavorites(recipe);
  }

  /**
   * Get the user's favorite recipes.
   *
   * @return an arraylist with the user's favorite recipes
   */
  public ArrayList<Recipe> getFavoriteRecipes() {
    return user.getFavorites();
  }

  public boolean isFavorite(Recipe recipe) {
    if (user == null) {
      return false;
    }

    ArrayList<Recipe> favoriteRecipes = user.getFavorites();
    return favoriteRecipes.stream().anyMatch(favoriteRecipe -> favoriteRecipe.getName().equals(recipe.getName()));
  }

}

