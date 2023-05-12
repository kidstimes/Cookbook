package cookbook.model;

public class Comment {
  private int id;
  private String text;
  private int recipeId;
  private String recipeName;
  private int userId;
  private String userName;

  public Comment(int id, String text, int recipeId, String recipeName, int userId, String userName) {
      this.id = id;
      this.text = text;
      this.recipeId = recipeId;
      this.recipeName = recipeName;
      this.userId = userId;
      this.userName = userName;
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

  public String getRecipeName() {
      return recipeName;
  }

  public void setRecipeName(String recipeName) {
      this.recipeName = recipeName;
  }

  public int getUserId() {
      return userId;
  }

  public void setUserId(int userId) {
      this.userId = userId;
  }

  public String getUserName() {
      return userName;
  }

  public void setUserName(String userName) {
      this.userName = userName;
  }
}
