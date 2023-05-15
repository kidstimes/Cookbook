package cookbook.model;

public class Comment {
  private int id;
  private String text;
  private int recipeId;
  private int userId;
  private String displayName;

  public Comment(int id, String text, int recipeId, int userId, String displayName) {
    this.id = id;
    this.text = text;
    this.recipeId = recipeId;
    this.userId = userId;
    this.displayName = displayName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getDisplayName() {
    return displayName;
  }



}
