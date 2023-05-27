package cookbook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import cookbook.model.HelpSubsection;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AppTest {

  static Database database = null;
  static CookbookFacade cookbook = null;

  @BeforeAll
  static void initializeTestData() {
    // Initialize global variables database & cookbook
    database = new Database();
    cookbook = new CookbookFacade(database);
    // Initialize test user in the database
    cookbook.userSignUp("testUsername", "testPassword", "testDisplayName");
    // Load user, recipes, and help page setions from database
    cookbook.setCurrentUser("testUserName");
    cookbook.loadAllRecipes();
    cookbook.getHelpSections();
  }
  
  @Test
  void testRecipeSearchByName() {
    // Choose keywords to search for
    ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("Beef", "Stir-Fry"));
  
    // Perform the search by name method
    ArrayList<Recipe> searchedRecipes = cookbook.getRecipesWithName(keywords);
  
    // Confirm that all the recipes have both keywords in the name
    for (Recipe recipe : searchedRecipes) {
      assertTrue(recipe.getName().toLowerCase().contains("beef")
          && recipe.getName().toLowerCase().contains("stir-fry"));
    }
  }

  @Test
  void testRecipeSearchByIngredients() {
    // Choose ingredients to search for
    ArrayList<String> ingredientNames = new ArrayList<String>(Arrays.asList("Garlic", "Onion"));
  
    // Perform the search by ingredients method
    ArrayList<Recipe> searchedRecipes = cookbook.getRecipesWithIngredients(ingredientNames);
  
    // Confirm that all the recipes both of the given ingredients
    boolean containsGarlic = false;
    boolean containsOnion = false;
    for (Recipe recipe : searchedRecipes) {
      containsGarlic = false;
      containsOnion = false;
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient.getName().toLowerCase().contains("onion")) {
          containsOnion = true;
        }
        if (ingredient.getName().toLowerCase().contains("garlic")) {
          containsGarlic = true;
        }
      }
      assertTrue(containsGarlic && containsOnion);
    }
  }

  @Test
  void testRecipeSearchByTags() {
    // Choose tags to search for
    ArrayList<String> tagNames = new ArrayList<String>(Arrays.asList("Vegetarian", "Gluten Free"));
  
    // Perform the search by tags method
    ArrayList<Recipe> searchedRecipes = cookbook.getRecipesWithTags(tagNames);
  
    // Confirm that all the recipes have both of the given tags
    for (Recipe recipe : searchedRecipes) {
      for (String tag : recipe.getTags()) {
        assertTrue(tag.toLowerCase().contains("vegetarian")
            && tag.toLowerCase().contains("gluten free"));
      }
    }
  }

  @Test
  void testHelpPageSearch() {
    // Perform the search of the subsections of the help page
    ArrayList<HelpSubsection> foundSubsections = cookbook.searchHelpContent("add recipe");
    
    // For each returned subsections
    for (HelpSubsection subsection : foundSubsections) {

      // The subsection should contain all the given keywords in its title or text
      assertTrue(
          (subsection.getTitle().toLowerCase().contains("add")
          || subsection.getText().toLowerCase().contains("add"))
          && (subsection.getTitle().toLowerCase().contains("recipe")
          || subsection.getText().toLowerCase().contains("recipe"))
      );
    }
  }

  @AfterAll
  static void deleteTestData() {
    // Delete test user from the database
    database.deleteTestUser();
    // Close connection with the database
    database.disconnect();
  }

}
