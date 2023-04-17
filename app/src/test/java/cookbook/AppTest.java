package cookbook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.ArrayList;
import cookbook.model.Recipe;
import cookbook.model.CookbookFacade;
import cookbook.database.FileHandler;

class AppTest {

    @Test void searchRecipesByName() {
        FileHandler fileHandler = new FileHandler();
        ArrayList<String[]> recipes = fileHandler.getRecipes();
        ArrayList<String[]> ingredients = fileHandler.getIngredients();

        CookbookFacade cookbook = new CookbookFacade();

        for (String[] recipe : recipes) {
            ArrayList<String[]> recipeIngredients = new ArrayList<String[]>();
            for (String [] ingredient : ingredients) {
                if (ingredient[0] == recipe[0]) {
                    String[] temp = new String[] {ingredient[1], ingredient[2], ingredient[3]};
                    recipeIngredients.add(temp);
                }
            }
            cookbook.addRecipe(recipe, recipeIngredients);
        }

        ArrayList<String> keywords = new ArrayList<String>(
            Arrays.asList("Chicken","Mango"));

        ArrayList<Recipe> searchedRecipes = cookbook.getRecipesWithName(keywords);

        for (Recipe recipe : searchedRecipes) {
            assertTrue(recipe.getName().toLowerCase().contains("chicken") || recipe.getName().toLowerCase().contains("mango"));
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
