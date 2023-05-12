package cookbook.model;

import java.util.ArrayList;

/**
 * The Recipe class.
 */
public class Recipe {

  private String name;
  private String shortDesc;
  private String directions;
  private ArrayList<String> tags;
  private ArrayList<Ingredient> ingredients;
  private boolean starred;
  private ArrayList<Comment> comments;

  /**
   * Recipe Constructor.
   *
   * @param name the name of the recipe
   * @param shortDesc the short description of the recipe
   * @param directions the directions for the recipe
   * @param ingredients the ingredients of the recipe in a 2-dimentional string array
   */
  public Recipe(String name, String shortDesc, String directions, ArrayList<String[]> ingredients,
      ArrayList<String> tags) {
    this.name = name;
    this.shortDesc = shortDesc;
    this.directions = directions;

    // Create ingredient objects and add them to the recipe
    this.ingredients = new ArrayList<Ingredient>();
    for (String[] ingredient : ingredients) {
      this.ingredients.add(new Ingredient(ingredient[0],
          Float.parseFloat(ingredient[1]), ingredient[2]));
    }

    // Initialize tags arraylist
    this.tags = new ArrayList<String>();
    for (String tag : tags) {
      this.tags.add(tag);
    }

    starred = false;
    comments = new ArrayList<>();
  }

  // Getters and setters
  public String getName() {
    return name;
  }

  public String getShortDesc() {
    return shortDesc;
  }

  public String getDirections() {
    return directions;
  }

  /**
   * Get the tags of the recipe.
   *
   * @return an arraylist with the tags
   */
  public ArrayList<String> getTags() {
    ArrayList<String> copyTags = new ArrayList<String>();
    for (String tag : tags) {
      copyTags.add(tag);
    }
    return copyTags;
  }

  /**
   * Set the tags of the recipe.
   *
   * @param tags an arraylist with the tags
   */
  public void setTags(ArrayList<String> tags) {
    for (String tag : tags) {
      if (!this.tags.contains(tag)) {
        this.tags.add(tag);
      }
    }
  }
  
  /**
   * Get the ingredients of the recipe.
   *
   * @return an arraylist with the ingredients
   */
  public ArrayList<Ingredient> getIngredients() {
    ArrayList<Ingredient> copyIngredients = new ArrayList<Ingredient>();
    for (Ingredient ingredient : ingredients) {
      copyIngredients.add(ingredient);
    }
    return copyIngredients;
  }

  /**
   * Star recipe.
   */
  public void star() {
    starred = true;
  }

  /**
   * Unstar recipe.
   */
  public void unstar() {
    starred = false;
  }

  /**
   * Check if the recipe is starred by the user.
   *
   * @return true if the recipe is starred, otherwise unstarred.
   */
  public boolean isStarred() {
    return starred;
  }

  /**
   * Get the comments of the recipe.
   *
   * @return an arraylist with the comments
   */
  public ArrayList<Comment> getComments() {
    ArrayList<Comment> copyComments = new ArrayList<>();
    for (Comment comment : comments) {
      copyComments.add(comment);
    }
    return copyComments;
  }

  /**
   * Add a new comment to the recipe.
   *
   * @param user the user who wrote the comment
   * @param text the text of the comment
   */
  public void addComment(User user, String text) {
    comments.add(new Comment(user, text));
  }

  /**
   * Edit the text of the given comment.
   *
   * @param comment the comment to edit
   * @param updatedText the updated text for the comment
   */
  public void editComment(Comment comment, String updatedText) {
    comment.setText(updatedText);
  }

  /**
   * Delete the comment under the recipe.
   *
   * @param comment the comment to delete
   */
  public void deleteComment(Comment comment) {
    comments.remove(comment);
  }

}
