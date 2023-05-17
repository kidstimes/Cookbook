package cookbook.model;

public class Message {
  private Recipe recipe;
  private String text;
  private int senderId;
  private int receiverId;
  private boolean isRead;
  private String senderUserName;
  private String senderDisplayName;
  private String receiverUserName;
  private String receiverDisplayName;


  /**
   * Message Constructor.
   *
   * @param recipe the recipe that the message is about
   * @param text the text of the message
   * @param senderId the id of the sender
   * @param receiverId the id of the receiver
   * @param isRead whether the message is read or not
   */
  public Message(Recipe recipe, String text, int senderId, int receiverId, boolean isRead) {
    this.recipe = recipe;
    this.text = text;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.isRead = isRead;
  }

  /**
   * Get the recipe that the message is about.
   *
   * @return the recipe
   */
  public Recipe getRecipe() {
    return recipe;
  }

  /**
   * Get the text of the message.
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Get the id of the sender.
   *
   * @return the sender id
   */
  public int getSenderId() {
    return senderId;
  }

  /**
   * Get the id of the receiver.
   *
   * @return the receiver id
   */
  public int getReceiverId() {
    return receiverId;
  }

  /**
   * Get whether the message is read or not.
   *
   * @return true if the message is read, false otherwise
   */
  public boolean isRead() {
    return isRead;
  }


  public void read() {
    isRead = true;
  }

  public void unread() {
    isRead = false;
  }

  public String getSenderUserName() {
    return senderUserName;
  }


  public String getSenderDisplayName() {
    return senderDisplayName;
  }


  public String getReceiverUserName() {
    return receiverUserName;
  }

  public String getReceiverDisplayName() {
    return receiverDisplayName;
  }

  
}
