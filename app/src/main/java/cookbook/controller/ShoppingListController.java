package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.ShoppingListView;
import cookbook.view.ShoppingListViewObserver;
import javafx.scene.Node;

public class ShoppingListController extends BaseController implements ShoppingListViewObserver {
  private ShoppingListView shoppingListView;
  private String displayName;

  public ShoppingListController(CookbookFacade model, MainController mainController, String displayName) {
    super(model, mainController);
    this.shoppingListView = new ShoppingListView(displayName);
    this.shoppingListView.setObserver(this);
  }

  @Override
  public void goToShoppingList() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'goToShoppingList'");
  }


  public Node getView() {
    return this.shoppingListView.getView();
  }


  
}
