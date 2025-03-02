package cookbook;

<<<<<<< HEAD
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
=======
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.ArrayList;
import cookbook.model.Recipe;
import cookbook.model.Ingredient;
import cookbook.model.CookbookFacade;
import cookbook.database.FileHandler;

class AppTest {

    @Test void searchRecipesByNameIngredientsOrTags() {
        // Get test data from csv files in database package
        FileHandler fileHandler = new FileHandler();
        ArrayList<String[]> recipes = fileHandler.getRecipes();
        ArrayList<String[]> ingredients = fileHandler.getIngredients();
        ArrayList<String[]> tags = fileHandler.getTags();

        // Add the recipes from the test data using the CookbookFacade class
        CookbookFacade cookbook = new CookbookFacade();
        for (String[] recipe : recipes) {
            // Separate the ingredients of the recipe from the rest of the recipes
            ArrayList<String[]> recipeIngredients = new ArrayList<String[]>();
            for (String [] ingredient : ingredients) {
                if (ingredient[0] == recipe[0]) {
                    String[] tempIngredients = new String[] {ingredient[1], ingredient[2], ingredient[3]};
                    recipeIngredients.add(tempIngredients);
                }
            }

            // Separate the tags of the recipe from the other recipes
            /*ArrayList<String> recipeTags = new ArrayList<String>();
            for (String[] tag : tags) {
                if (tag[0] == recipe[0]) {
                    recipeTags.add(tag[1]);
                }
            }

            // Add the recipe with its ingredients
            cookbook.addRecipe(recipe, recipeIngredients, recipeTags);*/
        }

        // Choose keywords to search for
        ArrayList<String> keywords = new ArrayList<String>(
            Arrays.asList("Chicken","Mango"));

        // Perform the search by name method
        ArrayList<Recipe> searchedRecipes = cookbook.getRecipesWithName(keywords);

        // Confirm that all the recipes have at least one of the keywords in the name
        for (Recipe recipe : searchedRecipes) {
            assertTrue(recipe.getName().toLowerCase().contains("chicken") || recipe.getName().toLowerCase().contains("mango"));
        }

        // Choose ingredients to search for
        ArrayList<String> ingredientNames = new ArrayList<String>(
            Arrays.asList("Olive Oil", "Garlic"));

        // Perform the search by ingredients method
        searchedRecipes.clear();
        searchedRecipes = cookbook.getRecipesWithIngredients(ingredientNames);

        // Confirm that all the recipes have at least one of the given ingredients
        boolean containsIngredient = false;
        for (Recipe recipe : searchedRecipes) {
            containsIngredient = false;
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.getName().toLowerCase().contains("olive oil") || ingredient.getName().toLowerCase().contains("garlic")) {
                    containsIngredient = true;
                }
            }
            assertTrue(containsIngredient);
        }

        // Choose tags to search for
        ArrayList<String> tagNames = new ArrayList<String>(
            Arrays.asList("Vegan","Main Course"));

        // Perform the search by tags method
        searchedRecipes.clear();
        searchedRecipes = cookbook.getRecipesWithTags(tagNames);

        // Confirm that all the recipes have at least one of the given tags
        for (Recipe recipe : searchedRecipes) {
            for (String tag : recipe.getTags())
            assertTrue(tag.toLowerCase().contains("vegan") || tag.toLowerCase().contains("main course"));
        }
        
    }

    @Test void readRecipes() {
        FileHandler fileHandler = new FileHandler();
        ArrayList<String[]> recipes = fileHandler.getRecipes();

        assertNotNull(recipes, "Unable to read recipes.csv");
    }

    @Test void readIngredients() {
        FileHandler fileHandler = new FileHandler();
        ArrayList<String[]> ingredients = fileHandler.getIngredients();

        assertNotNull(ingredients, "Unable to read ingredients.csv");
    }

    @Test void readTags() {
        FileHandler fileHandler = new FileHandler();
        ArrayList<String[]> tags = fileHandler.getTags();

        assertNotNull(tags, "Unable to read tags.csv");
    }
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae

}
