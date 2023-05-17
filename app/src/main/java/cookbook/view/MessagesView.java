package cookbook.view;

import java.util.ArrayList;

import cookbook.model.Message;
import cookbook.model.Recipe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class MessagesView {
  private BorderPane view;
  private MessagesViewObserver observer;
  private String displayName;
  private ListView<VBox> inboxView;
  private ListView<VBox> outboxView;
  private Image open;
  private Image close;
  private ArrayList<Message> inbox;
  private ArrayList<Message> outbox;


  /**
   * Constructor for the MessagesView.
   */
  public MessagesView(String displayName, ArrayList<Message> inbox, ArrayList<Message> outbox) {
    this.view = new BorderPane();
    this.displayName = displayName;
    this.inbox = inbox;
    this.outbox = outbox;

    open = new Image(getClass().getResourceAsStream("/images/open.png"));
    close = new Image(getClass().getResourceAsStream("/images/close.png"));

    inboxView = new ListView<>();
    outboxView = new ListView<>();
    initLayout();
    showInbox(inbox);
    showOutbox(outbox);
  }

  
  
  /** Set the observer for this view.
  *
  * @param observer the observer
  */
  public void setObserver(MessagesViewObserver observer) {
    this.observer = observer;
  }

  //get the view
  public Node getView() {
    return view;
  }

  /**
   * Initialize the layout.
   */
  public void initLayout() {
    // create a vbox to hold the menu buttons
    VBox sidebar = new VBox(20);
    sidebar.setStyle("-fx-padding: 50px 20px 20px 20px;");
    Text welcomeText = new Text(displayName + ", welcome!");
    welcomeText.setFont(Font.font("Roboto", 28));
    sidebar.getChildren().add(welcomeText);

    Button[] sidebarButtons = {
      createButton("Home Page", e -> observer.goToHomePage()),
      createButton("Browse Recipes", e -> observer.goToBrowser()),
      createButton("Add a Recipe", e -> observer.goToAddRecipe()),
      createButton("Weekly Dinner List", e -> observer.goToWeeklyDinner()),
      createButton("My Favorites", e -> observer.goToMyFavorite()),
      createButton("My Shopping List", e -> observer.goToShoppingList()),
      createButton("Messages", e -> observer.goToMessages()),
      createButton("My Account", e -> observer.goToAccount())
      };

    for (Button button : sidebarButtons) {
      sidebar.getChildren().add(button);
    }
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    sidebar.getChildren().add(spacer);
    HBox logoutHelpBox = new HBox(10);
    Hyperlink logoutButton = new Hyperlink("Logout");
    logoutButton.setFont(Font.font("Roboto", 14));
    logoutButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    logoutButton.setOnAction(e -> {
      observer.userLogout();
    });

    Region hspacer = new Region();  // This will take up as much space as possible
    HBox.setHgrow(hspacer, Priority.ALWAYS); 
    
    Button helpButton = new Button("Help");
    helpButton.setFont(Font.font("Roboto", 14));
    helpButton.setStyle("-fx-background-color: #FFFFFF; -fx-effect: null;-fx-cursor: hand;");
    helpButton.setOnAction(e -> {
      observer.goToHelp();
    });
    
    logoutHelpBox.getChildren().addAll(logoutButton, hspacer, helpButton);
    logoutHelpBox.setAlignment(Pos.CENTER_LEFT);  
    
    sidebar.getChildren().add(logoutHelpBox); 
    view.setLeft(sidebar);

    Button inboxButton = new Button("Inbox");
    inboxButton.setOnAction(e -> view.setCenter(inboxView));

    Button outboxButton = new Button("Outbox");
    outboxButton.setOnAction(e -> view.setCenter(outboxView));

    HBox buttonBox = new HBox(inboxButton, outboxButton);


    view.setCenter(inboxView);
    view.setRight(buttonBox);

  }

  public void showInbox(ArrayList<Message> messages) {
    inboxView.getItems().clear();
    for (Message message : inbox) {
        inboxView.getItems().add(createMessageView(message));
    }
  }

  public void showOutbox(ArrayList<Message> messages) {
    outboxView.getItems().clear();
    for (Message message : outbox) {
        outboxView.getItems().add(createMessageView(message));
    }
  }

  private VBox createMessageView(Message message) {
    VBox messageView = new VBox(5);

    ImageView envelopeIcon = new ImageView(message.isRead() ? open : close);
    envelopeIcon.setFitHeight(20);
    envelopeIcon.setFitWidth(20);

    Recipe recipe = message.getRecipe();
    Text sender = new Text(message.getSenderUserName());

    Hyperlink recipeLink = new Hyperlink(message.getRecipe().getName());
    recipeLink.setVisible(false);
    recipeLink.setOnAction(e -> observer.goToRecipe(recipe));
    Text messageText = new Text(message.getText());
    messageText.setVisible(false);

    messageView.getChildren().addAll(envelopeIcon, sender, recipeLink, messageText);

    // Set up a mouse click event on the sender Text to toggle visibility
    sender.setOnMouseClicked(e -> {
        boolean visibility = !recipeLink.isVisible();
        recipeLink.setVisible(visibility);
        messageText.setVisible(visibility);
        if (visibility) {
            message.read();
            envelopeIcon.setImage(open);
            messageView.setStyle(""); // Clear background color
        }
    });
    if (!message.isRead()) {
        messageView.setStyle("-fx-background-color: grey;");
    }

    return messageView;
}

  /** Create styled button with the given text and event handler.
   *
   * @param text is the text to display on the button
   * @param eventHandler is the event handler to execute when the button is clicked.
   * @return the created button
   */
  private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color:#F2CC8F ; -fx-text-fill:#3D405B; -fx-cursor: hand;");
    button.setFont(Font.font("Roboto", 18));
    button.setMinWidth(180);
    button.setMaxWidth(200); 
    button.setOnAction(eventHandler);
    return button;
  }
}

  

