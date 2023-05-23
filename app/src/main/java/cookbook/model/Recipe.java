package cookbook.model;

import java.util.ArrayList;
import java.util.Date;

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
  private boolean starred;
  private ArrayList<Comment> comments;
  private String createrUsername;
  private ArrayList<RecipeEditRecord> editRecords;
  private Date date;

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
    this.ingredients = new ArrayList<>();
    for (String[] ingredient : ingredients) {
      this.ingredients.add(new Ingredient(ingredient[0],
          Float.parseFloat(ingredient[1]), ingredient[2]));
    }

    // Initialize tags arraylist
    this.tags = new ArrayList<>();
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
      ArrayList<String[]> ingredients, ArrayList<String> tags, 
      String createrUsername, Date date, ArrayList<RecipeEditRecord> editRecords) {
    this.id = id;
    this.name = name;
    this.shortDesc = shortDesc;
    this.directions = directions;
    this.createrUsername = createrUsername;
    this.date = date;
    this.editRecords = editRecords;

    // Create ingredient objects and add them to the recipe
    this.ingredients = new ArrayList<>();
    for (String[] ingredient : ingredients) {
      this.ingredients.add(new Ingredient(ingredient[0],
          Float.parseFloat(ingredient[1]), ingredient[2]));
    }

    // Initialize tags arraylist
    this.tags = new ArrayList<>();
    for (String tag : tags) {
      this.tags.add(tag);
    }

    starred = false;
  }

  // Getters and setters
  public String getName() {
    return name;
  }

  public int getId() {
    return id;
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

  public String getCreaterUsername() {
    return createrUsername;
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
    ArrayList<String> copyTags = new ArrayList<>();
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
    this.tags.clear();
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
    ArrayList<Ingredient> copyIngredients = new ArrayList<>();
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

  /** Get a copy of the edit records.
   *
   * @return an arraylist with the edit records.
   */
  public ArrayList<RecipeEditRecord> getEditRecords() {
    ArrayList<RecipeEditRecord> copyEditRecords = new ArrayList<>();
    for (RecipeEditRecord record : editRecords) {
      copyEditRecords.add(record);
    }
    return copyEditRecords;

  }


  /** Set the ingredients of the recipe.
   *
   * @param editedIngredients an arraylist with the edited ingredients
   */
  public void setIngredients(ArrayList<String[]> editedIngredients) {
    this.ingredients.clear();
    for (String[] ingredient : editedIngredients) {
      this.ingredients.add(new Ingredient(ingredient[0],
          Float.parseFloat(ingredient[1]), ingredient[2]));
    }
  }

  /** Get the creation date of the recipe.
   *
   * @return the creation date of the recipe
   */
  public Date getCreationDate() {
    return date;
  }

}
