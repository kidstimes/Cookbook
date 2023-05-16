package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.Recipe;
import cookbook.view.MessagesView;
import cookbook.view.MessagesViewObserver;
import javafx.scene.Node;

/**
 * Controller for the messages view.
 */
public class MessagesController extends BaseController implements MessagesViewObserver{
  private MessagesView messagesView;

  /** Constructor for the messages controller.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */
  public MessagesController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.messagesView = new MessagesView(model.getUserDisplayName());
    this.messagesView.setObserver(this);
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

  
}
