package cookbook.model;

import java.util.ArrayList;

/**
 * The User class.
 */
public class User {

  private int id;
  private String username;
  private String displayName;
  private ArrayList<Recipe> favorites;

  /**
   * User Constructor.
   *
   * @param id the unique id of the user
   * @param username the unique username of the user
   * @param displayName the display name of the user
   */
  public User(int id, String username, String displayName) {
    this.id = id;
    this.username = username;
    this.displayName = displayName;
    this.favorites = new ArrayList<>(); 
  }

  /**
   * Modify the username of the user.
   *
   * @param username the new username
   */
  public void modifyUsername(String username) {
    this.username = username;
  }

  /**
   * Modify the display name of the user.
   *
   * @param displayName the new display name
   */
  public void modifyDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Add a recipe to the user's favorite recipes.
   *
   * @param recipe the recipe to add to the favorites
   */
  public void addToFavorites(Recipe recipe) {
    recipe.star();
    this.favorites.add(recipe);
  }

  /**
   * Get the username of the user.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Get the display naem of the user.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Get the favorite recipes of the user.
   *
   * @return an arraylist with the favorite recipes
   */
  public ArrayList<Recipe> getFavorites() {
    ArrayList<Recipe> copyFavorites = new ArrayList<>();
    for (Recipe recipe : favorites) {
      copyFavorites.add(recipe);
    }
    return copyFavorites;
  }

  /**
   * Remove a recipe from the user's favorites.
   *
   * @param recipe the recipe to remove
   */
  public void removeFromFavorites(Recipe recipe) {
    recipe.unstar();
    favorites.remove(recipe);
  }

  /**
   * Delete the user.
   */
  public void deleteUser() {
    // delete user from the db
  }

}
