package cookbook.controller;

import cookbook.view.UserInterface;
import cookbook.model.CookbookFacade;

public class Controller {

    private UserInterface view;
    private CookbookFacade model;

    public Controller(CookbookFacade model, UserInterface view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        // Get data from database and create objects
        runCookbook();
    }

    public void runCookbook() {
        // login stuff...

        view.displayHomePage();
        // String input = view.getHomeScreenOption();

        // if (input == "Browse") {
            while (true) {
                view.displayRecipes(model.getRecipes());

                // apply filters
                // get filters from view (user input)
                // apply filters in model
                // view.displayRecipes
                // model.getRecipesWithFilters());

                // if user goes back to home screen then
                // break;
            }
        // }
        // else if ( user wants to quit) {
            // end();
        // }
    }

    public void end() {
        // Put the data back to the database
        System.exit(0);
    }

}
