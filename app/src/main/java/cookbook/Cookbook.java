package cookbook;

import cookbook.model.CookbookFacade;
import cookbook.view.UserInterface;
import cookbook.controller.Controller;

public class Cookbook {
    
    public static void main(String args[]) {
        CookbookFacade model = new CookbookFacade();
        UserInterface view = new UserInterface();
        Controller controller = new Controller(model, view);

        controller.start();
    }

}
