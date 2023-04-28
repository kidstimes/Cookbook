package cookbook.controller;

import javafx.scene.Node;
import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;

/**
 * Controller for managing the home page.
 */
public class HomePageController implements HomePageViewObserver, BaseController {

    private ControllerManager controllerManager;
    private HomePageView homePageView;

    /**
     * Home Page Controller Constructor.
     *
     * @param controllerManager the main controller (used to navigate to other controllers)
     */
    public HomePageController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.homePageView = new HomePageView();
        this.homePageView.setObserver(this);
    }

    @Override
    public Node getView() {
        return this.homePageView.getView();
    }

    @Override
    public void handleBrowseRecipesClicked() {
        this.controllerManager.showBrowserView();
    }

    @Override
    public void handleAddRecipeClicked() {
        this.controllerManager.showAddRecipeView();
    }
}
