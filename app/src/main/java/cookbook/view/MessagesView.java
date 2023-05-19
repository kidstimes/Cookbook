package cookbook.view;

import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import cookbook.model.Message;
import cookbook.model.Recipe;

public class MessagesView {
    private BorderPane view;
    private MessagesViewObserver observer;
    private ListView<VBox> inboxView;
    private ListView<VBox> outboxView;
    private Image open;
    private Image close;

    public MessagesView(String displayName, ArrayList<Message> inbox, ArrayList<Message> outbox) {
        this.view = new BorderPane();
        this.open = new Image(getClass().getResourceAsStream("/images/open.png"));
        this.close = new Image(getClass().getResourceAsStream("/images/close.png"));
        this.inboxView = new ListView<>();
        this.outboxView = new ListView<>();
        initLayout(displayName);
        showInbox(inbox);
        showOutbox(outbox);
    }

    public void setObserver(MessagesViewObserver observer) {
        this.observer = observer;
    }

    public Node getView() {
        return view;
    }

    public void initLayout(String displayName) {
        Sidebar sidebar = new Sidebar(displayName);
        sidebar.addButton("Home Page", e -> observer.goToHomePage());
        sidebar.addButton("Browse Recipes", e -> observer.goToBrowser());
        sidebar.addButton("Add a Recipe", e -> observer.goToAddRecipe());
        sidebar.addButton("Weekly Dinner List", e -> observer.goToWeeklyDinner());
        sidebar.addButton("My Favorites", e -> observer.goToMyFavorite());
        sidebar.addButton("My Shopping List", e -> observer.goToShoppingList());
        sidebar.addButton("Messages", e -> observer.goToMessages());
        sidebar.addButton("My Account", e -> observer.goToAccount());
        sidebar.addHyperlink("Help", e -> observer.goToHelp());
        sidebar.addHyperlink("Log Out", e -> observer.userLogout());

        sidebar.setActiveButton("Messages");
        sidebar.finalizeLayout();

        view.setLeft(sidebar);

        Button inboxButton = new Button("Inbox");
        inboxButton.setOnAction(e -> view.setCenter(inboxView));

        Button outboxButton = new Button("Outbox");
        outboxButton.setOnAction(e -> view.setCenter(outboxView));

        VBox buttonBox = new VBox(10, inboxButton, outboxButton);
        view.setRight(buttonBox);

        view.setCenter(inboxView); 
    }

    public void showInbox(ArrayList<Message> messages) {
        inboxView.getItems().clear();
        for (Message message : messages) {
            inboxView.getItems().add(createInboxMessageView(message));
        }
    }

    public void showOutbox(ArrayList<Message> messages) {
        outboxView.getItems().clear();
        for (Message message : messages) {
            outboxView.getItems().add(createOutboxMessageView(message));
        }
    }

    private VBox createInboxMessageView(Message message) {
        return createMessageView(message, true);
    }

    private VBox createOutboxMessageView(Message message) {
        return createMessageView(message, false);
    }

    private VBox createMessageView(Message message, boolean isInbox) {
      ImageView envelopeIcon = new ImageView(isInbox ? (message.isRead() ? open : close) : close);
      envelopeIcon.setFitHeight(30);
      envelopeIcon.setFitWidth(30);
  
      String senderOrReceiver = isInbox ? "Sender: " : "Receiver: ";
      String username = isInbox ? message.getSenderUsername() : message.getReceiverUsername();
  
      Text sender = new Text("   " + senderOrReceiver + username);
      sender.setFont(isInbox && !message.isRead() ? Font.font("Roboto", FontWeight.BOLD, 24) : new Font("Roboto", 24));
  
      Recipe recipe = message.getRecipe();
      Hyperlink recipeLink = new Hyperlink("   " + recipe.getName());
      recipeLink.setOnAction(e -> observer.goToRecipe(recipe));
      recipeLink.setFont(new Font("Roboto", 22));
      recipeLink.setTextFill(Color.web("#81B29A"));  // set the color of hyperlink
  
      Text messageText = new Text("   " + message.getText());
      messageText.setFont(new Font("Roboto", 24));
  
      TextField replyInput = new TextField();
      replyInput.setPromptText("Type your reply here...");
      replyInput.setFont(new Font("Roboto", 20));
      replyInput.setVisible(false); // Set visibility to false initially
  
      Button replyButton = new Button("Reply");
      replyButton.setStyle("-fx-background-color: #81B29A; -fx-text-fill: white; -fx-font-size: 24px;");
      replyButton.setOnAction(e -> {
          if (!replyInput.getText().isEmpty()) {
              //observer.replyMessage(message, replyInput.getText());
              replyInput.clear();
          }
      });
      replyButton.setVisible(false); // Set visibility to false initially
  
      VBox messageContent = new VBox(5, sender, messageText, recipeLink, replyInput, replyButton);
      messageContent.setVisible(false);
  
      Text date = new Text(message.getDate().toString());
      date.setFont(new Font("Roboto", 14));
  
      HBox messageHeader = new HBox(5, envelopeIcon, date, messageContent); 
      messageHeader.setAlignment(Pos.CENTER_LEFT);
  
      VBox messageView = new VBox(5, messageHeader);
      messageView.setOnMouseClicked(e -> {
          boolean visibility = !messageContent.isVisible();
          messageContent.setVisible(visibility);
          replyButton.setVisible(visibility); 
          replyInput.setVisible(visibility); 
  
          if (isInbox && visibility && !message.isRead()) {
              message.read();
              observer.updateMessageIsRead(message.getId());
              envelopeIcon.setImage(open);
              messageView.setStyle("-fx-background-color: #F9F8F3;");
              sender.setFont(new Font("Roboto", 24));
          }
      });
  
      if (isInbox && !message.isRead()) {
          messageView.setStyle("-fx-background-color: grey;");
      } else {
          messageView.setStyle("-fx-background-color: #F9F8F3;");
      }
  
      return messageView;
  }
  
}
