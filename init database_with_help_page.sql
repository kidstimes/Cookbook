CREATE DATABASE IF NOT EXISTS cookbook;
use cookbook;
CREATE TABLE users (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password_hash VARCHAR(255) NOT NULL,
displayname VARCHAR(50) NOT NULL
);
CREATE TABLE recipes (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
description TEXT NOT NULL,
instructions TEXT NOT NULL,
user_id INT NOT NULL,
date_added DATE NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE RecipeEditHistory (
recipe_id INT NOT NULL,
user_id INT NOT NULL,
edit_date DATE NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE ingredients (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
recipe_id INT NOT NULL,
quantity float NOT NULL,
measurementUnit VARCHAR(50) NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
CREATE TABLE tags (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE recipe_tags (
id INT PRIMARY KEY AUTO_INCREMENT,
recipe_id INT NOT NULL,
tag_id INT NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
UNIQUE (recipe_id, tag_id)
);
CREATE TABLE comments (
id INT PRIMARY KEY AUTO_INCREMENT,
text TEXT NOT NULL,
recipe_id INT NOT NULL,
user_id INT,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE messages (
id INT PRIMARY KEY AUTO_INCREMENT,
text TEXT,
sender_id INT NOT NULL,
recipient_id INT NOT NULL,
recipe_id INT,
is_read BOOLEAN NOT NULL,
send_date DateTime NOT NULL,
FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
CREATE TABLE WeeklyDinner (
user_id INT NOT NULL,
recipe_id INT NOT NULL,
dinner_date date NOT NULL,
PRIMARY KEY (user_id, recipe_id, dinner_date),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
UNIQUE (user_id, recipe_id, dinner_date)
);
CREATE TABLE ShoppingList (
shoppinglist_id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
ingredient_name VARCHAR(255) NOT NULL,
list_quantity FLOAT NOT NULL,
measurementUnit VARCHAR(50) NOT NULL,
week_number INT NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
UNIQUE (user_id, ingredient_name, week_number)
);
CREATE TABLE PersonalTags (
user_id INT NOT NULL,
recipe_id INT NOT NULL,
tag_id INT NOT NULL,
PRIMARY KEY(user_id, tag_id, recipe_id),
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
UNIQUE (user_id, tag_id, recipe_id)
);
CREATE TABLE favorites (
user_id INT NOT NULL,
recipe_id INT NOT NULL,
PRIMARY KEY(user_id, recipe_id),
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
UNIQUE (user_id, recipe_id)
);
CREATE TABLE HelpSection (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100)
);
CREATE TABLE HelpSubsection (
  id INT AUTO_INCREMENT PRIMARY KEY,
  section_id INT,
  title VARCHAR(100),
  text TEXT,
  FOREIGN KEY (section_id) REFERENCES HelpSection(id)
);
INSERT INTO users (id, userName, password_hash, displayname)
VALUES (1, 'admin', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','admin');
INSERT INTO recipes (name, description, instructions, user_id, date_added) 
VALUES 
('Chicken Alfredo', 'Creamy pasta dish with chicken and parmesan cheese', '1. Cook pasta according to package instructions. \n2. In a separate pan, cook chicken until browned. \n3. Add heavy cream and parmesan cheese to the chicken and cook until thickened. \n4. Serve chicken mixture over cooked pasta.', 1, '2023-05-16'),
('Beef Tacos', 'Tacos filled with seasoned ground beef and toppings', '1. Brown ground beef in a pan. \n2. Add taco seasoning and water to the beef and simmer. \n3. Warm tortillas in a separate pan. \n4. Assemble tacos with beef and desired toppings.', 1, '2023-05-16'),
('Spaghetti Bolognese', 'Classic Italian dish with spaghetti and tomato-based sauce', '1. Cook spaghetti according to package instructions. \n2. In a separate pan, cook ground beef until browned. \n3. Add chopped onions and garlic to the beef and cook until softened. \n4. Add canned tomatoes, tomato paste, and seasonings to the beef mixture and simmer. \n5. Serve beef mixture over cooked spaghetti.', 1, '2023-05-16'),
('Caprese Salad', 'Salad made with fresh mozzarella, tomatoes, and basil', '1. Slice fresh mozzarella and tomatoes. \n2. Layer mozzarella and tomato slices on a plate. \n3. Drizzle with olive oil and balsamic vinegar. \n4. Sprinkle with salt, pepper, and chopped fresh basil.', 1, '2023-05-16'),
('Grilled Cheese', 'Classic sandwich made with bread and melted cheese', '1. Butter two slices of bread. \n2. Place cheese between the slices of bread. \n3. Cook sandwich on a hot skillet until bread is toasted and cheese is melted.', 1, '2023-05-16');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES
('Penne Pasta', 1, 12, 'oz'),
('Boneless Chicken Breast', 1, 1, 'lb'),
('Heavy Cream', 1, 2, 'cups'),
('Parmesan Cheese', 1, 1, 'cup'),
('Ground Beef', 2, 1, 'lb'),
('Taco Seasoning', 2, 1, 'package'),
('Tortillas', 2, 8, 'pieces'),
('Spaghetti', 3, 8, 'oz'),
('Ground Beef', 3, 1, 'lb'),
('Onion', 3, 1, 'medium'),
('Garlic', 3, 2, 'cloves'),
('Canned Tomatoes', 3, 28, 'oz'),
('Tomato Paste', 3, 6, 'oz'),
('Fresh Mozzarella', 4, 8, 'oz'),
('Tomatoes', 4, 2, 'large'),
('Basil', 4, 1, 'cup'),
('Olive Oil', 4, 2, 'tbsp'),
('Balsamic Vinegar', 4, 2, 'tbsp'),
('Bread', 5, 8, 'slices'),
('Cheddar Cheese', 5, 8, 'slices');
INSERT INTO tags (name) VALUES
('vegan'),
('vegetarian'),
('lactose free'),
('gluten free'),
('starter'),
('main course'),
('dessert and sweets');
INSERT INTO recipe_tags (recipe_id, tag_id) VALUES
(1, 6), -- Chicken Alfredo is a main course
(2, 6), -- Beef Tacos is a main course
(2, 4), -- Beef Tacos is gluten-free (assuming the tortillas are gluten-free)
(3, 6), -- Spaghetti Bolognese is a main course
(4, 5), -- Caprese Salad is a starter
(4, 2), -- Caprese Salad is vegetarian
(4, 4), -- Caprese Salad is gluten-free
(5, 6), -- Grilled Cheese is a main course
(5, 2); -- Grilled Cheese is vegetarian
INSERT INTO recipes (name, description, instructions, user_id, date_added)
VALUES ('Beef and Penne Pasta', 'Pasta dish with ground beef and penne', '1. Cook penne pasta according to package instructions. \n2. Brown ground beef in a separate pan. \n3. Add onion and garlic to the beef and cook until softened. \n4. Add canned tomatoes, tomato paste, and taco seasoning to the beef and simmer. \n5. Serve beef mixture over cooked penne pasta.',1, '2023-05-16');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES 
('Penne Pasta', 6, 8, 'ounces'),
('Ground Beef', 6, 1, 'pound'),
('Onion', 6, 1, 'medium'),
('Garlic', 6, 3, 'cloves'),
('Canned Tomatoes', 6, 14.5, 'ounces'),
('Tomato Paste', 6, 2, 'tablespoons'),
('Taco Seasoning', 6, 2, 'tablespoons');
INSERT INTO recipe_tags (recipe_id, tag_id)
VALUES (6, 1), (6, 3), (6, 4);
INSERT INTO recipes (name, description, instructions, user_id, date_added)
VALUES ('Chicken Parmesan', 'Breaded chicken served with tomato sauce and melted cheese', '1. Preheat the oven to 375°F. \n2. Pound chicken breasts until they are even in thickness. \n3. Mix breadcrumbs, parmesan cheese, and Italian seasoning in a shallow dish. \n4. Dip chicken in beaten egg, then coat with breadcrumb mixture. \n5. Heat oil in a skillet and cook chicken until browned. \n6. Transfer chicken to a baking dish, top with tomato sauce and mozzarella cheese. \n7. Bake for 20-25 minutes or until cheese is melted and bubbly.', 1, '2023-05-16');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES 
('Boneless Chicken Breast', 7, 4, 'pieces'),
('Breadcrumbs', 7, 1, 'cup'),
('Parmesan Cheese', 7, 1/2, 'cup'),
('Italian Seasoning', 7, 1, 'teaspoon'),
('Egg', 7, 2, 'large'),
('Olive Oil', 7, 2, 'tablespoons'),
('Tomato Sauce', 7, 1, 'cup'),
('Mozzarella Cheese', 7, 1, 'cup');
INSERT INTO recipe_tags (recipe_id, tag_id)
VALUES (7, 1), (7, 2), (7, 4);
INSERT INTO recipes (name, description, instructions, user_id, date_added)
VALUES ('Beef and Broccoli Stir-Fry', 'Stir-fried beef and broccoli in a savory sauce', '1. Cut beef into thin strips and marinate in soy sauce, cornstarch, and sugar. \n2. In a wok or large skillet, heat oil over high heat. \n3. Add garlic and ginger and stir-fry for 30 seconds. \n4. Add beef and stir-fry until browned. \n5. Add broccoli and stir-fry for 2-3 minutes. \n6. Mix together soy sauce, oyster sauce, and cornstarch. \n7. Add sauce to the wok and stir-fry until thickened.',1, '2023-05-16');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES 
('Beef Sirloin Steak', 8, 1, 'pound'),
('Soy Sauce', 8, 2, 'tablespoons'),
('Cornstarch', 8, 1, 'tablespoon'),
('Sugar', 8, 1, 'teaspoon'),
('Vegetable Oil', 8, 2, 'tablespoons'),
('Garlic', 8, 2, 'cloves'),
('Ginger', 8, 1, 'inch'),
('Broccoli Florets', 8, 4, 'cups'),
('Oyster Sauce', 8, 2, 'tablespoons');
INSERT INTO recipe_tags (recipe_id, tag_id)
VALUES (8, 1), (8, 2), (8, 5);
INSERT INTO recipes (name, description, instructions, user_id, date_added)
VALUES ('Caprese Stuffed Chicken', 'Chicken breasts stuffed with mozzarella cheese, tomatoes, and basil', '1. Preheat oven to 400°F. \n2. Cut a pocket in each chicken breast. \n3. Stuff each pocket with slices of mozzarella cheese, tomato, and fresh basil. \n4. Season chicken with salt and pepper. \n5. Heat oil in a skillet over medium-high heat. \n6. Add chicken and sear until browned. \n7. Transfer chicken to a baking dish and bake for 20-25 minutes or until chicken is cooked through and cheese is melted.',1, '2023-05-16');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES
('Boneless Chicken Breast', 9, 4, 'pieces'),
('Mozzarella Cheese', 9, 4, 'slices'),
('Tomatoes', 9, 2, 'medium'),
('Fresh Basil', 9, 8, 'leaves'),
('Salt', 9, 1/2, 'teaspoon'),
('Black Pepper', 9, 1/4, 'teaspoon'),
('Olive Oil', 9, 2, 'tablespoons');
INSERT INTO recipe_tags (recipe_id, tag_id)
VALUES (9, 1), (9, 2), (9, 4);

INSERT INTO HelpSection(id, title) VALUES (1, 'Getting Started');
INSERT INTO HelpSection(id, title) VALUES (2, 'Recipe Management');
INSERT INTO HelpSection(id, title) VALUES (3, 'Customization and Organization');
INSERT INTO HelpSection(id, title) VALUES (4, 'Shopping List and Sharing');
INSERT INTO HelpSection(id, title) VALUES (5, 'Help');
INSERT INTO HelpSection(id, title) VALUES (6, 'Administration');

INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (1, 'Welcome', 'Welcome to Overcooked, a digital cookbook application for desktop computers. This help page will guide you through the features and functionalities of the application. If you have any questions or need assistance, refer to the sections below.', 1);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (2, 'Logging In & Signing Up', 'After a short animation, you will be prompted to log in. Enter your login credentials to access your account. If you are a new user, you can ask the administrator to create an account for you or register yourself. The password must be longer than 5 characters long and the username should be unique. You can also choose a display name.', 1);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (3, 'Favorite Recipes', 'Once logged in, you can quickly access your favorite recipes. Simply click on the “My Favorites" section in the sidebar to view and access your favorite recipes. You can click the star on a recipe to add or remove a recipe to/from your favorites.', 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (4, "Weekly Dinner Lists", "To plan your meals for the week, you can create weekly dinner lists, which allow you to organize and schedule the recipes you are going to cook for dinner in advance. You can add a recipe to a dinner by first clicking on the recipe, choosing the date of the dinner, and clicking “Add to Weekly Dinner”. Click on the \"Weekly Dinner List\" section in the sidebar to see your weekly dinner plans. You can navigate through the weeks (past and future) and also delete recipes that you do not want any more in your dinners.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (5, "Searching for Recipes", "If you want to search for recipes you can do that by going to the “Browse Recipes” section of the sidebar. There are different search options available:
Search by a recipe’s name: Enter the name of the recipe you are looking for.
Search by ingredient(s): Enter one or more ingredients separated by whitespace to find recipes containing those ingredients.
Search by tag(s): Select tags by checking the box next to them to find recipes with those tags.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (6, "Adding a New Recipe", "Click the \"Add a Recipe\" option in the sidebar to add a new recipe to the cookbook. Fill in the required details, such as the recipe name, short description, ingredients, and step-by-step directions. You can also assign tags to the recipe to make it easier to find later.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (7, "Viewing Recipe Details", "When you find a recipe you're interested in, click on it to see the detailed view, where you can see the ingredients and step-by-step directions, tags, and editing history of the recipe. It is also possible to add more tags to the recipe as well as write comments.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (8, "Adjusting Recipe Serving Size", "Recipes in Overcooked can be adjusted to accommodate different serving sizes. When viewing a recipe, you can select the number of persons the recipe is for. The ingredient quantities will be adjusted accordingly.", 3);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (9, "Adding Tags to Recipes", "Tags help you categorize and organize your recipes. You can add one of the predefined tags or create your own tags and add them to the recipes. Tags make searching for recipes with specific attributes or dietary preferences easier.", 3);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (10, "Adding and Removing Comments", "You can add comments to recipes to provide additional information or personal notes for everyone to see. Comments can be used to highlight specific details or share your experiences with a recipe. You can also edit or remove your own comments if needed.", 3);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (11, "Generating a Shopping List", "Overcooked allows you to generate a shopping list for a week based on your selected recipes in weekly dinners. The shopping list will display the necessary ingredients, ensuring you have a comprehensive list for your grocery shopping. The list can be printed or saved as a PDF for convenience.", 4);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (12, "Modifying the Shopping List", "If you already have some items at home or wish to remove certain items from the shopping list, you can easily modify them. Edit the shopping list to adapt the quantity or completely remove items that you don't need to purchase. Click the “Refresh Shopping List” button once you modify the weekly dinner list to update the shopping list accordingly. ", 4);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (13, "Sending Recipes to Other Users", "Overcooked supports recipe sharing among users of the application. If you come across a recipe that you think another user would enjoy, you can send it to them. Simply go to the bottom of the detailed view of a recipe, choose a user to send the message to, optionally add a text message to it and click “Send”. By sending a recipe to a user you start a conversation. That allows you and the receiving user to start texting each other from the “Messages” section of the sidebar.", 4);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (14, "Messaging Other Users", "By starting a conversation with another user (by sending a recipe from the detailed view) you can now start exchanging text messages from the “Messages” section of the sidebar. In the “Messages” section you can find your conversations with other users and see your unread messages. In order to mark a message as read you can click on the envelope icon on the bottom right corner of a message (it should turn into a double checkmark).", 4);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (15, "Accessing the Help System", "If you need assistance or want to learn more about the application's features, you can access this help system. Here you can find information and instructions on how to use the various functionalities of the application.", 5);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (16, "Searching the Help System", "To quickly find information on a specific topic within the help system, you can use the search functionality. Enter keywords to search for specific instructions or explanations. We hope this guide has provided you with all the necessary information to easily navigate and use the Overcooked application. Enjoy cooking and exploring new recipes!", 5);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (17, "Admin Functions", "The application includes an administrator account with special privileges. The admin is able to add, modify, and remove user accounts. Log in using the admin account credentials (username: admin, password:123 are the default). We recommend the admin change their password after the first login.", 6);







