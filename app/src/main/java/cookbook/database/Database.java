package cookbook.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import cookbook.model.CookbookFacade;


public class Database {

    private Connection connection;
    private CookbookFacade cookbookFacade;

    public Database(CookbookFacade cookbookFacade) {
        this.cookbookFacade = cookbookFacade;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/cookbook?user=root&password=12345678&useSSL=false");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void loadAllRecipes() {
        try (
            PreparedStatement stmt = connection.prepareStatement("SELECT r.id, r.name, r.description, r.instructions " +
                    "FROM recipes r")) {
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Integer recipeId = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String instructions = rs.getString(4);
    
                ArrayList<String[]> ingredients = loadIngredientsForRecipe(recipeId);
                ArrayList<String> tags = loadTagsForRecipe(name);
    
                cookbookFacade.addRecipe(new String[]{name, description, instructions}, ingredients, tags);
            }
    
            rs.close();
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
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
