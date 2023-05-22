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

  public Message getMostRecentUnreadMessage() {
    return messages.stream().filter(message -> !message.isRead())
              .max(Comparator.comparing(Message::getDateTime)).orElse(null);
  }

  /**
   * Get the most recent message.
   *
   * @return the most recent message
   */
  public Message getMostRecentMessage() {
  	return messages.stream().max(Comparator.comparing(Message::getDateTime)).orElse(null);
  }

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
    return (int) messages.stream().filter(m -> m.getReceiverUsername().equals(username) && !m.isRead()).count();
  }
    

}
