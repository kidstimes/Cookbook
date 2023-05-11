package cookbook.database;

import cookbook.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handler for the database connection.
 */
public class Database {
  private Connection connection;

  /**
   * Database Constructor.
   * Opes the connection with the database.
   */
  public Database() {
    try {
      connection = DriverManager.getConnection(
          "jdbc:mysql://localhost/cookbook?user=root&password=12345678&useSSL=false");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Get the connection with the database.
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Check if the username exists in the database.
   */
  public boolean checkIfUserNameExists(String userName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ?")) {
      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();

      // If there is a record in the result set, then the username exists
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    // If there is no record in the result set or an exception occurs, the username
    // does not exist
    return false;
  }

  /**
   * Sign up a new user in the database.
   */
  public boolean userSignUp(String userName, String password, String displayName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO users (username, password_hash, displayname ) VALUES (?, ?, ?)")) {
      stmt.setString(1, userName);
      stmt.setString(2, hashPassword(password));
      stmt.setString(3, displayName);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * User logs in to the application.
   */
  public boolean userLogin(String userName, String password) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ? AND password_hash = ?")) {
      stmt.setString(1, userName);
      stmt.setString(2, hashPassword(password));
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**Get user id from the database based on username.
   *
   * @param userName the username of the user
   * @return the id of the user
   */
  public int getUserId(String userName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT id FROM users WHERE username = ?")) {
      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("id");
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return -1;
  }

  /**
   * Get the display name of the user.
   */
  public String getUserDisplayName(String userName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT displayname FROM users WHERE username = ?")) {
      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("displayname");
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Method to hash the password.
   */
  public static String hashPassword(String password) {
    try {
      // Create a MessageDigest instance for SHA-256
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

      // Hash the password as bytes
      byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
      byte[] hashedBytes = messageDigest.digest(passwordBytes);

      // Convert the hashed bytes to a hexadecimal string
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashedBytes) {
        hexString.append(String.format("%02x", b));
      }

      // Return the hashed password as a hexadecimal string
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      // Handle the exception if the hashing algorithm is not supported
      throw new RuntimeException("SHA-256 is not supported", e);
    }
  }

  /**
   * Save a recipe to the database and associate tags with the recipe.
   */
  public boolean saveRecipeToDatabase(String[] recipe, ArrayList<String[]> ingredients,
       ArrayList<String> tags,
      String userName) {
    try {
      // Get the user id
      int userId = getUserId(userName);
      // Insert the recipe into the recipes table
      String query = "INSERT INTO recipes (name, description, instructions) VALUES (?, ?, ?)";
      try (PreparedStatement statement =
           connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, recipe[0]); // name
        statement.setString(2, recipe[1]); // description
        statement.setString(3, recipe[2]); // instructions
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
          int recipeId = generatedKeys.getInt(1);

          // Insert ingredients into the ingredients table
          for (String[] ingredient : ingredients) {
            String name = ingredient[0];
            String quantity = ingredient[1];
            String measurementUnit = ingredient[2];
            insertIngredient(recipeId, name, quantity, measurementUnit);
          }

          // Define the predefined tags
          List<String> predefinedTags = Arrays.asList("vegan", "vegetarian", "lactose free",
              "gluten free", "starter", "main course", "dessert and sweets");

          // Add tags to the database
          for (String tag : tags) {
            int tagId = getTagId(tag);
            if (tagId == -1) {
              // If the tag doesn't exist, insert it into the tags table
              tagId = insertTag(tag);
            }
            // If the tag is a predefined tag, insert it into the recipe_tags table,
            // otherwise insert it into the PersonalTags table
            if (predefinedTags.contains(tag.toLowerCase())) {
              insertRecipeTag(recipeId, tagId);
            } else {
              insertPersonalTag(userId, recipeId, tagId);
            }
          }
          return true;
        }
      }
    } catch (SQLException e) {
      System.out.println("Error while saving recipe to the database: " + e.getMessage());
    }
    return false;
  }

  /**
   * Insert an ingredient into the ingredients table and associate it with a
   * recipe.
   */
  private void insertIngredient(int recipeId, String name, String quantity,
         String measurementUnit) {
    try {
      String query = "INSERT INTO ingredients (recipe_id, name, quantity, measurementUnit) VALUES (?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, recipeId); // recipe_id
        statement.setString(2, name); // name
        statement.setString(3, quantity); // quantity
        statement.setString(4, measurementUnit); // measurementUnit
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Error while inserting ingredient into the database: " + e.getMessage());
    }
  }

  /**
   * Load recipes from the database and return arraylist of Recipe (from model)
   * objects.
   */
  public ArrayList<Recipe> loadAllRecipes(String userName) {
    ArrayList<Recipe> recipes = new ArrayList<>();
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT r.id, r.name, r.description, r.instructions FROM recipes r")) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Integer recipeId = rs.getInt(1);
        String name = rs.getString(2);
        String description = rs.getString(3);
        String instructions = rs.getString(4);

        // load ingredients and tags
        ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
        ArrayList<String> tags = loadTagsForRecipe(recipeId, userName);

        // create the recipe objects with ingredients and tags
        Recipe recipe = new Recipe(recipeId, name, description, instructions, ingredients, tags);
        recipes.add(recipe);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return recipes;
  }

  /**
   * Load the ingredients of recipes from the database and
   * return them in an arraylist of string arrays.
   */
  private ArrayList<String[]> loadIngredientsForRecipe(int recipeId) {
    ArrayList<String[]> ingredients = new ArrayList<>();

    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT i.name, i.quantity, i.measurementUnit "
                + "FROM ingredients i "
                + "WHERE i.recipe_id = ?")) {
      stmt.setInt(1, recipeId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String ingredientName = rs.getString(1);
        String quantity = rs.getString(2);
        String measurementUnit = rs.getString(3);
        ingredients.add(new String[] { ingredientName, quantity, measurementUnit });
      }

      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return ingredients;
  }

  /**
   * Load the tags of recipes from the database and return them in an arraylist of
   * String Objects.
   */
  private ArrayList<String> loadTagsForRecipe(int recipeId, String username) {
    ArrayList<String> tags = new ArrayList<>();
    try {
      int userId = getUserId(username);

      // Fetch tags from recipe_tags table
      String query = "SELECT name FROM tags INNER JOIN recipe_tags ON tags.id = recipe_tags.tag_id WHERE recipe_tags.recipe_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, recipeId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          tags.add(resultSet.getString("name"));
        }
      }

      // Fetch personal tags for the given username from PersonalTags table
      query = "SELECT name FROM tags INNER JOIN PersonalTags ON tags.id = PersonalTags.tag_id WHERE PersonalTags.recipe_id = ? AND PersonalTags.user_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, recipeId);
        statement.setInt(2, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          tags.add(resultSet.getString("name"));
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tags;
  }

  /**Update the tags of a recipe in the database associated with a user.
   *
   * @param tags is an arraylist of strings of tags for a recipe
   * @param recipeId is the ID of the recipe
   * @param userName is the name of the user
   */
  public void updateTagToDatabase(ArrayList<String> tags, int recipeId, String userName) {
    try {
      // Get the user and recipe IDs
      int userId = getUserId(userName);

      // Delete existing personal tags for the user and recipe
      deletePersonalTags(userId, recipeId);

      // Define the predefined tags
      List<String> predefinedTags = Arrays.asList("vegan", "vegetarian", "lactose free",
          "gluten free", "starter", "main course", "dessert and sweets");

      // Add new personal tags to the database
      for (String tag : tags) {
        int tagId = getTagId(tag);
        if (tagId == -1) {
          // If the tag doesn't exist, insert it into the tags table
          tagId = insertTag(tag);
        }
        // If the tag is a predefined tag, insert it into the recipe_tags table,
        // otherwise insert it into the PersonalTags table
        if (predefinedTags.contains(tag.toLowerCase())) {
          insertRecipeTag(recipeId, tagId);
        } else {
          insertPersonalTag(userId, recipeId, tagId);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private int getRecipeId(String recipeName) {
    String query = "SELECT id FROM recipes WHERE name = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, recipeName);
      ResultSet resultSet = statement.executeQuery();
      return resultSet.next() ? resultSet.getInt("id") : -1;
    } catch (SQLException e) {
      System.out.println("Error while getting recipe id: " + e.getMessage());
      return -1;
    }
  }

  private int getTagId(String tagName) throws SQLException {
    String query = "SELECT id FROM tags WHERE name = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, tagName);
      ResultSet resultSet = statement.executeQuery();
      return resultSet.next() ? resultSet.getInt("id") : -1;
    } catch (SQLException e) {
      System.out.println("Error while getting tag id: " + e.getMessage());
      return -1;
    }
  }

  private int insertTag(String tagName) throws SQLException {
    String query = "INSERT INTO tags (name) VALUES (?) ON DUPLICATE KEY UPDATE name = name";
    try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, tagName);
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
    }
}

  private void deletePersonalTags(int userId, int recipeId) throws SQLException {
    String query = "DELETE FROM PersonalTags WHERE user_id = ? AND recipe_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting personal tags: " + e.getMessage());
    }
  }

  private void insertPersonalTag(int userId, int recipeId, int tagId) throws SQLException {
    String query = "INSERT INTO PersonalTags (user_id, recipe_id, tag_id) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE user_id = user_id, recipe_id = recipe_id, tag_id = tag_id";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.setInt(3, tagId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while inserting personal tag: " + e.getMessage());
    }
  }




  private void insertRecipeTag(int recipeId, int tagId) throws SQLException {
    String query = "INSERT IGNORE INTO recipe_tags (recipe_id, tag_id) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, recipeId);
      statement.setInt(2, tagId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while inserting recipe tag: " + e.getMessage());
    }
  }

  /**
   * Get the private tags for the given user.
   */
  public ArrayList<String> getPrivateTagsForUser(String username) {
    ArrayList<String> privateTags = new ArrayList<>();
    int userId = getUserId(username);
    try {
      String query = "SELECT DISTINCT tags.name FROM tags INNER JOIN PersonalTags ON tags.id = PersonalTags.tag_id WHERE PersonalTags.user_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          privateTags.add(resultSet.getString("name"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return privateTags;
  }

  /**　
   * Get the recipe with the given id.
   */
  public Recipe getRecipeById(int recipeId) {
    Recipe recipe = null;
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT name, description, instructions FROM recipes WHERE id = ?"
        )
    ) {
      stmt.setInt(1, recipeId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String name = rs.getString(1);
        String description = rs.getString(2);
        String instructions = rs.getString(3);

        ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
        ArrayList<String> tags = loadTagsForRecipe(recipeId, null);

        recipe = new Recipe(name, description, instructions, ingredients, tags);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return recipe;
  }

  /** Save weekly dinner list to database.
   *
   * @param weeklyDinnerList is the list of Dinner objects to save
   * @param username is the username of the user
   * @return true if the save was successful, false otherwise
   */
  public boolean saveWeeklyDinnerToDatabase(ArrayList<Dinner> weeklyDinnerList,
       String username) {
    boolean saveSuccessful = false;
    try {
      // Prepare a SQL statement to insert or update records in the WeekMenuRecipe table
      String sql = "INSERT INTO WeeklyDinner (user_id, recipe_id, dinner_date) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE dinner_date = VALUES(dinner_date)";
      PreparedStatement statement = connection.prepareStatement(sql);
  
      // Get the user ID from the username
      int userId = getUserId(username);
  
      // Loop through each Dinner object in the weeklyDinnerList
      for (Dinner dinner : weeklyDinnerList) {
        // Get the date for this Dinner
        LocalDate dinnerDate = dinner.getDate();
  
        // Loop through each Recipe in the Dinner
        for (Recipe recipe : dinner.getRecipes()) {
          // Get the recipe ID from the recipe name
          int recipeId = getRecipeId(recipe.getName());
  
          // Set the values in the prepared statement
          statement.setInt(1, userId);
          statement.setInt(2, recipeId);
          statement.setDate(3, java.sql.Date.valueOf(dinnerDate));
  
          // Execute the SQL statement to insert or update the record
          statement.executeUpdate();
        }
      }
  
      // Close the prepared statement
      statement.close();
  
      // If no exceptions were thrown, the save was successful
      saveSuccessful = true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  
    return saveSuccessful;
  }

  /**Load weekly dinner list from database.
   *
   * @param username is the username of the user
   * @return the list of weekly Dinner objects loaded from the database
   */
  public ArrayList<Dinner> loadWeeklyDinnerListFromDatabase(String username) {
    ArrayList<Dinner> weeklyDinnerList = new ArrayList<>();
    try {
      // Get the user ID from the username
      int userId = getUserId(username);
  
      // Prepare a SQL statement to select records from the WeekMenuRecipe table
      String sql = "SELECT recipe_id, dinner_date FROM WeeklyDinner WHERE user_id = " + userId;
      Statement statement = connection.createStatement();
  
      // Execute the SQL statement and get the result set
      ResultSet result = statement.executeQuery(sql);
  
      // Loop through each row in the result set
      while (result.next()) {
        // Get the recipe ID and date for this row
        int recipeId = result.getInt("recipe_id");
        LocalDate dinnerDate = result.getDate("dinner_date").toLocalDate();
  
        // Get the Recipe object for this recipe ID
        Recipe recipe = getRecipeById(recipeId);
  
        // Check if there is already a Dinner object for this date
        Dinner dinner = null;
        for (Dinner d : weeklyDinnerList) {
          if (d.getDate().equals(dinnerDate)) {
            dinner = d;
            break;
          }
        }
        // If there is no Dinner object for this date, create a new one
        if (dinner == null) {
          dinner = new Dinner(dinnerDate, recipe);
          weeklyDinnerList.add(dinner);
        } else {
          // Add the Recipe object to the existing Dinner object
          dinner.addRecipe(recipe);
        }
      }
  
      // Close the result set and statement
      result.close();
      statement.close();
    } catch (SQLException e) {
      System.out.println("Error loading weekly dinner list from database: " + e.getMessage());
    }
  
    return weeklyDinnerList;
  }

  /** Remove a recipe from the weekly dinner list in the database.
   *
   * @param username is the username of the user
   * @param dayDate is the date of the dinner
   * @param recipeName is the name of the recipe to remove
   * @return true if the remove was successful, false otherwise
   */
  public boolean removeRecipeFromWeeklyDinnerInDatabase(String username, LocalDate dayDate, String recipeName) {
    boolean deleteSuccessful = false;
    try {
      // Get the user ID from the username
      int userId = getUserId(username);
      // Get the recipe ID from the recipe name
      int recipeId = getRecipeId(recipeName);
      // Prepare a SQL statement to delete the record from the WeeklyDinner table
      String sql = "DELETE FROM WeeklyDinner WHERE user_id = ? AND recipe_id = ? AND dinner_date = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      // Set the values in the prepared statement
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.setDate(3, java.sql.Date.valueOf(dayDate));

      // Execute the SQL statement to delete the record
      int rowsAffected = statement.executeUpdate();

      // If at least one row was affected, the delete was successful
      if (rowsAffected > 0) {
        deleteSuccessful = true;
      }

      // Close the prepared statement
      statement.close();
    } catch (SQLException e) {
      System.out.println("Error removing recipe from weekly dinner in database: " + e.getMessage());
    }
    return deleteSuccessful;
  }



  /**
   * Get the favorite recipes for the given user.
   *
   * @param userName the user's name
   * @return the list of favorite recipes of the user
   */
  public ArrayList<Recipe> loadFavoriteRecipes(String userName) {
    ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
    int userId = getUserId(userName);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT r.id, r.name, r.description, r.instructions FROM recipes r " 
            + "INNER JOIN favorites f ON r.id = f.recipe_id " 
            + "WHERE f.user_id = ?"
        )
    ) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Integer recipeId = rs.getInt(1);
        String name = rs.getString(2);
        String description = rs.getString(3);
        String instructions = rs.getString(4);

        ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
        ArrayList<String> tags = loadTagsForRecipe(recipeId, userName);

        Recipe recipe = new Recipe(name, description, instructions, ingredients, tags);
        favoriteRecipes.add(recipe);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return favoriteRecipes;
  }

  /**Adds a recipe to the favorites table.
   *
   * @param userName the user's username
   * @param recipeName the name of recipe to add
   * @return true if the recipe was added successfully, false otherwise
   */
  public boolean addRecipeToFavorites(String userName, String recipeName) {
    int userId = getUserId(userName);
    int recipeId = getRecipeId(recipeName);
    if (userId == -1 || recipeId == -1) {
      return false;
    }

    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (?, ?)"
        )
    ) {
      stmt.setInt(1, userId);
      stmt.setInt(2, recipeId);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }
  
  /**Removes a recipe from the favorites table.
   *
   * @param userName the user's username
   * @param recipeName the name of recipe to remove
   * @return true if the recipe was removed successfully, false otherwise
   */
  public boolean removeRecipeFromFavorites(String userName, String recipeName){
    int userId = getUserId(userName);
    int recipeId = getRecipeId(recipeName);

    if (userId == -1 || recipeId == -1) {
      return false;
    }

    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM favorites WHERE user_id = ? AND recipe_id = ?"
        )
    ) {
      stmt.setInt(1, userId);
      stmt.setInt(2, recipeId);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }


  

  /**
   * Close the connection with the database.
   */
  public void disconnect() {
    // disconnect from the database
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }
  public ArrayList<ShoppingList> loadShoppingListsFromDatabase(String username) {
    ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
    int userId = getUserId(username);

    // then get the shopping lists for the user
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT sl.shoppinglist_id, i.name, sl.list_quantity, i.measurementUnit, sl.week_number "
                + "FROM ShoppingList sl "
                + "JOIN ingredients i ON sl.ingredient_id = i.id "
                + "WHERE sl.user_id = ? "
                + "ORDER BY sl.week_number, sl.shoppinglist_id")) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();

      int currentWeekNumber = -1;
      ShoppingList currentShoppingList = null;
      while (rs.next()) {
        int weekNumber = rs.getInt(5);
        if (weekNumber != currentWeekNumber) {
          // found a new shopping list
          currentWeekNumber = weekNumber;
          currentShoppingList = new ShoppingList(currentWeekNumber, new ArrayList<>());
          shoppingLists.add(currentShoppingList);
        }
        
        // add ingredient to the current shopping list
        String ingredientName = rs.getString(2);
        float quantity = rs.getFloat(3);
        String measurementUnit = rs.getString(4);
        Ingredient ingredient = new Ingredient(ingredientName, quantity, measurementUnit);
        currentShoppingList.addIngredient(ingredient);
      }

      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return shoppingLists;
  }



  public void editRecipeInDatabase(int id, String recipeName, String description, String instructions, ArrayList<String[]> ingredients, ArrayList<String> tags, String userName) {
    try {
      // Update the recipe in the recipes table
      String query = "UPDATE recipes SET name = ?, description = ?, instructions = ? WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, recipeName);
        statement.setString(2, description);
        statement.setString(3, instructions);
        statement.setInt(4, id);
        statement.executeUpdate();
      }

      // Update the ingredients associated with the recipe
      for (String[] ingredient : ingredients) {
        String name = ingredient[0];
        String quantity = ingredient[1];
        String measurementUnit = ingredient[2];

        query = "UPDATE ingredients SET name = ?, quantity = ?, measurementUnit = ? WHERE recipe_id = ? AND name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
          statement.setString(1, name);
          statement.setString(2, quantity);
          statement.setString(3, measurementUnit);
          statement.setInt(4, id);
          statement.setString(5, name);
          statement.executeUpdate();
        }
      }
      // Get the user id
      int userId = getUserId(userName);
      // Update the tags associated with the recipe
      List<String> predefinedTags = Arrays.asList("vegan", "vegetarian", "lactose free",
            "gluten free", "starter", "main course", "dessert and sweets");
      for (String tag : tags) {
        int tagId = getTagId(tag);
        if (tagId == -1) {
          // If the tag doesn't exist, insert it into the tags table
          tagId = insertTag(tag);
        }
        // If the tag is a predefined tag, update it in the recipe_tags table,
        // otherwise update it in the PersonalTags table
        if (predefinedTags.contains(tag.toLowerCase())) {
          query = "UPDATE recipe_tags SET tag_id = ? WHERE recipe_id = ? AND tag_id = ?";
        } else {
        query = "UPDATE PersonalTags SET tag_id = ? WHERE user_id = ? AND recipe_id = ? AND tag_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
          statement.setInt(1, tagId);
          statement.setInt(2, id);
          statement.setInt(3, tagId);
          statement.executeUpdate();
        }
      }
    } catch (SQLException e) {
      System.out.println("Error while editing recipe in the database: " + e.getMessage());
    }
  }




  public void editIngredientQuantity(String username, String ingredientName, float newQuantity, int weekNumber) {
    int userId = getUserId(username);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE ShoppingList SET list_quantity = ? WHERE user_id = ? AND ingredient_name = ? AND week_number = ?")) {
      stmt.setFloat(1, newQuantity);
      stmt.setInt(2, userId);
      stmt.setString(3, ingredientName);
      stmt.setInt(4, weekNumber);
  
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void deleteIngredientFromShoppingList(String username, String ingredientName, int weekNumber) {
    int userId = getUserId(username);
  
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM ShoppingList WHERE user_id = ? AND ingredient_name = ? AND week_number = ?")) {
      stmt.setInt(1, userId);
      stmt.setString(2, ingredientName);
      stmt.setInt(3, weekNumber);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
  
  public void addRecipeToShoppingList(String username, int recipeId, int weekNumber) {
    try {
      int userId = getUserId(username);
      // Retrieve the ingredients associated with the recipe
      PreparedStatement getIngredientsStmt = connection.prepareStatement(
          "SELECT id, name, quantity FROM ingredients WHERE recipe_id = ?");
      getIngredientsStmt.setInt(1, recipeId);
      ResultSet rs = getIngredientsStmt.executeQuery();
  
      // Prepare the SQL statements
      PreparedStatement checkExistenceStmt = connection.prepareStatement(
          "SELECT list_quantity FROM ShoppingList WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement updateStmt = connection.prepareStatement(
          "UPDATE ShoppingList SET list_quantity = list_quantity + ? WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement insertStmt = connection.prepareStatement(
          "INSERT INTO ShoppingList (user_id, ingredient_id, ingredient_name, list_quantity, week_number) VALUES (?, ?, ?, ?, ?)");
  
      // For each ingredient, add a new entry in the ShoppingList or update an existing one
      while (rs.next()) {
        int ingredientId = rs.getInt(1);
        String ingredientName = rs.getString(2);
        float quantity = rs.getFloat(3);
  
        // Check if the ingredient already exists in the ShoppingList for the given week
        checkExistenceStmt.setInt(1, userId);
        checkExistenceStmt.setString(2, ingredientName);
        checkExistenceStmt.setInt(3, weekNumber);
        ResultSet rsExist = checkExistenceStmt.executeQuery();
  
        if (rsExist.next()) {  // The ingredient already exists in the ShoppingList, update it
          updateStmt.setFloat(1, quantity);
          updateStmt.setInt(2, userId);
          updateStmt.setString(3, ingredientName);
          updateStmt.setInt(4, weekNumber);
          updateStmt.executeUpdate();
        } else {  // The ingredient does not exist in the ShoppingList, insert it
          insertStmt.setInt(1, userId);
          insertStmt.setInt(2, ingredientId);
          insertStmt.setString(3, ingredientName);
          insertStmt.setFloat(4, quantity);
          insertStmt.setInt(5, weekNumber);
          insertStmt.executeUpdate();
        }
  
        rsExist.close();
      }
  
      rs.close();
      getIngredientsStmt.close();
      checkExistenceStmt.close();
      updateStmt.close();
      insertStmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
  


  /** Deletes a recipe from the shopping list of a user for a given week.
   * 
   * @param username
   * @param recipeId
   * @param weekNumber
   */
  public void deleteRecipeFromShoppingList(String username, int recipeId, int weekNumber) {
    try {
      int userId = getUserId(username);
  
      PreparedStatement getIngredientsStmt = connection.prepareStatement(
                "SELECT name, quantity FROM ingredients WHERE recipe_id = ?");
      getIngredientsStmt.setInt(1, recipeId);
      ResultSet rs = getIngredientsStmt.executeQuery();
  
      PreparedStatement checkExistenceStmt = connection.prepareStatement(
          "SELECT list_quantity FROM ShoppingList WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement updateStmt = connection.prepareStatement(
          "UPDATE ShoppingList SET list_quantity = list_quantity - ? WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement deleteStmt = connection.prepareStatement(
          "DELETE FROM ShoppingList WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
  
      while (rs.next()) {
          String ingredientName = rs.getString(1);
          float quantity = rs.getFloat(2);
  
          checkExistenceStmt.setInt(1, userId);
          checkExistenceStmt.setString(2, ingredientName);
          checkExistenceStmt.setInt(3, weekNumber);
          ResultSet rsExist = checkExistenceStmt.executeQuery();
  
          if (rsExist.next()) {
              float currentQuantity = rsExist.getFloat(1);
  
              if (currentQuantity - quantity <= 0) {
                  deleteStmt.setInt(1, userId);
                  deleteStmt.setString(2, ingredientName);
                  deleteStmt.setInt(3, weekNumber);
                  deleteStmt.executeUpdate();
              } else {
                  updateStmt.setFloat(1, quantity);
                  updateStmt.setInt(2, userId);
                  updateStmt.setString(3, ingredientName);
                  updateStmt.setInt(4, weekNumber);
                  updateStmt.executeUpdate();
              }
          }
  
          rsExist.close();
      }
  
      rs.close();
      getIngredientsStmt.close();
      checkExistenceStmt.close();
      updateStmt.close();
      deleteStmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
  
  



  public ArrayList<User> loadAllUsersFromDatabase() {
    return null;
  }
}




  

