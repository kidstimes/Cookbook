package cookbook;

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
            ArrayList<String> recipeTags = new ArrayList<String>();
            for (String[] tag : tags) {
                if (tag[0] == recipe[0]) {
                    recipeTags.add(tag[1]);
                }
            }

            // Add the recipe with its ingredients
            cookbook.addRecipe(recipe, recipeIngredients, recipeTags);
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

}
