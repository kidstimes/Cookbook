package cookbook.model;

import cookbook.database.Database;
import java.time.LocalDate;
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
  private ArrayList<User> loggedOutUsers;


  /**
   * Cookbook Constructor.
   */
  public CookbookFacade(Database database) {
    recipes = new ArrayList<Recipe>();
    this.database = database;
    this.loggedOutUsers = new ArrayList<>();
  }

  public void loadAllRecipes() {
    recipes = database.loadAllRecipes(user.getUsername());
  }

  public boolean checkIfUserNameExists(String userName) {
    return database.checkIfUserNameExists(userName);
  }

  public boolean checkIfUserNameExistsExceptSelf(String userName, int userId) {
    return database.checkIfUserNameExistsExceptSelf(userName, userId);
  }

  public boolean userLogin(String userName, String password) {
    return database.userLogin(userName, password);
  }

  public boolean userSignUp(String userName, String password, String displayName) {
    return database.userSignUp(userName, password, displayName);
  }

  public boolean checkPasswordForUser(String password) {
    return database.checkPasswordForUserInDatabase(user.getUsername(), password);
  }

  public boolean changePasswordForUser(String newPassword) {
    return database.changePasswordForUserInDatabase(user.getUsername(), newPassword);
  }

  public boolean changeDisplayNameForUser(String newDisplayName) {
    user.modifyDisplayName(newDisplayName);
    return database.changeDisplayNameForUserInDatabase(user.getUsername(), newDisplayName);
  }

  public void deleteUser(int userId) {
    database.deleteUser(userId);
  }

  public boolean isDeletedUser(String userName) {
    return database.isDeletedUserInDatabase(userName);
  }

  /**
   * Set the user to logged in user and load logged out users.
   *
   * @param userName the username of the logged in user
   */
  public void setCurrentUser(String userName) {
    user = new User(database.getUserId(userName), userName, database.getUserDisplayName(userName));
    loggedOutUsers = database.loadLoggedOutUsers(user);
    System.out.println("Userid" + database.getUserId(userName));
  }

  public String getUserDisplayName() {
    return user.getDisplayName();
  }

  public String getUserName() {
    return user.getUsername();
  }

  public int getUserId() {
    return user.getId();
  } 

  /*
   * editUser to edit the user's username and display name.
   */
  public void editUser(int userId, String userName, String displayName) {
    database.editUser(userId, userName, displayName);
  }

  /*
   * editUserPassword to edit the user's password.
   */
  public void editUserPassword(int userId, String password) {
    database.editUserPassword(userId, password);
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
  public void remove(String name) {
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

  /** Update the tags of a recipe in the database.
   *
   * @param tags the tags
   * @param recipe the recipe
   */
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
    //Set attribute starred to true for all the favorite recipes in the cookbook
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
    user.addToFavorites(recipe);
    //change the recipe attribute starred to true 
    //in the arraylist of recipes in this cookbookfacade class
    for (Recipe r : recipes) {
      if (r.getName().equals(recipe.getName())) {
        r.star();
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
    user.removeFromFavorites(recipe);
    //change the recipe attribute starred to false 
    //in the arraylist of recipes in this cookbookfacade class
    for (Recipe r : recipes) {
      if (r.getName().equals(recipe.getName())) {
        r.unstar();
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

  /**Remove a recipe from the weekly dinner of the user.
   *
   * @param dayDate the date of the dinner
   * @param recipe the recipe to remove
   */
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
   */
  public void editRecipe(Recipe recipe, String name, String description, String instructions, 
      ArrayList<String[]> ingredients) {
    recipe.setName(name);
    recipe.setShortDesc(description);
    recipe.setDirection(instructions);
    recipe.setIngredients(ingredients);
    database.editRecipeInDatabase(recipe.getId(), name, description, instructions, ingredients);
  }

  /** Add all ingredients of a recipe to the shopping list of the user.
   *
   * @param recipe the recipe to add
   * @param weekNumber the week number of the dinner
   */
  public void addRecipeToShoppingList(Recipe recipe, int weekNumber) {
    database.addRecipeToShoppingList(user.getUsername(), recipe.getId(), weekNumber);
  }


  /**
   * Refresh the shopping list of the user with the most updated weekly dinner list.
   */
  public void refreshShoppingListWithWeeklyDinnerList() {
    database.clearUserShoppingList(user.getUsername());
    for (Dinner dinner : user.getWeeklyDinners()) {
      for (Recipe recipe : dinner.getRecipes()) {
        database.addRecipeToShoppingList(user.getUsername(),
            recipe.getId(), dinner.getWeekNumber());
      }
    }
  }

  /**
   * Edit a ingredient in the shopping list of the user.
   *
   * @param ingredientName the name of the ingredient
   * @param newQuantity the new quantity of the ingredient
   * @param weekNumber the week number of the shopping list
   */
  public void editIngredientInShoppingList(String ingredientName,
        float newQuantity, int weekNumber) {
    database.editIngredientQuantityInShoppingList(user.getUsername(),
        ingredientName, newQuantity, weekNumber);
  }

  /** Delete a ingredient in the shopping list of the user.
   *
   * @param ingredientName the name of the ingredient
   * @param weekNumber the week number of the shopping list
   */
  public void deleteIngredientInShoppingList(String ingredientName, int weekNumber) {
    database.deleteIngredientFromShoppingList(user.getUsername(), ingredientName, weekNumber);
  }

  //Load the shopping list of the user from the database.
  public ArrayList<ShoppingList> loadShoppingListsFromDatabase() {
    user.setShoppingLists(database.loadShoppingListsFromDatabase(user.getUsername()));
    return user.getShoppingLists();
  }

  /** Load all users from the database.
   *
   * @return an arraylist with all the users
   */
  public ArrayList<User> loadAllUsers() {
    return database.loadAllUsersFromDatabase();
  }

  /**
   * Add ingredients to the shopping list of the user with a given week number.
   *
   * @param weekNumber the week number of the shopping list
   * @param ingredients the ingredients to add to the shopping list
   */
  public void addIngredientsToUsersShoppingList(int weekNumber, ArrayList<Ingredient> ingredients) {
    user.addIngredientsToShoppingList(weekNumber, ingredients);
  }

  /**
   * Get the shopping list of the user for the given week number.
   *
   * @param weekNumber the week number of the shopping list
   * @return an arraylist with the ingredients of the shopping list
   */
  public ArrayList<Ingredient> getUsersShoppingList(int weekNumber) {
    return user.getShoppingList(weekNumber);
  }

  /**
   * Edit the quantity of an ingredient in the user's shopping list of a given week.
   *
   * @param weekNumber the number of the week
   * @param ingredientName the name of the ingredient to edit
   * @param newQuantity the updated quantity of the ingredient
   */
  public void editIngredientQuantityInUsersShoppingList(int weekNumber,
      String ingredientName, float newQuantity) {
    user.editIngredientQuantityInShoppingList(weekNumber, ingredientName, newQuantity);
  }

  /**
   * Delete an ingredient from the user's shopping list of the given week.
   *
   * @param weekNumber the number of the week
   * @param ingredientName the name of the ingredient to delete
   */
  public void deleteIngredientFromUsersShoppingList(int weekNumber, String ingredientName) {
    user.deleteIngredientFromShoppingList(weekNumber, ingredientName);
  }

  public void addComment(Recipe recipe, String commentText) {
    database.addComment(recipe.getId(), user.getId(), commentText);
  }

  public void updateComment(int commentId, String commentText) {
    database.updateComment(commentId, commentText);
  }

  public void deleteComment(int commentId) {
    database.deleteComment(commentId);
  }

  public ArrayList<Comment> getComments(int recipeId) {
    return database.getComments(recipeId);
  }

  public ArrayList<Message> getReceivedMessagesOfUser() {
    return user.getReceivedMessages();
  }

  public ArrayList<Message> getSentMessagesOfUser() {
    return user.getSentMessages();
  }

  public ArrayList<Message> getInboxMessages() {
    //return database.getInboxMessages(user.getId());
    ArrayList<Message> messages = new ArrayList<Message>();

    return messages;
  }

  public ArrayList<Message> getOutboxMessages() {
    //return database.getOutboxMessages(user.getId());
    ArrayList<Message> messages = new ArrayList<Message>();

    return messages;
  }

	public int getNumberUnreadMessages() {
    int numberUnreadMessages = 0;
    for (Message message : getInboxMessages()) {
      if (!message.isRead()) {
        numberUnreadMessages++;
      }
    }
    return numberUnreadMessages;
	}


  /**
   * Add a message to the sent messages of the user.
   *
   * @param messageId the unique id of the message
   * @param recipe the recipe being sent with the message
   * @param text the text being sent with the message
   * @param receiverUsername the username of the user that received the message
   */
  public void sendMessageToUser(int messageId, Recipe recipe,
      String text, String receiverUsername) {
    User receiver = new User(null, null);
    for (User user : loggedOutUsers) {
      if (user.getUsername() == receiverUsername) {
        receiver = user;
      }
    }
    user.sendMessage(messageId, recipe, text, receiver);
  }


}
