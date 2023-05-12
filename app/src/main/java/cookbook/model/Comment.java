package cookbook.model;

/**
 * The Comment class.
 */
public class Comment {

  private User user;
  private String text;

  /**
   * Comment Constructor.
   *
   * @param user the user who wrote the comment
   * @param text the text of the comment
   */
  public Comment(User user, String text) {
    this.user = user;
    this.text = text;
  }

  /**
   * Get the user who wrote the comment.
   *
   * @return the user who wrote the comment
   */
  public User getUser() {
    return user;
  }

  /**
   * Get the text of the comment.
   *
   * @return the text of the comment
   */
  public String getText() {
    return text;
  }

  /**
   * Set the text of the comment.
   *
   * @param updatedText the updated text of the comment
   */
  public void setText(String updatedText) {
    text = updatedText;
  }

}
