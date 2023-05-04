package cookbook.database;

import cookbook.model.Recipe;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
  public boolean saveRecipeToDatabase(String[] recipe, ArrayList<String[]> ingredients, ArrayList<String> tags,
      String userName) {
    try {
      // Get the user id
      int userId = getUserId(userName);
      // Insert the recipe into the recipes table
      String query = "INSERT INTO recipes (name, description, instructions) VALUES (?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
  /**
   * Insert an ingredient into the ingredients table and associate it with a
   * recipe.
   */
  private void insertIngredient(int recipeId, String name, String quantity, String measurementUnit) {
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
        Recipe recipe = new Recipe(name, description, instructions, ingredients, tags);
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

  public void updateTagToDatabase(ArrayList<String> tags, String recipeName, String userName) {
    try {
      // Get the user and recipe IDs
      int userId = getUserId(userName);
      int recipeId = getRecipeId(recipeName);

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

  private int getRecipeId(String recipeName) throws SQLException {
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
    String query = "INSERT IGNORE INTO tags (name) VALUES (?)";
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
    String query = "INSERT IGNORE INTO PersonalTags (user_id, recipe_id, tag_id) VALUES (?, ?, ?)";
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
}
