package cookbook.controller;

import cookbook.view.MyFavouriteViewObserver;
import cookbook.model.CookbookFacade;
import cookbook.view.MyFavouriteView;
import javafx.scene.Node;


/**
 * Controller for the my favourite view.
 */
public class MyFavouriteController implements MyFavouriteViewObserver{
  private CookbookFacade model;
  private MainController mainController;
  private MyFavouriteView myFavouriteView;

  /**
   * Constructor for the my favourite controller.
   */
  public MyFavouriteController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.myFavouriteView = new MyFavouriteView();
    this.myFavouriteView.setObserver(this);
    this.mainController = mainController;
  }

  //Get the view
  public Node getView() {
    return this.myFavouriteView.getView();
  }


  
}
