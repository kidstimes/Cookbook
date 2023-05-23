package cookbook.controller;

import java.util.ArrayList;

import cookbook.model.Conversation;
import cookbook.model.CookbookFacade;
import cookbook.model.Message;
import cookbook.model.Recipe;
import cookbook.view.MessagesView;
import cookbook.view.MessagesViewObserver;
import javafx.scene.Node;

/**
 * Controller for the messages view.
 */
public class MessagesController extends BaseController implements MessagesViewObserver {
  private MessagesView messagesView;

  /** Constructor for the messages controller.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */
  public MessagesController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.messagesView = new MessagesView(model.getUserDisplayName(),
         model.getConversations(), this);

  }



  /**
   * Get the messages view.
   */
  public Node getView() {
    return messagesView.getView();
  }
  


  @Override
  public void goToRecipe(Recipe recipe) {
    mainController.goToRecipe(recipe);
  }



  @Override
  public void updateMessageIsRead(int messageId) {
    model.updateMessageIsRead(messageId);
  }

  @Override
  public boolean replyMessage(String receiverUsername, String reply) {
    return model.replyMessage(receiverUsername, reply);
  }


  @Override
  public String getUsername() {
    return model.getUserName();
  }

  @Override
  public ArrayList<Conversation> getConversations() {
    return model.getConversations();
  }

  @Override
  public String getDisplayNameByUsername(String username) {
    return model.getDisplayNameByUsername(username);
  }

  @Override
  public void addRecipeToFavorite(Recipe recipe) {
    model.addRecipeToFavorites(recipe);
  }

  @Override
  public void removeRecipeFromFavorite(Recipe recipe) {
    model.removeRecipeFromFavorites(recipe);
  }

  @Override
  public Message getLatestMessage() {
    return model.getLatestMessage();
  }

}
