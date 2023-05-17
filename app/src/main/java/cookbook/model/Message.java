package cookbook.model;

/**
 * The Message class.
 */
public class Message {

  private Recipe recipe;
  private String text;
  private int senderId;
  private int receiverId;

  /**
   * Message Constructor.
   *
   * @param recipe the recipe of the message
   * @param text the text of the message
   * @param senderId the user id of the sender
   * @param receiverId the user id  of the receiver
   */
  public Message(Recipe recipe, String text, int senderId, int receiverId) {
    this.recipe = recipe;
    this.text = text;
    this.senderId = senderId;
    this.receiverId = receiverId;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public String getText() {
    return text;
  }

  public int getSenderId() {
    return senderId;
  }

  public int getReceiverId() {
    return receiverId;
  }
  
}
