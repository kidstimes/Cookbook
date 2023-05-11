package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.AdminView;
import cookbook.view.AdminViewObserver;
import javafx.scene.Node;

/**
 * Controller for managing the admin view.
 */
public class AdminController implements AdminViewObserver{
  private CookbookFacade model;
  private MainController mainController;
  private AdminView adminView;

  public AdminController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.mainController = mainController;
    this.adminView = new AdminView(model.loadAllUsers());
    this.adminView.setObserver(this);
  }

  public void addUser(String username, String password, String displayName) {
    
  }

  public void deleteUser(String username) {
    
  }

  public void editUser(String username, String password, String displayName) {
    
  }

  public Node getView() {
    return this.adminView.getView();
  }

  public void logout() {
    mainController.userLogout();
  }

  public void goToAdmin() {
    mainController.goToAdmin();
  }


  
}
