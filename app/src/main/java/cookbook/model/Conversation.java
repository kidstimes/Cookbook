package cookbook.model;

import java.util.ArrayList;
import java.util.Comparator;

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
   * Get the most recent unread message.
   *
   * @return the most recent unread message
   */
  public Message getMostRecentUnreadMessage() {
    return messages.stream()
        .filter(message -> !message.isRead())
        .max(Comparator.comparing(Message::getDate))
        .orElse(null);
  }

  /**
   * Get the most recent message.
   *
   * @return the most recent message
   */
  public Message getMostRecentMessage() {
    return messages.stream()
        .max(Comparator.comparing(Message::getDate))
        .orElse(null);
  }

}
