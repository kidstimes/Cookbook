package cookbook.model;

/**
 * The Message class.
 */
public class Message {

  private int id;
  private Recipe recipe;
  private String text;
  private User sender;
  private User receiver;

  /**
   * Message Constructor.
   *
   * @param id the unique id of the message
   * @param recipe the recipe of the message
   * @param text the text of the message
   * @param sender the user sending the message
   * @param receiver the user receiving the message
   */
  public Message(int id, Recipe recipe, String text, User sender, User receiver) {
    this.id = id;
    this.recipe = recipe;
    this.text = text;
    this.sender = sender;
    this.receiver = receiver;
  }

  public int getId() {
    return id;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public String getText() {
    return text;
  }

  public User getSender() {
    return sender;
  }

  public User getReceiver() {
    return receiver;
  }
  
}
