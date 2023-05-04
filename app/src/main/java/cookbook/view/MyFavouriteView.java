package cookbook.view;

import cookbook.view.MyFavouriteViewObserver;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;




public class MyFavouriteView{
  private MyFavouriteViewObserver observer;
  private BorderPane view;


  /**
   * Constructor for the my favourite view.
   */
  public MyFavouriteView() {
    this.view = new BorderPane();

  }

  //Set the observer for the view
  public void setObserver(MyFavouriteViewObserver observer) {
    this.observer = observer;
  }

  //Get the view
  public Node getView() {
    return view;
  }



  
}
