![image](https://github.com/user-attachments/assets/cf2a15af-8759-49ec-aafd-db9dbc8d9cb7)
# Overcooked Cookbook by group JAVA

## Overview

Overcooked Cookbook is a comprehensive recipe management system developed by Group Java for the 1DV508 course. This desktop application allows users to:

- Store and organize personal recipes
- Search recipes by ingredients, categories, or difficulty
- Plan weekly meals with an interactive calendar
- Generate shopping lists based on meal plans
- Share favorite recipes with other users
- Send messages in the application and chat with other users
- Track recipe history
- Admin feature to manage users

Built with JavaFX and MySQL, the application offers an intuitive interface for home cooks to digitize and organize their culinary collection.

## Running Instructions

1. Use `git clone` to make a local copy of the repository in a suitable folder on your computer.
2. Open the MySQL Workbench and make sure you have the same info here: User: root.  Host: localhost.  Password: 12345678. Alternativelly, you can also change the connection information in the Database class inside the database package to match your local database.
3. Then in MySQL workbench, open the `init_database.sql` script file in this repo , then execute the script and a database called cookbook will be initiated in the MySQL server.
4. Build the applicaiton using `./gradlew build` to start it quicker each time it runs. Run the application using `./gradlew run` - you should be prompted a folder mark and click it you can start using overcooked.
5. After logging in the app, you can log in using the admin account with username admin and password 123. You can also sign up as a new user, or add users from admin account under my account, admin page to start using the app. There are total 19 recipes already in it.
6. Have fun with Overcooked!

## The progression of our class diagram

The following diagrams are showing the state of our code structure at the end of each sprint starting from sprint 2 and going all the way up to sprint 6.

### Sprint 2

![sprint 2](img/ClassDiagram-sprint2.png)

### Sprint 3

![sprint 3](img/ClassDiagram-sprint3.png)

### Sprint 4

![sprint 4](img/ClassDiagram-sprint4.png)

### Sprint 5

![sprint 5](img/ClassDiagram-sprint5.png)

### Sprint 6

![sprint 6](img/ClassDiagram-sprint6.png)
