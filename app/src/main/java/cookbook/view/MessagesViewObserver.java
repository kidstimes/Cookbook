package cookbook.view;

import cookbook.model.Conversation;
import cookbook.model.Recipe;
import java.util.ArrayList;

/**
 * Observer for the messages view.
 */
public interface MessagesViewObserver {

  void userLogout();

  void goToHomePage();

  void goToBrowser();

  void goToAddRecipe();

  void goToWeeklyDinner();

  void goToMyFavorite();

  void goToShoppingList();

  void goToMessages();

  void goToRecipe(Recipe recipe);

  void goToHelp();

  void goToAccount();

  void updateMessageIsRead(int messageId);

  void replyMessage(String receiverUsername, String reply);


  String getUsername();

  ArrayList<Conversation> getConversations();
  
}
