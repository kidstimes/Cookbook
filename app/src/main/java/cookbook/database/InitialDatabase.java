package cookbook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * initial database class for database operations.
 * including register, login, delete user.
 *including create schema if not exists, create tables if not exists.
  */

public class InitialDatabase {

    private Connection connection;

    public InitialDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/cookbook?user=root&password=12345678&useSSL=false");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            createSchemaIfNotExists();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Check if the database schema cookbook exists.
     * If it does not exist, create the schema and necessary tables.
     */
    private void createSchemaIfNotExists() {
        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE SCHEMA IF NOT EXISTS cookbook;"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS recipes (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "instructions TEXT NOT NULL, " +
                    "PRIMARY KEY (id)" +
                ")"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ingredients (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "quantity VARCHAR(255) NOT NULL, " +
                    "measurementUnit VARCHAR(255) NOT NULL, " +
                    "recipe_id INT NOT NULL, " +
                    "PRIMARY KEY (id), " +
                    "FOREIGN KEY (recipe_id) REFERENCES recipes(id)" +
                ")"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS tags (" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (id)" +
                ")"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS recipe_tags (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "recipe_id INT NOT NULL, " +
                    "tag_id INT NOT NULL, " +
                    "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE" +
                ")"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS comments (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "text TEXT NOT NULL, " +
                    "recipe_id INT NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")"
            )
        ) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try (
            PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS messages (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " + 
                    "text TEXT NOT NULL, " +
                    "sender_id INT NOT NULL, " +
                    "recipient_id INT NOT NULL, " +
                    "recipe_id INT, " +
                    "FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE SET NULL" +
                    ")"
            )
            ) {
              stmt.execute();
          } catch (SQLException e) {
              System.out.println(e.getMessage());
          }
                    
        try (
            PreparedStatement stmt = connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS WeekMenuRecipe (" +
            "weekmenu_id INT NOT NULL, " +
            "recipe_id INT NOT NULL, " +
            "day_of_week VARCHAR(10) NOT NULL, " +
            "PRIMARY KEY (weekmenu_id, recipe_id), " +
            "FOREIGN KEY (weekmenu_id) REFERENCES users(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE" +
            ")"
            )
              ) {
                stmt.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
                    
        try (
            PreparedStatement stmt = connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS ShoppingList (" +
            "shoppinglist_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "user_id INT NOT NULL, " +
            "weekmenu_id INT NOT NULL, " +
            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (weekmenu_id) REFERENCES WeekMenuRecipe(weekmenu_id) ON DELETE CASCADE" +
            ")"
            )
          ) {
              stmt.execute();
          } catch (SQLException e) {
                System.out.println(e.getMessage());

          }
        }

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
