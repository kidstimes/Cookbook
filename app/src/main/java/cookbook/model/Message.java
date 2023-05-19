package cookbook.model;

import java.time.LocalDate;

/**
 * The Message class.
 */
public class Message {

  private int id;
  private Recipe recipe;
  private String text;
  private String senderUsername;
  private String receiverUsername;
  private boolean isRead;
  private LocalDate date;

  /**
   * Message Constructor.
   *
   * @param id the unique id of the message
   * @param recipe the recipe of the message
   * @param text the text of the message
   * @param senderUsername the username sending the message
   * @param receiverUsername the username receiving the message
   */
  public Message(int id, Recipe recipe, String text, String senderUsername,
       String receiverUsername, boolean isRead, LocalDate date) {
    this.id = id;
    this.recipe = recipe;
    this.text = text;
    this.senderUsername = senderUsername;
    this.receiverUsername = receiverUsername;
    this.isRead = isRead;
    this.date = date;
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

  public String getSenderUsername() {
    return senderUsername;
  }

  public String getReceiverUsername() {
    return receiverUsername;
  }

  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    isRead = read;
  }

  /**
   * Mark the message as read.
   */
  public void read() {
    isRead = true;
  }
  
  public LocalDate getDate() {
    return date;
  }
}
