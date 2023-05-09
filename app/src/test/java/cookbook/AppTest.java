package cookbook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class AppTest {

  @Test
  void testRecipeSearchByName() {
    CookbookFacade cookbook = new CookbookFacade(new Database());
    cookbook.userSignUp("testUserName", "testPassword", "testDisplayName");
    cookbook.setCurrentUser("testUserName");
    cookbook.loadAllRecipes();

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
    CookbookFacade cookbook = new CookbookFacade(new Database());
    cookbook.setCurrentUser("testUserName");
    cookbook.loadAllRecipes();

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
    CookbookFacade cookbook = new CookbookFacade(new Database());
    cookbook.setCurrentUser("testUserName");
    cookbook.loadAllRecipes();

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

}
