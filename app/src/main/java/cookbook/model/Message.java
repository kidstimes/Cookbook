package cookbook.model;

import java.time.LocalDateTime;

/**
 * The Message class.
 */
public class Message implements Comparable<LocalDateTime> {

  private int id;
  private Recipe recipe;
  private String text;
  private String senderUsername;
  private String receiverUsername;
  private boolean isRead;
  private LocalDateTime dateTime;

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
       String receiverUsername, boolean isRead, LocalDateTime dateTime) {
    this.id = id;
    this.recipe = recipe;
    this.text = text;
    this.senderUsername = senderUsername;
    this.receiverUsername = receiverUsername;
    this.isRead = isRead;
    this.dateTime = dateTime;
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

  public void markAsRead() {
    isRead = true;
  }


  
  public LocalDateTime getDateTime() {
    return dateTime;
  }

  @Override
  public int compareTo(LocalDateTime o) {
    return dateTime.compareTo(o);
  }
}
