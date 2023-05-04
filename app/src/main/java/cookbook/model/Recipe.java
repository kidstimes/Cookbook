package cookbook.model;

import java.util.ArrayList;

public class Recipe {

  private String name;
  private String shortDesc;
  private String directions;
  private ArrayList<String> tags;
  private ArrayList<Ingredient> ingredients;
  private String comments;
  // deal with the comments method later

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
  }

  public String getName() {
    return name;
  }

  public String getShortDesc() {
    return shortDesc;
  }

  public String getDirections() {
    return directions;
  }

  public ArrayList<String> getTags() {
    ArrayList<String> copyTags = new ArrayList<String>();
    for (String tag : tags) {
      copyTags.add(tag);
    }
    return copyTags;
  }

  public void setTags(ArrayList<String> tags) {
    for (String tag : tags) {
      if (!this.tags.contains(tag)) {
        this.tags.add(tag);
      }
    }
  }
  
  public ArrayList<Ingredient> getIngredients() {
    ArrayList<Ingredient> copyIngredients = new ArrayList<Ingredient>();
    for (Ingredient ingredient : ingredients) {
      copyIngredients.add(ingredient);
    }
    return copyIngredients;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  // the following code toString method for testing
  // later can be remove to test package
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(name).append("\n");
    sb.append("Short description: ").append(shortDesc).append("\n");
    sb.append("Directions: ").append(directions).append("\n");
    sb.append("Tags: ");
    for (String tag : tags) {
      sb.append(tag).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length()); // remove the last comma and space
    sb.append("\n");
    sb.append("Ingredients: ");
    for (Ingredient ingredient : ingredients) {
      sb.append(ingredient.toString()).append(", ");
    }
    sb.delete(sb.length() - 2, sb.length()); // remove the last comma and space
    sb.append("\n");
    sb.append("Comments: ").append(comments).append("\n");
    return sb.toString();
  }
}