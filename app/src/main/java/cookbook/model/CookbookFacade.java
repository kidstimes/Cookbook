package cookbook.model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CookbookFacade {
   
    private ArrayList<Recipe> recipes;

    /**
     * Cookbook Constructor.
     */
    public CookbookFacade() {
        recipes = new ArrayList<Recipe>();
    }

    /**
     * Add a recipe to the cookbook.
     *
     * @param recipe the information about the recipe in a string array
     * @param ingredients the ingredients of the recipe in a 2-dimentional string array
     * @param tags the tags of the recipe in a string array
     */
    public void addRecipe(String recipe[], ArrayList<String[]> ingredients, ArrayList<String> tags) {
        recipes.add(new Recipe(recipe[0], recipe[1], recipe[2], ingredients, tags));
    }

    /**
     * Delete a recipe from the cookbook.
     *
     * @param name the name of the recipe to remove.
     */
    public void removeRecipe(String name) {
        recipes.removeIf(recipe -> Objects.equals(name, recipe.getName()));
    }

    /**
     * Get the recipes of the cookbook.
     *
     * @return the recipe objects
     */
    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> copyRecipes = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            copyRecipes.add(recipe);
        }
        return copyRecipes;
    }

    /**
     * Get the recipes of the cookbook containing the given keywords in the name.
     *
     * @param keywords the keywords to search for in the recipes
     * @return the recipes containing the keywords
     */
    public ArrayList<Recipe> getRecipesWithName(ArrayList<String> keywords) {
        // return a list of recipes that contain any of the keywords in their name
        return recipes.stream()
        // change the characters to lower case and to find the match use the stream collecter
                .filter(recipe -> keywords.stream().anyMatch(keyword -> recipe.getName().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get the recipes of the cookbook containing the given ingredients.
     *
     * @param ingredients the ingredients to search for in the recipes
     * @return the recipes containing the ingredients
     */
    public ArrayList<Recipe> getRecipesWithIngredients(ArrayList<String> ingredients) {
        // return a list of recipes that contain all the ingredients
        return recipes.stream()
        //as above
                .filter(recipe -> ingredients.stream().anyMatch(ingredient -> recipe.getIngredients().stream().anyMatch(i -> i.getName().toLowerCase().contains(ingredient.toLowerCase()))))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get the recipes of the cookbook containing the given tags.
     *
     * @param tags the tags to search for in the recipes
     * @return the recipes containing the tags
     */
    public ArrayList<Recipe> getRecipesWithTags(ArrayList<String> tags) {
        // return a list of recipes that contain all the tags
        return recipes.stream()
                .filter(recipe -> recipe.getTags().containsAll(tags))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
