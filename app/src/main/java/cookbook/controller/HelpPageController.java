package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.model.HelpSubsection;
import cookbook.view.HelpPageView;
import cookbook.view.HelpPageViewObserver;
import java.util.ArrayList;
import javafx.scene.Node;

/**
 * The controller for the help page.
 */
public class HelpPageController extends BaseController implements HelpPageViewObserver {
  private HelpPageView helpPageView;  

  /**
   * Constructor for the help page controller.
   */
  public HelpPageController(CookbookFacade model, MainController mainController) { 
    super(model, mainController);
    this.helpPageView = new HelpPageView(model.getUserDisplayName(), model.getHelpSections());
    this.helpPageView.setObserver(this);
  }

  //Get the view
  public Node getView() {
    return this.helpPageView.getView();
  }

  @Override
  public ArrayList<HelpSubsection> search(String searchText) {
    return this.model.searchHelpContent(searchText);
  }



}
