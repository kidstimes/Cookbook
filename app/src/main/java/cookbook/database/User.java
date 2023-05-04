package cookbook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User class for database operations.
 * including register, login, delete user.
 */

public class User {
  private Connection connection;

  public User() {
    try {
      connection = DriverManager.getConnection(
          "jdbc:mysql://localhost/cookbook?user=root&password=12345678&useSSL=false");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // chek if username exists.
  public void checkifUserNameExists(String userName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ?")) {
      stmt.setString(1, userName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        System.out.println("Username already exists");
        // username already exists
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // register user method.
  public void registerUser(String userName, String password) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO users (username, password) VALUES (?, ?)")) {
      stmt.setString(1, userName);
      stmt.setString(2, password);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    checkifUserNameExists(userName);
    System.out.println("User registered");
    return;
  }

  // user login method
  public void userloginin(String userName, String password) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "SELECT * FROM users WHERE username = ? AND password = ?")) {
      stmt.setString(1, userName);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        System.out.println("User logged in");
        return;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("User not found");
    return;
  }

  // delete user method.
  public void deleteUser(String userName) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "DELETE FROM users WHERE username = ?")) {
      stmt.setString(1, userName);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("User deleted");
    return;
  }

  public void userAddRecipe(String recipeName, String recipeDescription, String recipeInstructions) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO recipes (name, description, instructions) VALUES (?, ?, ?)")) {
      stmt.setString(1, recipeName);
      stmt.setString(2, recipeDescription);
      stmt.setString(3, recipeInstructions);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Recipe added");
    return;
  }

  public void userAddIngredients(String ingredientName, String ingredientQuantity, String ingredientMeasurementUnit) {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO ingredients (name, quantity, measurementUnit) VALUES (?, ?)")) {
      stmt.setString(1, ingredientName);
      stmt.setString(2, ingredientQuantity);
      stmt.setString(3, ingredientMeasurementUnit);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Ingredient added");
    return;
  }

  // add personal tag method.
  // not showed in the database yet.
  public void addPersonalTag() {
    try (
        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO personal_tags (tag_name) VALUES (?)")) {
      stmt.setString(1, "test");
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Personal tag added");
    return;
  }

  // close connection method.
  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

}