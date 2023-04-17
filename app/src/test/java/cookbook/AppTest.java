package cookbook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cookbook.model.Recipe;
import cookbook.database.FileHandler;
import java.util.ArrayList;

class AppTest {
    @Test void appHasAGreeting() {
        // App classUnderTest = new App();
        //assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
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
