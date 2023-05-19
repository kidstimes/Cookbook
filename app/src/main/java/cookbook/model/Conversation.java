package cookbook.model;

import java.util.*;

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
      return messages.stream()
              .filter(message -> !message.isRead())
              .max(Comparator.comparing(Message::getDate))
              .orElse(null);
  }

  public Message getMostRecentMessage() {
      return messages.stream()
              .max(Comparator.comparing(Message::getDate))
              .orElse(null);
  }

}
