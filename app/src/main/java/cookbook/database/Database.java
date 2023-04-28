package cookbook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import cookbook.model.Recipe;

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
            connection = DriverManager.getConnection("jdbc:mysql://localhost/cookbook?user=root&password=12345678&useSSL=false");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        loadAllRecipes();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    /**
     * Load recipes from the database and return arraylist of Recipe (from model) objects.
     */
    public ArrayList<Recipe> loadAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        
        try (
            PreparedStatement stmt = connection.prepareStatement("SELECT r.id, r.name, r.description, r.instructions " +
                    "FROM recipes r")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer recipeId = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String instructions = rs.getString(4);

                // load ingredients and tags
                ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
                ArrayList<String> tags = loadTagsForRecipe(name);

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
     * Load the ingredients of recipes from the database and return them in an arraylist of string arrays.
     */
    private ArrayList<String[]> loadIngredientsForRecipe(int recipeId) {
        ArrayList<String[]> ingredients = new ArrayList<>();
    
        try (
            PreparedStatement stmt = connection.prepareStatement("SELECT i.name, i.quantity, i.measurementUnit " +
                    "FROM ingredients i " +
                    "WHERE i.recipe_id = ?")) {
            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String ingredientName = rs.getString(1);
                String quantity = rs.getString(2);
                String measurementUnit = rs.getString(3);
    
                ingredients.add(new String[]{ingredientName, quantity, measurementUnit});
            }
    
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return ingredients;
    }
    
    /**
     * Load the tags of recipes from the database and return them in an arraylist of String Objects.
     */
    private ArrayList<String> loadTagsForRecipe(String recipeName) {
        ArrayList<String> tags = new ArrayList<>();
    
        try (
            PreparedStatement stmt = connection.prepareStatement("SELECT t.name " +
                    "FROM tags t " +
                    "JOIN recipe_tags rt ON t.id = rt.tag_id " +
                    "JOIN recipes r ON rt.recipe_id = r.id " +
                    "WHERE r.name = ?")) {
            stmt.setString(1, recipeName);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String tagName = rs.getString(1);
                tags.add(tagName);
            }
    
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return tags;
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
