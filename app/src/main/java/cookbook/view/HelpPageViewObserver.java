package cookbook.view;

import cookbook.model.HelpSection;
import cookbook.model.HelpSubsection;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;


/**
 * Interface for the HelpPageView observer.
 */
public interface HelpPageViewObserver {

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void userLogout();

  void goToWeeklyDinner();

  void goToShoppingList();

  void goToMyFavorite();

  void goToMessages();

  void goToHelp();

  void goToAccount();

  ArrayList<HelpSubsection> searchHelpContent(String query);

 
  
  
}
