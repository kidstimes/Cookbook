package cookbook.controller;


import cookbook.view.HomePageView;
import cookbook.view.HomePageViewObserver;
import javafx.scene.Node;

public class HomePageController implements HomePageViewObserver, BaseController {
    private ControllerManager controllerManager;
    private HomePageView homePageView;


    public HomePageController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        this.homePageView = new HomePageView();
        this.homePageView.setObserver(this);
    }


    public String getTitle() {
        return "HomePage";
    }

    public Node getView() {
        return this.homePageView.getView();
    }

    public void handleBrowseRecipesClicked() {
        this.controllerManager.showBrowserView();
    }
}

  

