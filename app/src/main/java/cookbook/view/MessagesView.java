package cookbook.view;

import cookbook.model.Conversation;
import cookbook.model.Message;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * View class for the messages page.
 */
public class MessagesView {
  private BorderPane view;
  private MessagesViewObserver observer;
  private ListView<VBox> usersView;
  private ListView<VBox> messagesView;
  private Conversation selectedConversation;
  private TextArea replyField;
  private Image open;
  private Image close;

  /**
   * Message View Constructor.
   *
   * @param displayName   the display name of the user
   * @param conversations the conversations of the user
   */
  public MessagesView(String displayName, ArrayList<Conversation> conversations) {
    this.view = new BorderPane();
    this.open = new Image(getClass().getResourceAsStream("/images/open.png"));
    this.close = new Image(getClass().getResourceAsStream("/images/close.png"));
    this.usersView = new ListView<>();
    this.messagesView = new ListView<>();
    this.replyField = new TextArea();
    initLayout(displayName);
    showConversations(conversations);
  }

  public void setObserver(MessagesViewObserver observer) {
    this.observer = observer;
  }

  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout of the message view.
   *
   * @param displayName the display name of the user
   */
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
    SplitPane splitPane = new SplitPane();
    splitPane.getItems().addAll(usersView, messagesView);
    splitPane.setPrefSize(1000, 800);
    splitPane.setDividerPositions(0.25f);
    view.setCenter(splitPane);
    Label title = new Label("Messages");
    title.setFont(Font.font("Roboto", FontWeight.BOLD, 32));

    VBox titleBox = new VBox();
    titleBox.getChildren().addAll(title, splitPane);
    titleBox.setAlignment(Pos.TOP_LEFT);
    titleBox.setPadding(new Insets(20, 20, 0, 50));
    titleBox.setSpacing(10);
    view.setCenter(titleBox);
  }

  /**
   * Show the conversations of the user.
   *
   * @param conversations an arraylist with the conversations
   */
  public void showConversations(ArrayList<Conversation> conversations) {
    usersView.getItems().clear();
    for (Conversation conversation : conversations) {
      usersView.getItems().add(createUserView(conversation));
    }
  }

  private VBox createUserView(Conversation conversation) {
    Text otherUser = new Text("   " + conversation.getOtherUsername());
    otherUser.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
    ImageView envelopeIcon = new ImageView(conversation.getMostRecentUnreadMessage()
        != null ? close : open);
    envelopeIcon.setFitHeight(30);
    envelopeIcon.setFitWidth(30);
    HBox userInfo = new HBox(otherUser, envelopeIcon);
    VBox userBox = new VBox();
    userBox.getChildren().add(userInfo);

    userBox.setOnMouseClicked(e -> {
      this.selectedConversation = conversation;
      showMessages(new ArrayList<>(conversation.getMessages().stream()
          .sorted(Comparator.comparing(Message::getDate))
          .collect(Collectors.toList())));
    });
    return userBox;
  }

  private void showMessages(ArrayList<Message> messages) {
    messagesView.getItems().clear();
    for (Message message : messages) {
      messagesView.getItems().add(createMessageBox(message));
    }
    VBox replyWithSpaceBox = new VBox();
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    VBox replyBox = createReplyBox();
    replyWithSpaceBox.getChildren().addAll(spacer, replyBox);
    messagesView.getItems().add(replyWithSpaceBox);
  }

  private VBox createReplyBox() {
    replyField.setPromptText("Write a message...");
    replyField.setPrefHeight(100);
    Button replyButton = new Button("Reply");
    replyButton.setOnAction(e -> {
      String replyText = replyField.getText();
      if (!replyText.isEmpty()) {
        observer.replyMessage(selectedConversation.getOtherUsername(), replyText);
        replyField.clear();
        refreshView();
      }
    });
    VBox replyBox = new VBox();
    replyBox.getChildren().addAll(replyField, replyButton);
    replyBox.setSpacing(20);
    return replyBox;
  }

  private VBox createMessageBox(Message message) {
    VBox messageBox = new VBox();
    messageBox.setAlignment(message.getSenderUsername().equals(observer.getUsername())
        ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
    messageBox.setBackground(new Background(new BackgroundFill(
        message.getSenderUsername().equals(observer.getUsername())
            ? Color.web("#F4F1DE") : Color.web("#81B29A"), null, null)));
    messageBox.setPadding(new Insets(10, 10, 10, 10));

    ImageView envelopeIcon = new ImageView(message.isRead() ? open : close);
    envelopeIcon.setFitHeight(30);
    envelopeIcon.setFitWidth(30);
    Text date = new Text(message.getDate().toString());
    date.setFont(Font.font("Roboto", 14));
    HBox iconAndDate = new HBox(envelopeIcon, date);
    iconAndDate
      .setAlignment(message.getSenderUsername().equals(observer.getUsername()) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT); // added
                                                                                                                        // this
                                                                                                                        // line
    messageBox.getChildren().add(iconAndDate);
    Text text = new Text(message.getText());
    text.setFont(Font.font("Roboto", 24));
    messageBox.getChildren().add(text);
    if (message.getRecipe() != null) {
      Hyperlink recipeLink = new Hyperlink(message.getRecipe().getName());
      recipeLink.setOnAction(e -> observer.goToRecipe(message.getRecipe()));
      messageBox.getChildren().add(recipeLink);
    }
    if (!message.getSenderUsername().equals(observer.getUsername())) {
      messageBox.setOnMouseClicked(e -> {
        observer.updateMessageIsRead(message.getId());
        message.setRead(true);
        refreshView();
      });
    }
    return messageBox;
  }

  /**
   * Refresh the message view.
   */
  public void refreshView() {
    ArrayList<Conversation> conversations = observer.getConversations();
    for (Conversation conversation : conversations) {
      if (conversation.equals(selectedConversation)) {
        selectedConversation = conversation;
        break;
      }
    }
    showConversations(conversations);
    showMessages(new ArrayList<>(selectedConversation.getMessages().stream()
        .sorted(Comparator.comparing(Message::getDate).reversed())
        .collect(Collectors.toList())));
  }
}
