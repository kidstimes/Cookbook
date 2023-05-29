package cookbook.database;

import cookbook.model.Comment;
import cookbook.model.Conversation;
import cookbook.model.Dinner;
import cookbook.model.HelpSection;
import cookbook.model.HelpSubsection;
import cookbook.model.Ingredient;
import cookbook.model.Message;
import cookbook.model.Recipe;
import cookbook.model.RecipeEditRecord;
import cookbook.model.ShoppingList;
import cookbook.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  /**
   * Get user id from the database based on username.
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
      LocalDate dateAdded = LocalDate.now();
      // Insert the recipe into the recipes table
      String query = "INSERT INTO recipes (name, description, instructions, user_id, date_added) "
          + "VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(
          query, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, recipe[0]);
        statement.setString(2, recipe[1]);
        statement.setString(3, recipe[2]);
        statement.setInt(4, userId);
        statement.setDate(5, Date.valueOf(dateAdded));
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
      String query = "INSERT INTO ingredients (recipe_id, name, "
          + "quantity, measurementUnit) VALUES (?, ?, ?, ?)";
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
   * Inserts a new ingredient into the database when editing a recipe.
   *
   * @param recipeId        the id of the recipe
   * @param name            the name of the ingredient
   * @param quantity        the quantity of the ingredient
   * @param measurementUnit the measurement unit of the ingredient
   */
  private void insertIngredient(int recipeId, String name,
                                float quantity, String measurementUnit) {
    String query = "INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit) "
        + "VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query,
        Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, name);
      statement.setInt(2, recipeId);
      statement.setFloat(3, quantity);
      statement.setString(4, measurementUnit);
      statement.executeUpdate();
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          generatedKeys.getInt(1);
        } 
      }
    } catch (SQLException e) {
      System.out.println("Error while inserting ingredient: " + e.getMessage());
    }
  }

  /**
   * Load recipes from the database and return arraylist of Recipe (from model)
   * objects.
   */
  public ArrayList<Recipe> loadAllRecipes(String username) {
    ArrayList<Recipe> recipes = new ArrayList<>();
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT r.id, r.name, r.description, r.instructions, r.date_added, u.username "
            + "FROM recipes r JOIN users u ON r.user_id = u.id")) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int recipeId = rs.getInt(1);
        String name = rs.getString(2);
        String description = rs.getString(3);
        String instructions = rs.getString(4);
        Date dateAdded = rs.getDate(5);

        String createrUsername = rs.getString(6);

        // load ingredients and tags
        ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
        ArrayList<String> tags = loadTagsForRecipe(recipeId, username);

        // load edit records
        ArrayList<RecipeEditRecord> editRecords = loadEditRecordsForRecipe(recipeId);

        // create the recipe objects with ingredients, tags, dateAdded and edit records
        Recipe recipe = new Recipe(recipeId, name, description, instructions,
            ingredients, tags, createrUsername, dateAdded, editRecords);
        recipes.add(recipe);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return recipes;
  }

  private ArrayList<RecipeEditRecord> loadEditRecordsForRecipe(int recipeId) {
    ArrayList<RecipeEditRecord> editRecords = new ArrayList<>();
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT u.username, reh.edit_date "
            + "FROM RecipeEditHistory reh JOIN users u ON reh.user_id = u.id " 
            + "WHERE reh.recipe_id = ?")) {
      stmt.setInt(1, recipeId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String editorUsername = rs.getString(1);
        LocalDate editDate = rs.getDate(2).toLocalDate();
        editRecords.add(new RecipeEditRecord(editorUsername, editDate));
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println("Error while loading recipe edit records: " + e.getMessage());
    }
    return editRecords;
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
      String query = "SELECT name FROM tags INNER JOIN recipe_tags ON "
          + "tags.id = recipe_tags.tag_id WHERE recipe_tags.recipe_id = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, recipeId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          tags.add(resultSet.getString("name"));
        }
      }
      // Fetch personal tags for the given username from PersonalTags table
      query = "SELECT name FROM tags INNER JOIN PersonalTags ON tags.id = PersonalTags.tag_id"
          + " WHERE PersonalTags.recipe_id = ? AND PersonalTags.user_id = ?";
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

  /**
   * Update the tags of a recipe in the database associated with a user.
   *
   * @param newTags  is an arraylist of strings of tags for a recipe
   * @param recipeId is the ID of the recipe
   * @param userName is the name of the user
   */
  public void updateTagToDatabase(ArrayList<String> newTags, int recipeId, String userName) {
    try {
      int userId = getUserId(userName);
      ArrayList<String> oldTags = loadTagsForRecipe(recipeId, userName);
      // Determine which tags have been removed and which have been added
      List<String> removedTags = oldTags.stream().filter(
          tag -> !newTags.contains(tag)).toList();
      List<String> addedTags = newTags.stream().filter(
          tag -> !oldTags.contains(tag)).toList();
      List<String> predefinedTags = Arrays.asList("vegan", "vegetarian", "lactose free",
          "gluten free", "starter", "main course", "dessert and sweets");
      // Delete removed tags from the database
      for (String tag : removedTags) {
        int tagId = getTagId(tag);
        if (tagId != -1) {
          if (predefinedTags.contains(tag.toLowerCase())) {
            deleteRecipeTag(recipeId, tagId);
          }
          deletePersonalTag(userId, recipeId, tagId);
        }
      }
      // Add new tags to the database
      for (String tag : addedTags) {
        int tagId = getTagId(tag);
        if (tagId == -1) {
          // If the tag doesn't exist, insert it into the tags table
          tagId = insertTag(tag);
        }
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

  /**
   * Delete a public tag for a recipefrom the database.
   *
   * @param recipeId is the ID of the recipe
   * @param tagId    is the ID of the tag
   */
  private void deleteRecipeTag(int recipeId, int tagId) {
    String query = "DELETE FROM recipe_tags WHERE recipe_id = ? AND tag_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, recipeId);
      statement.setInt(2, tagId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting recipe tag: " + e.getMessage());
    }
  }

  /**
   * Get the recipe id from the database by recipe name.
   *
   * @param recipeName is the name of the recipe
   * @return the id of the recipe
   */
  public int getRecipeId(String recipeName) {
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

  // Get the tag id from the database
  private int getTagId(String tagName) {
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

  /**
   * Insert a tag into the database tags table and return the tag id.
   *
   * @param tagName name of the tag
   * @return the id of the tag
   * @throws SQLException if there is an error while inserting the tag
   */
  private int insertTag(String tagName) throws SQLException {
    String query = "INSERT INTO tags (name) VALUES (?) ON DUPLICATE KEY UPDATE name = name";
    try (PreparedStatement statement = connection
        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, tagName);
      statement.executeUpdate();
      ResultSet generatedKeys = statement.getGeneratedKeys();
      return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
    }
  }

  /**
   * Delete a specific personal tag of a user for a recipe from the database.
   *
   * @param userId   id of the user
   * @param recipeId id of the recipe
   * @param tagId    id of the tag
   */
  private void deletePersonalTag(int userId, int recipeId, int tagId) {
    String query = "DELETE FROM PersonalTags WHERE user_id = ? AND recipe_id = ? AND tag_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.setInt(3, tagId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting personal tag: " + e.getMessage());
    }
  }

  /**
   * Insert a personal tag of a user for a recipe into the database.
   *
   * @param userId   id of the user
   * @param recipeId id of the recipe
   * @param tagId    id of the tag
   */
  private void insertPersonalTag(int userId, int recipeId, int tagId) {
    String query = "INSERT INTO PersonalTags (user_id, recipe_id, tag_id) "
        + "VALUES (?, ?, ?) ON DUPLICATE KEY "
        + "UPDATE user_id = user_id, recipe_id = recipe_id, tag_id = tag_id";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.setInt(3, tagId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while inserting personal tag: " + e.getMessage());
    }
  }

  /**
   * Insert a recipe tag into the database for a recipe.
   *
   * @param recipeId id of the recipe
   * @param tagId    id of the tag
   */
  private void insertRecipeTag(int recipeId, int tagId) {
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
      String query = "SELECT DISTINCT tags.name FROM tags INNER JOIN PersonalTags "
          + "ON tags.id = PersonalTags.tag_id WHERE PersonalTags.user_id = ?";
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
            "SELECT name, description, instructions, user_id FROM recipes WHERE id = ?")) {
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

  private Recipe getRecipeById(int recipeId, ArrayList<Recipe> recipes) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == recipeId) {
        return recipe;
      }
    }
    return null;
  }

  /**
   * Save weekly dinner list to database.
   *
   * @param weeklyDinnerList is the list of Dinner objects to save
   * @param username         is the username of the user
   * @return true if the save was successful, false otherwise
   */
  public boolean saveWeeklyDinnerToDatabase(ArrayList<Dinner> weeklyDinnerList,
      String username) {
    boolean saveSuccessful = false;
    try {
      String sql = "INSERT INTO WeeklyDinner (user_id, recipe_id, dinner_date) "
          + "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE dinner_date = VALUES(dinner_date)";
      PreparedStatement statement = connection.prepareStatement(sql);
      int userId = getUserId(username);
      for (Dinner dinner : weeklyDinnerList) {
        LocalDate dinnerDate = dinner.getDate();
        for (Recipe recipe : dinner.getRecipes()) {
          int recipeId = getRecipeId(recipe.getName());
          statement.setInt(1, userId);
          statement.setInt(2, recipeId);
          statement.setDate(3, java.sql.Date.valueOf(dinnerDate));
          statement.executeUpdate();
        }
      }
      statement.close();
      saveSuccessful = true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return saveSuccessful;
  }

  /**
   * Load weekly dinner list from database.
   *
   * @param username is the username of the user
   * @return the list of weekly Dinner objects loaded from the database
   */
  public ArrayList<Dinner> loadWeeklyDinnerListFromDatabase(String username) {
    ArrayList<Dinner> weeklyDinnerList = new ArrayList<>();
    try {
      int userId = getUserId(username);
      String sql = "SELECT recipe_id, dinner_date FROM WeeklyDinner WHERE user_id = " + userId;
      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(sql);
      while (result.next()) {
        int recipeId = result.getInt("recipe_id");
        LocalDate dinnerDate = result.getDate("dinner_date").toLocalDate();
        Recipe recipe = getRecipeById(recipeId);
        Dinner dinner = null;
        for (Dinner d : weeklyDinnerList) {
          if (d.getDate().equals(dinnerDate)) {
            dinner = d;
            break;
          }
        }
        if (dinner == null) {
          dinner = new Dinner(dinnerDate, recipe);
          weeklyDinnerList.add(dinner);
        } else {
          dinner.addRecipe(recipe);
        }
      }
      result.close();
      statement.close();
    } catch (SQLException e) {
      System.out.println("Error loading weekly dinner list from database: " + e.getMessage());
    }
    return weeklyDinnerList;
  }

  /**
   * Remove a recipe from the weekly dinner list in the database.
   *
   * @param username   is the username of the user
   * @param dayDate    is the date of the dinner
   * @param recipeName is the name of the recipe to remove
   */
  public void removeRecipeFromWeeklyDinnerInDatabase(String username,
                                                     LocalDate dayDate, String recipeName) {
    try {
      // Get the user ID from the username
      int userId = getUserId(username);
      // Get the recipe ID from the recipe name
      int recipeId = getRecipeId(recipeName);
      // Prepare a SQL statement to delete the record from the WeeklyDinner table
      String sql = "DELETE FROM WeeklyDinner "
          + "WHERE user_id = ? AND recipe_id = ? AND dinner_date = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, userId);
      statement.setInt(2, recipeId);
      statement.setDate(3, java.sql.Date.valueOf(dayDate));

      statement.close();
    } catch (SQLException e) {
      System.out.println("Error removing recipe from weekly dinner in database: " + e.getMessage());
    }
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
                + "WHERE f.user_id = ?")) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int recipeId = rs.getInt(1);
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

  /**
   * Adds a recipe to the favorites table.
   *
   * @param userName   the user's username
   * @param recipeName the name of recipe to add
   */
  public void addRecipeToFavorites(String userName, String recipeName) {
    int userId = getUserId(userName);
    int recipeId = getRecipeId(recipeName);
    if (userId == -1 || recipeId == -1) {
      return;
    }
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (?, ?)")) {
      stmt.setInt(1, userId);
      stmt.setInt(2, recipeId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Removes a recipe from the favorites table.
   *
   * @param userName   the user's username
   * @param recipeName the name of recipe to remove
   */
  public void removeRecipeFromFavorites(String userName, String recipeName) {
    int userId = getUserId(userName);
    int recipeId = getRecipeId(recipeName);
    if (userId == -1 || recipeId == -1) {
      return;
    }
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM favorites WHERE user_id = ? AND recipe_id = ?")) {
      stmt.setInt(1, userId);
      stmt.setInt(2, recipeId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Update the ingredient for a recipe the given user when editing a recipe.
   *
   * @param ingredientId    the id of the ingredient to update
   * @param name            the name of the ingredient
   * @param quantity        the quantity of the ingredient
   * @param measurementUnit the measurement unit of the ingredient
   */
  private void updateIngredient(int ingredientId,
      String name, float quantity, String measurementUnit) {
    String query = "UPDATE ingredients SET name = ?, quantity = ?,"
        + " measurementUnit = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, name);
      statement.setFloat(2, quantity);
      statement.setString(3, measurementUnit);
      statement.setInt(4, ingredientId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while updating ingredient: " + e.getMessage());
    }
  }

  /**
   * Delete the ingredient for a recipe the given user when editing a recipe.
   *
   * @param ingredientId the id of the ingredient to delete
   */
  private void deleteIngredient(int ingredientId) {
    String query = "DELETE FROM ingredients WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, ingredientId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting ingredient: " + e.getMessage());
    }
  }


  /**
   * Load the ingredients of recipes from the database and
   * return them in an arraylist of string arrays.
   */
  private ArrayList<String[]> loadIngredientsWithIdForRecipe(int recipeId) {
    ArrayList<String[]> ingredients = new ArrayList<>();
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT i.name, i.quantity, i.measurementUnit, i.id "
                + "FROM ingredients i "
                + "WHERE i.recipe_id = ?")) {
      stmt.setInt(1, recipeId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String ingredientName = rs.getString(1);
        String quantity = rs.getString(2);
        String measurementUnit = rs.getString(3);
        String ingredientId = rs.getString(4);
        ingredients.add(new String[] { ingredientName, quantity, measurementUnit, ingredientId });
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return ingredients;
  }

  /**
   * Edit a recipe in the database with the given parameters.
   *
   * @param recipeId        the id of the recipe to edit
   * @param newRecipeName   the new name of the recipe
   * @param newDescription  the new description of the recipe
   * @param newInstructions the new instructions of the recipe
   * @param newIngredients  the new ingredients of the recipe
   */
  public void editRecipeInDatabase(int recipeId, String newRecipeName, String newDescription,
      String newInstructions, ArrayList<String[]> newIngredients, String userName) {
    int userId = getUserId(userName);
    try {
      // Update recipe name, description and instructions
      String updateRecipeQuery = "UPDATE recipes "
          + "SET name = ?, description = ?, instructions = ? WHERE id = ?";
      try (PreparedStatement updateRecipeStmt = connection.prepareStatement(updateRecipeQuery)) {
        updateRecipeStmt.setString(1, newRecipeName);
        updateRecipeStmt.setString(2, newDescription);
        updateRecipeStmt.setString(3, newInstructions);
        updateRecipeStmt.setInt(4, recipeId);
        updateRecipeStmt.executeUpdate();
      }
      // Get current ingredients for the recipe
      ArrayList<String[]> currentIngredients = loadIngredientsWithIdForRecipe(recipeId);

      // Update ingredients
      for (String[] currentIngredient : currentIngredients) {
        boolean found = false;
        for (String[] newIngredient : newIngredients) {
          if (currentIngredient[0].equals(newIngredient[0])) {
            found = true;
            // Update ingredient if quantity or measurementUnit has changed
            if (!currentIngredient[1].equals(newIngredient[1])
                || !currentIngredient[2].equals(newIngredient[2])) {
              int ingredientId = Integer.parseInt(currentIngredient[3]);
              updateIngredient(ingredientId, newIngredient[0],
                  Float.parseFloat(newIngredient[1]), newIngredient[2]);
            }
            break;
          }
        }
        // Delete ingredient if not found in newIngredients
        if (!found) {
          int ingredientId = Integer.parseInt(currentIngredient[3]);
          deleteIngredient(ingredientId);
        }
      }
      // Insert new ingredients
      for (String[] newIngredient : newIngredients) {
        boolean found = false;
        for (String[] currentIngredient : currentIngredients) {
          if (currentIngredient[0].equals(newIngredient[0])) {
            found = true;
            break;
          }
        }
        // Insert ingredient if not found in currentIngredients
        if (!found) {
          insertIngredient(recipeId, newIngredient[0],
              Float.parseFloat(newIngredient[1]), newIngredient[2]);
        }
      }
      String insertHistoryQuery = "INSERT INTO recipeEditHistory (recipe_id, user_id, edit_date) "
          + "VALUES (?, ?, ?)";
      try (PreparedStatement insertHistoryStmt = connection.prepareStatement(insertHistoryQuery)) {
        insertHistoryStmt.setInt(1, recipeId);
        insertHistoryStmt.setInt(2, userId);
        insertHistoryStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        insertHistoryStmt.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Error while editing recipe in database: " + e.getMessage());
    }

  }

  /**
   * Load the shopping lists of a user with the given username from the database.
   *
   * @param username the given username
   * @return the shopping lists of the user
   */
  public ArrayList<ShoppingList> loadShoppingListsFromDatabase(String username) {
    ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
    int userId = getUserId(username);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT shoppinglist_id, ingredient_name, list_quantity,"
                + " measurementUnit, week_number "
                + "FROM ShoppingList "
                + "WHERE user_id = ? "
                + "ORDER BY week_number, shoppinglist_id")) {
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
        if (currentShoppingList != null) {
          currentShoppingList.addIngredient(ingredient);
        }
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return shoppingLists;
  }

  /**
   * Edit the quantity of an ingredient in a user's shopping list.
   *
   * @param username       the username of the user
   * @param ingredientName the name of the ingredient in the shopping list
   * @param newQuantity    the updated quantity for the ingredient
   * @param weekNumber     the week number of the shopping list
   */
  public void editIngredientQuantityInShoppingList(String username,
      String ingredientName, float newQuantity, int weekNumber) {
    int userId = getUserId(username);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE ShoppingList SET list_quantity = ? "
                + "WHERE user_id = ? AND ingredient_name = ? AND week_number = ?")) {
      stmt.setFloat(1, newQuantity);
      stmt.setInt(2, userId);
      stmt.setString(3, ingredientName);
      stmt.setInt(4, weekNumber);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete an ingredient from the user's shopping list.
   *
   * @param username       the username of the user
   * @param ingredientName the name of the ingredient to delete
   * @param weekNumber     the week number of shopping list
   */
  public void deleteIngredientFromShoppingList(String username,
      String ingredientName, int weekNumber) {
    int userId = getUserId(username);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM ShoppingList WHERE user_id = ? "
                + "AND ingredient_name = ? AND week_number = ?")) {
      stmt.setInt(1, userId);
      stmt.setString(2, ingredientName);
      stmt.setInt(3, weekNumber);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete all the records in the shopping list for the given user.
   *
   * @param username the username of the user who owns the shopping list
   */
  public void clearUserShoppingList(String username) {
    try {
      // Get the userId from the username
      int userId = getUserId(username);
      // Now delete all the records in the shopping list associated with that user
      PreparedStatement deleteStmt = connection
          .prepareStatement("DELETE FROM ShoppingList WHERE user_id = ?");
      deleteStmt.setInt(1, userId);
      deleteStmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Add all ingredients of a recipe to the shopping list for the given user.
   *
   * @param username   is the username of the user
   * @param recipeId   is the id of the recipe to add
   * @param weekNumber is the week number to add the recipe to
   */
  public void addRecipeToShoppingList(String username, int recipeId, int weekNumber) {
    try {
      int userId = getUserId(username);
      PreparedStatement getIngredientsStmt = connection.prepareStatement(
          "SELECT name, quantity, measurementUnit FROM ingredients WHERE recipe_id = ?");
      getIngredientsStmt.setInt(1, recipeId);
      ResultSet rs = getIngredientsStmt.executeQuery();
      PreparedStatement checkStmt = connection.prepareStatement(
          "SELECT list_quantity FROM ShoppingList "
              + "WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement updateStmt = connection.prepareStatement(
          "UPDATE ShoppingList SET list_quantity = list_quantity + ? "
              + "WHERE user_id = ? AND ingredient_name = ? AND week_number = ?");
      PreparedStatement insertStmt = connection.prepareStatement(
          "INSERT INTO ShoppingList (user_id, ingredient_name, list_quantity,"
              + " measurementUnit, week_number) VALUES (?, ?, ?, ?, ?)");
      while (rs.next()) {
        String ingredientName = rs.getString(1);
        float quantity = rs.getFloat(2);
        String measurementUnit = rs.getString(3);
        // Check if the ingredient already exists in the ShoppingList
        checkStmt.setInt(1, userId);
        checkStmt.setString(2, ingredientName);
        checkStmt.setInt(3, weekNumber);
        ResultSet checkRs = checkStmt.executeQuery();
        if (checkRs.next()) {
          // The ingredient already exists in the ShoppingList, update it
          updateStmt.setFloat(1, quantity);
          updateStmt.setInt(2, userId);
          updateStmt.setString(3, ingredientName);
          updateStmt.setInt(4, weekNumber);
          updateStmt.executeUpdate();
        } else {
          // The ingredient does not exist in the ShoppingList, insert it
          insertStmt.setInt(1, userId);
          insertStmt.setString(2, ingredientName);
          insertStmt.setFloat(3, quantity);
          insertStmt.setString(4, measurementUnit);
          insertStmt.setInt(5, weekNumber);
          insertStmt.executeUpdate();
        }
        checkRs.close();
      }
      rs.close();
      getIngredientsStmt.close();
      checkStmt.close();
      updateStmt.close();
      insertStmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Load all users from the database.
   *
   * @return ArrayList of all users
   */
  public ArrayList<User> loadAllUsersFromDatabase() {
    ArrayList<User> users = new ArrayList<>();
    // Retrieve all users from the database with id, username, and displayname
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT id, username, displayname FROM Users")) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt(1);
        String username = rs.getString(2);
        String displayName = rs.getString(3);
        User user = new User(id, username, displayName);
        users.add(user);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  /**
   * Get a list of the users that are not currently logged in.
   *
   * @param username the username of the logged in user
   * @return an arraylist with the logged out users
   */
  public ArrayList<User> loadLoggedOutUsers(String username) {
    ArrayList<User> users = new ArrayList<>();
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT id, username, displayname FROM Users "
            + "WHERE username != ? AND displayname != ?")) {
      stmt.setString(1, username);
      stmt.setString(2, "Deleted User");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt(1);
        String fetchedUsername = rs.getString(2);
        String displayName = rs.getString(3);
        User user = new User(id, fetchedUsername, displayName);
        users.add(user);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  /**
   * Edit user username and display name in the database.
   */
  public void editUser(int userId, String newUsername, String newDisplayName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE Users SET username = ?, displayname = ? WHERE id = ?")) {
      stmt.setString(1, newUsername);
      stmt.setString(2, newDisplayName);
      stmt.setInt(3, userId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Edit user password in the database.
   *
   * @param userId      is the id of the user
   * @param newPassword is the new password
   */
  public void editUserPassword(int userId, String newPassword) {
    String hashedPassword = hashPassword(newPassword);
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE Users SET password_hash = ? WHERE id = ?")) {
      stmt.setString(1, hashedPassword);
      stmt.setInt(2, userId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete user from the database.
   */
  public void deleteUser(int userId) {
    String hashedPassword = hashPassword("del");
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE Users SET displayName = ?, password_hash = ? WHERE id = ?")) {
      stmt.setString(1, "Deleted User");
      stmt.setString(2, hashedPassword);
      stmt.setInt(3, userId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    String[] tables = { "WeeklyDinner", "ShoppingList", "PersonalTags", "favorites" };
    for (String table : tables) {
      String query = String.format("DELETE FROM %s WHERE user_id = ?", table);
      try (PreparedStatement stmt2 = connection.prepareStatement(query)) {
        stmt2.setInt(1, userId);
        stmt2.executeUpdate();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Check if a user display name is deleted user from the database with userName.
   * 
   */
  public boolean isDeletedUserInDatabase(String userName) {
    boolean isDeleted = false;
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT displayname FROM Users WHERE username = ?")) {
      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String displayName = rs.getString(1);
        if (displayName.equals("Deleted User")) {
          isDeleted = true;
        }
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return isDeleted;
  }

  /**
   * Change the display name of a user.
   *
   * @param username       is the username of the user
   * @param newDisplayName is the new display name
   * @return true if the display name was changed, false otherwise
   */
  public boolean changeDisplayNameForUserInDatabase(String username, String newDisplayName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE Users SET displayname = ? WHERE username = ?")) {
      stmt.setString(1, newDisplayName);
      stmt.setString(2, username);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
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

  /**
   * Check if password is correct for user in database.
   *
   * @param username is the username of the user
   * @param password is the password to check
   * @return true if the password is correct, false otherwise
   */
  public boolean checkPasswordForUserInDatabase(String username, String password) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT password_hash FROM Users WHERE username = ?")) {
      stmt.setString(1, username);
      // hash the password
      String hashedPassword = hashPassword(password);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String passwordFromDatabase = rs.getString(1);
        return passwordFromDatabase.equals(hashedPassword);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * Change the password for a user in the database.
   *
   * @param username    is the username of the user
   * @param newPassword is the new password
   * @return true if the password was changed, false otherwise
   */
  public boolean changePasswordForUserInDatabase(String username, String newPassword) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "UPDATE Users SET password_hash = ? WHERE username = ?")) {
      stmt.setString(1, hashPassword(newPassword));
      stmt.setString(2, username);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * Check if the username exists in the database, except for the current user.
   */
  public boolean checkIfUserNameExistsExceptSelf(String userName, int selfId) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ? AND id != ?")) {
      stmt.setString(1, userName);
      stmt.setInt(2, selfId);
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
   * Add a comment to a recipe by a user to the database.
   *
   *
   * @param recipeName the name of the recipe
   * @param userId   the unique id of the user
   * @param text     the text of the comment
   */
  public void addComment(String recipeName, int userId, String text) {
    int recipeId = getRecipeId(recipeName);
    String sql = "INSERT INTO comments (text, recipe_id, user_id) VALUES (?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, text);
      pstmt.setInt(2, recipeId);
      pstmt.setInt(3, userId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Update a comment.
   *
   * @param commentId the id of the comment
   * @param text      the new text of the comment
   */
  public void updateComment(int commentId, String text) {
    String sql = "UPDATE comments SET text = ? WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, text);
      pstmt.setInt(2, commentId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete a comment.
   *
   * @param commentId the id of the comment
   */
  public void deleteComment(int commentId) {
    String sql = "DELETE FROM comments WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, commentId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Get all comments for a recipe.
   *
   * @param recipeId the id of the recipe
   * @return the list of comments
   */
  public ArrayList<Comment> getComments(int recipeId) {
    ArrayList<Comment> comments = new ArrayList<>();
    String sql = "SELECT c.id, c.text, c.recipe_id, c.user_id, u.displayname FROM comments c "
        + "INNER JOIN users u ON c.user_id = u.id WHERE c.recipe_id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, recipeId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Comment comment = new Comment(
            rs.getInt("id"),
            rs.getString("text"),
            rs.getInt("recipe_id"),
            rs.getInt("user_id"),
            rs.getString("displayname"));
        comments.add(comment);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return comments;
  }



  /**
   * Add a sent message to the database.
   *
   * @param username the username of the sender
   * @param selectedUserName the username of the receiver
   * @param recipeName the name of the recipe
   * @param message the text of the message
   * @return true if sent, otherwise false
   */
  public boolean sendMessageToUser(String username, String selectedUserName,
      String recipeName, String message) {
    int recipeId = getRecipeId(recipeName);
    int senderId = getUserId(username);
    int receiverId = getUserId(selectedUserName);
    String sql = "INSERT INTO messages (text, sender_id, recipient_id, recipe_id, "
        + "is_read, send_date) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, message);
      pstmt.setInt(2, senderId);
      pstmt.setInt(3, receiverId);
      pstmt.setInt(4, recipeId);
      pstmt.setBoolean(5, false);
      pstmt.setObject(6, LocalDateTime.now());

      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * Load the received messages of a user from the database.
   *
   * @param receiverUsername the username of the receiver
   * @param recipes the recipes of the cookbook
   * @return the received messages of the user
   */
  public ArrayList<Message> loadReceivedMessagesFromDatabase(String receiverUsername,
      ArrayList<Recipe> recipes) {
    int userId = getUserId(receiverUsername);
    ArrayList<Message> messages = new ArrayList<>();

    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT m.id, m.text, sender.username, receiver.username, m.is_read, m.send_date, r.* "
            + "FROM messages m"
            + " JOIN users sender ON m.sender_id = sender.id"
            + " JOIN users receiver ON m.recipient_id = receiver.id"
            + " LEFT JOIN recipes r ON m.recipe_id = r.id"
            + " WHERE m.recipient_id = ?")) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int messageId = rs.getInt(1);
        String text = rs.getString(2);
        String senderName = rs.getString(3);
        String receiverName = rs.getString(4);
        boolean isRead = rs.getBoolean(5);
        LocalDateTime sendDateTime = rs.getObject(6, LocalDateTime.class);
        int recipeIdColumnIndex = 7;
        Recipe recipe = null;
        if (rs.getObject(recipeIdColumnIndex) != null) {
          int recipeId = rs.getInt(recipeIdColumnIndex);
          recipe = getRecipeById(recipeId, recipes);
        }
        Message message = new Message(messageId, recipe, text,
            senderName, receiverName, isRead, sendDateTime);
        messages.add(message);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return messages;
  }

  /**
   * Update a message to read in the database.
   *
   * @param messageId the unique id of the message
   */
  public void updateMessageIsRead(int messageId) {
    String sql = "UPDATE messages SET is_read = ? WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setBoolean(1, true);
      pstmt.setInt(2, messageId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Add a reply message to the database.
   *
   * @param senderUsername the username of the sender
   * @param receiverUsername the username of the receiver
   * @param text the text of the reply message
   * @return true if sent, otherwise false
   */
  public boolean replyMessage(String senderUsername, String receiverUsername, String text) {
    int senderId = getUserId(senderUsername);
    int receiverId = getUserId(receiverUsername);
    String sql = "INSERT INTO messages (text, sender_id, recipient_id, is_read, send_date) "
        + "VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, text);
      pstmt.setInt(2, senderId);
      pstmt.setInt(3, receiverId);
      pstmt.setBoolean(4, false);
      pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * Load the conversations of a user from the database.
   *
   * @param username the username of the user
   * @param recipes the recipes of the cookbook
   * @return an arraylist with the conversations of the user
   */
  public ArrayList<Conversation> loadConversationsFromDatabase(String username,
      ArrayList<Recipe> recipes) {
    int userId = getUserId(username);
    Map<String, Conversation> conversationMap = new HashMap<>();

    loadMessagesFromDatabase(userId, "sender_id", conversationMap, recipes);
    loadMessagesFromDatabase(userId, "recipient_id", conversationMap, recipes);

    return new ArrayList<>(conversationMap.values());
  }

  private void loadMessagesFromDatabase(int userId, String field, Map<String,
      Conversation> conversationMap, ArrayList<Recipe> recipes) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT m.id, m.text, sender.username, receiver.username, m.is_read, m.send_date, r.* "
            + "FROM messages m"
            + " JOIN users sender ON m.sender_id = sender.id"
            + " JOIN users receiver ON m.recipient_id = receiver.id"
            + " LEFT JOIN recipes r ON m.recipe_id = r.id"
            + " WHERE m." + field + " = ? ORDER BY m.send_date ASC")) {
      // added ORDER BY clause to sort by send date
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int messageId = rs.getInt(1);
        String text = rs.getString(2);
        String senderName = rs.getString(3);
        String receiverName = rs.getString(4);
        boolean isRead = rs.getBoolean(5);
        Timestamp sendDate = rs.getTimestamp(6); // Changed from LocalDate to Timestamp
        int recipeIdColumnIndex = 7;
        Recipe recipe = null;
        if (rs.getObject(recipeIdColumnIndex) != null) {
          int recipeId = rs.getInt(recipeIdColumnIndex);
          recipe = getRecipeById(recipeId, recipes);
        }

        Message message = new Message(messageId, recipe, text, senderName, receiverName, isRead,
            sendDate.toLocalDateTime());

        String otherUsername = field.equals("sender_id") ? receiverName : senderName;
        Conversation conversation = conversationMap.getOrDefault(otherUsername,
            new Conversation(otherUsername));
        conversation.addMessage(message);
        conversationMap.put(otherUsername, conversation);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Load the latest message the user has sent.
   *
   * @param username the username of the sender
   * @param recipes the recipes of the cookbook
   * @return the message the user last sent
   */
  public Message getLatestMessageFromUserAsSender(String username, ArrayList<Recipe> recipes) {
    int userId = getUserId(username);
    String sql = "SELECT m.id, m.text, sender.username, receiver.username, "
        + "m.is_read, m.send_date, r.* FROM messages m"
        + " JOIN users sender ON m.sender_id = sender.id"
        + " JOIN users receiver ON m.recipient_id = receiver.id"
        + " LEFT JOIN recipes r ON m.recipe_id = r.id"
        + " WHERE m.sender_id = ? ORDER BY m.send_date DESC LIMIT 1";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        int messageId = rs.getInt(1);
        String text = rs.getString(2);
        String senderName = rs.getString(3);
        String receiverName = rs.getString(4);
        boolean isRead = rs.getBoolean(5);
        LocalDateTime sendDateTime = rs.getObject(6, LocalDateTime.class);
        int recipeIdColumnIndex = 7;
        Recipe recipe = null;
        if (rs.getObject(recipeIdColumnIndex) != null) {
          int recipeId = rs.getInt(recipeIdColumnIndex);
          recipe = getRecipeById(recipeId, recipes);
        }
        return new Message(messageId, recipe, text, senderName, receiverName, isRead, sendDateTime);
      }
      rs.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   *  Load the help sections of the cookbook.
   *
   * @return an arraylist with the sections of the help system
   */
  public ArrayList<HelpSection> getHelpSections() {
    ArrayList<HelpSection> helpSections = new ArrayList<>();
    String sql = "SELECT * FROM HelpSection";
    try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        ArrayList<HelpSubsection> subsections = getHelpSubsections(id);
        helpSections.add(new HelpSection(id, title, subsections));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return helpSections;
  }

  /**
   * Load the subsections of the given section of the help system.
   *
   * @param sectionId the id of the section
   * @return an arraylist with the subsections
   */
  public ArrayList<HelpSubsection> getHelpSubsections(int sectionId) {
    ArrayList<HelpSubsection> helpSubsections = new ArrayList<>();
    String sql = "SELECT * FROM HelpSubsection WHERE section_id = ?";
    try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
      stmt.setInt(1, sectionId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String text = rs.getString("text");
        helpSubsections.add(new HelpSubsection(id, title, text));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return helpSubsections;
  }

  /**
   * Delete the user used in the unit tests from the database.
   */
  public void deleteTestUser() {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM users WHERE username = ?")) {
      stmt.setString(1, "testUserName");
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
