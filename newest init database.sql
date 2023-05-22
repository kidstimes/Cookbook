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
INSERT INTO HelpSection(id, title) VALUES (5, 'Help and Administration');

INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (1, 'Welcome', 'Welcome to Cookbook, a digital cookbook application for desktop computers. This help page will guide you through the features and functionalities of the application. If you have any questions or need assistance, refer to the sections below.', 1);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (2, 'Starting the Application', 'To start the Cookbook application, simply open the application on your desktop computer. Upon launching, you will be greeted with a short animation.', 1);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (3, 'Logging In', 'After the animation, you will be prompted to log in. Enter your login credentials to access your account. If you are a new user, contact the administrator to create an account for you or register yourself, the password must be longer than 5. If the username already exists, it will tell you the username has been taken.', 1);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (4, "Viewing Favorite Recipes", "Once logged in, you can quickly access your favorite recipes. These are the recipes you have marked as favorites for easy reference. Simply click on the Favorites section to view and access your favorite recipes. You can click the star or unstar to mark it as a favorite or not.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (5, "Creating Weekly Dinner Lists", "To plan your meals for the week, you can create weekly dinner lists. These lists allow you to organize and schedule your meals in advance. Click on the Weekly Dinner section to create and manage your weekly dinner plans. You can create the weekly dinner by using the available recipes. You can also edit the weekly dinner; it always shows the current week as a start page and can be chosen from marks on both sides or by writing the week number.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (6, "Searching for Recipes", "If you are looking for a specific recipe, you can use the search functionality. There are different search options available: Search by recipe name: Enter the name of the recipe you are looking for. Search by ingredient(s): Enter one or more ingredients to find recipes containing those ingredients. Search by tag(s): Select from a list of predefined tags or enter your own to find recipes with specific characteristics.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (7, "Browsing All Recipes", "To explore all the available recipes, click on the Browse Recipes section. This will provide you with a list of all the recipes in the application.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (8, "Adding a New Recipe", "If you have a new recipe, click the Add Recipe option. Fill in the required details, such as the recipe name, short description, ingredients, and detailed preparation steps(cannot be null for each). You can also assign tags to the recipe to make it easier to find later.", 2);
INSERT INTO HelpSubsection(id, title, text, section_id) VALUES (9, "Viewing Recipe Details", "When you find a recipe you are interested in, click on it to view its full details. The recipe will be displayed with formatting, including bold and larger text for the name, italics for the short description, and lists for ingredients and preparation steps.", 2);







