package cookbook.view;

import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The sidebar class for the application.
 */
public class Sidebar extends VBox {
  private Button activeButton;
  private HashMap<String, Button> buttons = new HashMap<>();
  private HashMap<String, Hyperlink> hyperlinks = new HashMap<>();
  private VBox hyperlinkBox = new VBox(10);

  /**
   * Constructor for the sidebar.
   */
  public Sidebar(String displayName) {
    setSpacing(20);
    setMaxWidth(250);
    setStyle("-fx-padding: 20px 0px 20px 0px;-fx-background-color: #F9F8F3;");
    this.setFillWidth(true);
    Label title = new Label("Welcome,");
    

    title.setPrefWidth(280);
    title.setWrapText(true);
    title.setFont(Font.font("Roboto", FontWeight.BOLD, 22));
    title.setStyle("-fx-text-fill:#3D405B;");
    title.setStyle("-fx-padding: 30px 20px 0px 40px;");
    title.setAlignment(Pos.CENTER_LEFT);
    Label subtitle = new Label(displayName);
    subtitle.setPrefWidth(280);
    subtitle.setWrapText(true);
    subtitle.setFont(Font.font("Roboto", FontWeight.BOLD, 22));
    subtitle.setStyle("-fx-text-fill:#3D405B;");
    subtitle.setStyle("-fx-padding: 0px 20px 30px 40px;");


    getChildren().addAll(title, subtitle);


  }

  /** Add a button to the sidebar.
   *
   * @param name the name of the button
   * @param eventHandler the event handler for the button
   */
  public void addButton(String name, EventHandler<ActionEvent> eventHandler, String imagePath) {
  
    // Create an image and an image view.
    Image image = new Image(getClass().getResourceAsStream(imagePath));
    ImageView imageView = new ImageView(image);
  
    // Optional: Set the size of the image.
    imageView.setFitHeight(18);
    imageView.setFitWidth(18);
  
    // Set the image view as the button's graphic.

    Button button = new Button(name);
    button.setGraphic(imageView);
    button.setStyle("-fx-background-color:#F9F8F3 ; -fx-text-fill:#3D405B; "
        + "-fx-cursor: hand;-fx-padding: 0px 20px 0px 30px;");
    button.setFont(Font.font("Roboto", 20));
    button.setMaxWidth(Double.MAX_VALUE);
    button.setMinHeight(43);
    button.setOnAction(e -> {
      if (activeButton != null) {
        activeButton.setStyle("-fx-background-color:#F9F8F3 ; "
            + "-fx-text-fill:#3D405B; -fx-cursor: hand; -fx-padding: 0px 20px 0px 30px;");
      }
      button.setStyle("-fx-background-color:#F2CC8F; -fx-text-fill:#3D405B; "
          + "-fx-cursor: hand; -fx-padding: 0px 20px 0px 30px;");
      activeButton = button;
      eventHandler.handle(e);
    });
    buttons.put(name, button);
    button.setAlignment(Pos.CENTER_LEFT);
    getChildren().add(button);
  }

  /** Add a hyperlink to the sidebar.
   *
   * @param name the name of the hyperlink
   * @param eventHandler  the event handler for the hyperlink
   */
  public void addHyperlink(String name, EventHandler<ActionEvent> eventHandler) {
    Hyperlink hyperlink = new Hyperlink(name);
    hyperlink.setFont(Font.font("Roboto", 18));
    hyperlink.setStyle("-fx-text-fill:#3D405B; -fx-cursor: hand;-fx-padding: 0px 20px 0px 40px;");
    hyperlink.setOnAction(eventHandler);
    hyperlinks.put(name, hyperlink);
    hyperlinkBox.getChildren().add(hyperlink);
  }

  /** Set the active button in the sidebar to make the button change color.
   *
   * @param name the name of the button
   */
  public void setActiveButton(String name) {
    Button button = buttons.get(name);
    if (button != null) {
      if (activeButton != null) {
        activeButton.setStyle("-fx-background-color: #F9F8F3; -fx-text-fill:#3D405B; "
            + "-fx-cursor: hand;-fx-padding: 0px 20px 0px 30px;");
      }
      button.setStyle("-fx-background-color:#F2CC8F; -fx-text-fill:#3D405B; "
          + "-fx-cursor: hand;-fx-padding: 0px 20px 0px 30px;");
      activeButton = button;
    }
  }

  /**
   * Finalize the layout of the sidebar.
   */
  public void finalizeLayout() {
    hyperlinkBox.setAlignment(Pos.BOTTOM_LEFT);
    VBox.setVgrow(hyperlinkBox, Priority.ALWAYS);
    getChildren().add(hyperlinkBox);
  }
}
