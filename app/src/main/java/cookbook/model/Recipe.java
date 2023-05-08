package cookbook.model;

import java.util.ArrayList;

/**
 * The Recipe class.
 */
public class Recipe {

  private int id;
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

  /**
   * Recipe Constructor.
   *
   * @param id the unique id of the recipe
   * @param name the name of the recipe
   * @param shortDesc the short description of the recipe
   * @param directions the directions for the recipe
   * @param ingredients the ingredients of the recipe in a 2-dimentional string array
   */
  public Recipe(int id, String name, String shortDesc, String directions,
      ArrayList<String[]> ingredients, ArrayList<String> tags) {
    this.id = id;
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

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }  

  public String getShortDesc() {
    return shortDesc;
  }

  public void setShortDesc(String shortDesc) {
    this.shortDesc = shortDesc;
  }

  public String getDirections() {
    return directions;
  }

  public void setDirection(String directions) {
    this.directions = directions;
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

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

}