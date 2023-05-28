package cookbook.model;

import java.util.ArrayList;

/**
 * The Conversation class.
 */
public class Conversation {
  private String otherUsername;
  private ArrayList<Message> messages;

  public Conversation(String otherUsername) {
    this.otherUsername = otherUsername;
    this.messages = new ArrayList<>();
  }

  public String getOtherUsername() {
    return otherUsername;
  }

  public ArrayList<Message> getMessages() {
    return messages;
  }

  public void addMessage(Message message) {
    this.messages.add(message);
  }


  /**
   * Check if the conversation has unread messages.
   */
  public boolean hasUnreadReceivedMessages(String username) {
    return this.messages.stream()
        .filter(message -> !message.getSenderUsername().equals(username))
        .anyMatch(message -> !message.isRead());
  }

  public boolean allMessagesSentByUser(String username) {
    return this.messages.stream()
            .allMatch(message -> message.getSenderUsername().equals(username));
  }

  public int countUnreadReceivedMessages(String username) {
    return (int) messages.stream().filter(
        m -> m.getReceiverUsername().equals(username) && !m.isRead()).count();
  }
    
  /** Get the newest message in the conversation.
   *
   * @return the newest message
   */
  public Message getNewestMessage() {
    //return the message with most recent datetime
    return this.messages.stream().max((m1, m2) -> 
      m1.getDateTime().compareTo(m2.getDateTime())).get();
  }

}
