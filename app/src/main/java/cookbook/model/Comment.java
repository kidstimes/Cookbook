package cookbook.model;

/**
 * The Comment class.
 */
public class Comment {

  private int id;
  private String text;
  private int userId;
  private String displayName;

  /**
   * Comment Constructor.
   */
  public Comment(int id, String text, int recipeId, int userId, String displayName) {
    this.id = id;
    this.text = text;
    this.userId = userId;
    this.displayName = displayName;
  }

  public int getId() {
    return id;
  }


  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  public int getUserId() {
    return userId;
  }


  public String getDisplayName() {
    return displayName;
  }



}
