package cookbook.view;

public interface AccountViewObserver {
  
    void goToBrowser();
  
    void goToAddRecipe();
  
    void userLogout();
  
    void goToWeeklyDinner();
  
    void goToShoppingList();
  
    void goToMyFavorite();
  
    void goToMessages();
  
    void handlePasswordChange(String oldPassword, String newPassword);
  
    void goToHomePage();
  
    void changeDisplayName(String newDisplayName);
  
    void goToHelp();
  
    void goToAdmin();

    void goToAccount();
  
}
