package cookbook.view;

import cookbook.model.Conversation;
import cookbook.model.Message;
import cookbook.model.Recipe;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * View for the messages.
 */
public class MessagesView {
  private BorderPane view;
  private MessagesViewObserver observer;
  private ListView<VBox> usersView;
  private ListView<VBox> messagesView;
  private ArrayList<Conversation> conversations;
  private VBox messageBox;
  private SplitPane splitPane;
  private Conversation selectedConversation;
  private TextArea replyField;
  private Image open;
  private Image close;
  private VBox selectedUserBox;
  private Label messageLabel;

  /** Constructor for the messages view.
   *
   * @param displayName the display name of the user
   * @param conversations the conversations of the user
   * @param messagesViewController the messages view controller
   */
  public MessagesView(String displayName, ArrayList<Conversation> conversations, MessagesViewObserver messagesViewController) {
    this.view = new BorderPane();
    this.open = new Image(getClass().getResourceAsStream("/images/open.png"));
    this.close = new Image(getClass().getResourceAsStream("/images/close.png"));
    this.usersView = new ListView<>();
    this.messagesView = new ListView<>();
    this.replyField = new TextArea();
    this.conversations = conversations;
    this.observer = messagesViewController;
    initLayout(displayName);
    showConversations(conversations);
  }

  public void setObserver(MessagesViewObserver observer) {
    this.observer = observer;
  }

  public Node getView() {
    return view;
  }

  /** Initialize the layout of the messages view.
   *
   * @param displayName the display name of the user
   */
  public void initLayout() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
    sidebar.setMaxWidth(100);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeTitle = new Text(displayName + ", welcome!");
    welcomeTitle.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeTitle);
    
    Button[] sidebarButtons = {
      createButton("Home Page", e -> observer.goToHomePage()),
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> observer.goToMyFavorite()),
      createButton("My Shopping List", e -> observer.goToShoppingList()),
      createButton("Messages", e -> observer.goToMessages()),
    };
    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    spacer.setMinHeight(50);
    VBox replyBox = createReplyBox();
    replyBox.setAlignment(Pos.BOTTOM_RIGHT);
    VBox.setVgrow(replyBox, Priority.NEVER);
    VBox spacerBox = new VBox();
    spacerBox.getChildren().add(spacer);
    messagesView.getItems().add(spacerBox);
    messagesView.getItems().add(replyBox);
  }

  private VBox createReplyBox() {
    replyField.setPromptText("Write a message...");
    replyField.setWrapText(true);
    replyField.setFont(Font.font("Roboto", FontWeight.NORMAL, 18));
    replyField.setPrefHeight(100); 
    Button replyButton = new Button("Reply");
    replyButton.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    replyButton.setStyle(" -fx-background-color: #3D405B; -fx-text-fill: white; -fx-background-radius:"
    + " 20;-fx-effect: null;-fx-cursor: hand;"
    + " -fx-padding: 5 10 5 10; -fx-margin: 0 0 0 10;");
    replyButton.setOnAction(e -> {
      String replyText = replyField.getText();
      if (!replyText.isEmpty()) {
        if (observer.replyMessage(selectedConversation.getOtherUsername(), replyText)) {
          replyField.clear();
          Message newMessage = observer.getLatestMessage();
          selectedConversation.addMessage(newMessage);
          refreshView();
        } else {
          showInlineStyledAlert(Alert.AlertType.ERROR, "Message Error", "There was an issue sending your message. Please try again.");
        }
      } else {
          showInlineStyledAlert(Alert.AlertType.WARNING, "Empty Message", "You cannot send an empty message.");
      }
    });
    HBox replyButtonBox = new HBox();
    replyButtonBox.getChildren().add(replyButton);
    replyButtonBox.setAlignment(Pos.BOTTOM_RIGHT); 
    VBox replyBox = new VBox();
    replyBox.getChildren().addAll(replyField, replyButtonBox);
    replyBox.setSpacing(20);
    return replyBox;
  }


  private VBox createMessageBox(Message message) {
    messagesView.setStyle("-fx-selection-bar: #ffffff; -fx-selection-bar-non-focused: #ffffff;");
    messagesView.setStyle("-fx-cell-text-fill: #000000;");
    messageBox = new VBox();
    if (message.getSenderUsername().equals(observer.getUsername())) {
        messageBox.setAlignment(Pos.CENTER_RIGHT);
    } else {
        messageBox.setAlignment(Pos.CENTER_LEFT);
    }
    messageBox.setPadding(new Insets(10, 10, 20, 10));
    messageBox.setSpacing(10);
    Label senderNameAndDate;
    if (message.getSenderUsername().equals(observer.getUsername())) {
        senderNameAndDate = new Label("You   " + message.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    } else {
        senderNameAndDate = new Label(observer.getDisplayNameByUsername(message.getSenderUsername()) + "(" + message.getSenderUsername() + ")" +
                "   " + message.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
    senderNameAndDate.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
    senderNameAndDate.setTextFill(Color.web("#69a486")); // Set color to #81B29A
    senderNameAndDate.setPadding(new Insets(2, 2, 2, 2));
    VBox innerMessageBox = new VBox();
    if (message.getSenderUsername().equals(observer.getUsername())) {
        innerMessageBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        innerMessageBox.setBackground(new Background(new BackgroundFill(Color.web("#F9F8F3"), CornerRadii.EMPTY, Insets.EMPTY)));
    } else {
        innerMessageBox.setBorder(new Border(new BorderStroke(Color.web("#D3D3D3"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        innerMessageBox.setBackground(new Background(new BackgroundFill(Color.web("#d6e6de"), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    messageLabel = new Label();
    messageLabel.setMaxWidth(400);
    messageLabel.setWrapText(true);
    messageLabel.setPrefWidth(400);
    messageLabel.setFont(Font.font("Roboto", 20));
    messageLabel.setPadding(new Insets(10, 10, 10, 10));
    // Check if message has text, if not, show recipe link instead
    if (message.getText().trim().isEmpty()) {
        messageLabel.setText("No text message");
        messageLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
        messageLabel.setTextFill(Color.GRAY);
        innerMessageBox.setStyle("-fx-background-color: #F9F8F3; -fx-border-color: #d6e6de ; -fx-border-width: 2;");
    } else {
        messageLabel.setText(message.getText());
    }
    innerMessageBox.getChildren().add(messageLabel);
    ImageView readUnreadIcon;
    if (message.getSenderUsername().equals(observer.getUsername())) {
        if (message.isRead()) {
            readUnreadIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/read.png")));
        } else {
            readUnreadIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/send.png")));
        }
    } else {
        if (message.isRead()) {
            readUnreadIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/read.png")));
        } else {
            readUnreadIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/new.png")));
        }
    }
    readUnreadIcon.setFitHeight(30);
    readUnreadIcon.setFitWidth(30);
    HBox readUnreadBox = new HBox(readUnreadIcon);
    readUnreadBox.setAlignment(Pos.CENTER_RIGHT);
    innerMessageBox.getChildren().add(readUnreadBox);
    innerMessageBox.setMaxWidth(400);
    messageBox.getChildren().addAll(senderNameAndDate, innerMessageBox);
    if (message.getRecipe() != null) {
        Text recipText = new Text("Recipe: ");
        recipText.setFont(Font.font("Roboto", 20));
        Image star = new Image(getClass().getResourceAsStream("/images/star.png"));
        ImageView starIcon = new ImageView(star);
        starIcon.setFitWidth(25);
        starIcon.setFitHeight(25);
        Image unstar = new Image(getClass().getResourceAsStream("/images/unstar.png"));
        ImageView unstarIcon = new ImageView(unstar);
        unstarIcon.setFitWidth(25);
        unstarIcon.setFitHeight(25);
        ToggleButton starButton = new ToggleButton("", unstarIcon);
        starButton.setSelected(message.getRecipe().isStarred());
        if (message.getRecipe().isStarred()) {
            starButton.setGraphic(starIcon);
        }
        starButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                starButton.setGraphic(starIcon);
                observer.addRecipeToFavorite(message.getRecipe());
            } else {
                starButton.setGraphic(unstarIcon);
                observer.removeRecipeFromFavorite(message.getRecipe());
            }
        });
        Hyperlink recipeLink = new Hyperlink(message.getRecipe().getName());
        recipeLink.setFont(Font.font("ROBOTO", FontWeight.BOLD, 20));
        recipeLink.setOnAction(e -> observer.goToRecipe(message.getRecipe()));
        HBox recipeBox = new HBox(recipText, recipeLink, starButton);
        recipeBox.setAlignment(message.getSenderUsername().equals(observer.getUsername()) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.getChildren().add(recipeBox);
    }
    if (!message.getSenderUsername().equals(observer.getUsername())) {
      messageBox.setOnMouseClicked(e -> {

        observer.updateMessageIsRead(message.getId());
        message.markAsRead();
        messageBox.setStyle("-fx-text-fill: black;");
        refreshView();
    });
    
    if (!message.isRead()) {
      Tooltip tooltip = new Tooltip("Click to mark as read");
      tooltip.setFont(Font.font("Roboto", 16));
      tooltip.setShowDuration(Duration.millis(200));
      Tooltip.install(messageBox, tooltip);
    return messageBox;
  }
    }
    if (message.getSenderUsername().equals(observer.getUsername())) {
        messageBox.setOnMouseClicked(e -> {
            refreshView();
        });
    }
    return messageBox;
}


  /**
   * Refresh the view of the conversations and messages.
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
        .sorted(Comparator.comparing(Message::getDateTime).reversed())
        .collect(Collectors.toList())));
  }

  /**
   * Show an alert with the given alert type, title, and message.
   */
  private void showInlineStyledAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 18px;"
        + " -fx-background-color: #F9F8F3; -fx-border-color: #F9F8F3;");
    ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
    buttonBar.getButtons().forEach(button -> {
      button.setStyle("-fx-background-color: #3D405B;"
          + " -fx-text-fill: white; -fx-padding: 5 10 5 10;");
    });
    Label contentLabel = (Label) dialogPane.lookup(".content");
    contentLabel.setStyle("-fx-text-fill: #3D405B;");
    alert.showAndWait();
  }

  private boolean allSentMessagesRead(Conversation conversation, String username) {
    return conversation.getMessages().stream()
        .filter(message -> message.getSenderUsername().equals(username))
        .allMatch(Message::isRead);
  }
}
