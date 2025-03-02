

CREATE TABLE users (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password_hash VARCHAR(255) NOT NULL,
is_admin BOOLEAN DEFAULT FALSE
);
CREATE TABLE recipes (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
description TEXT NOT NULL,
instructions TEXT NOT NULL,
servings INT NOT NULL
);
CREATE TABLE ingredients (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
recipe_id INT NOT NULL,
quantity INT NOT NULL,
measurementUnit VARCHAR(50) NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
CREATE TABLE tags (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(50) NOT NULL
);
CREATE TABLE recipe_tags (
id INT PRIMARY KEY AUTO_INCREMENT,
recipe_id INT NOT NULL,
tag_id INT NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);
CREATE TABLE comments (
id INT PRIMARY KEY AUTO_INCREMENT,
text TEXT NOT NULL,
recipe_id INT NOT NULL,
user_id INT NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE messages (
id INT PRIMARY KEY AUTO_INCREMENT,
text TEXT NOT NULL,
sender_id INT NOT NULL,
recipient_id INT NOT NULL,
recipe_id INT,
FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE SET NULL
);
CREATE TABLE WeekMenuRecipe (
weekmenu_id INT NOT NULL,
recipe_id INT NOT NULL,
day_of_week VARCHAR(10) NOT NULL,
PRIMARY KEY (weekmenu_id, recipe_id),
FOREIGN KEY (weekmenu_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);
CREATE TABLE ShoppingList (
shoppinglist_id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
weekmenu_id INT NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
FOREIGN KEY (weekmenu_id) REFERENCES WeekMenuRecipe(weekmenu_id) ON DELETE CASCADE
);


INSERT INTO recipes (name, description, instructions, servings)
VALUES
('Chicken Alfredo', 'Creamy pasta dish with chicken and parmesan cheese', '1. Cook pasta according to package instructions. 2. In a separate pan, cook chicken until browned. 3. Add heavy cream and parmesan cheese to the chicken and cook until thickened. 4. Serve chicken mixture over cooked pasta.', 2),
('Beef Tacos', 'Tacos filled with seasoned ground beef and toppings', '1. Brown ground beef in a pan. 2. Add taco seasoning and water to the beef and simmer. 3. Warm tortillas in a separate pan. 4. Assemble tacos with beef and desired toppings.', 2),
('Spaghetti Bolognese', 'Classic Italian dish with spaghetti and tomato-based sauce', '1. Cook spaghetti according to package instructions. 2. In a separate pan, cook ground beef until browned. 3. Add chopped onions and garlic to the beef and cook until softened. 4. Add canned tomatoes, tomato paste, and seasonings to the beef mixture and simmer. 5. Serve beef mixture over cooked spaghetti.', 2),
('Caprese Salad', 'Salad made with fresh mozzarella, tomatoes, and basil', '1. Slice fresh mozzarella and tomatoes. 2. Layer mozzarella and tomato slices on a plate. 3. Drizzle with olive oil and balsamic vinegar. 4. Sprinkle with salt, pepper, and chopped fresh basil.', 2),
('Grilled Cheese', 'Classic sandwich made with bread and melted cheese', '1. Butter two slices of bread. 2. Place cheese between the slices of bread. 3. Cook sandwich on a hot skillet until bread is toasted and cheese is melted.', 2);

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





INSERT INTO recipes (name, description, instructions, servings)
VALUES ('Beef and Penne Pasta', 'Pasta dish with ground beef and penne', '1. Cook penne pasta according to package instructions. 2. Brown ground beef in a separate pan. 3. Add onion and garlic to the beef and cook until softened. 4. Add canned tomatoes, tomato paste, and taco seasoning to the beef and simmer. 5. Serve beef mixture over cooked penne pasta.', 2);


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


INSERT INTO recipes (name, description, instructions, servings)
VALUES ('Chicken Parmesan', 'Breaded chicken served with tomato sauce and melted cheese', '1. Preheat the oven to 375°F. 2. Pound chicken breasts until they are even in thickness. 3. Mix breadcrumbs, parmesan cheese, and Italian seasoning in a shallow dish. 4. Dip chicken in beaten egg, then coat with breadcrumb mixture. 5. Heat oil in a skillet and cook chicken until browned. 6. Transfer chicken to a baking dish, top with tomato sauce and mozzarella cheese. 7. Bake for 20-25 minutes or until cheese is melted and bubbly.', 4);

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



INSERT INTO recipes (name, description, instructions, servings)
VALUES ('Beef and Broccoli Stir-Fry', 'Stir-fried beef and broccoli in a savory sauce', '1. Cut beef into thin strips and marinate in soy sauce, cornstarch, and sugar. 2. In a wok or large skillet, heat oil over high heat. 3. Add garlic and ginger and stir-fry for 30 seconds. 4. Add beef and stir-fry until browned. 5. Add broccoli and stir-fry for 2-3 minutes. 6. Mix together soy sauce, oyster sauce, and cornstarch. 7. Add sauce to the wok and stir-fry until thickened.', 4);

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



INSERT INTO recipes (name, description, instructions, servings)
VALUES ('Caprese Stuffed Chicken', 'Chicken breasts stuffed with mozzarella cheese, tomatoes, and basil', '1. Preheat oven to 400°F. 2. Cut a pocket in each chicken breast. 3. Stuff each pocket with slices of mozzarella cheese, tomato, and fresh basil. 4. Season chicken with salt and pepper. 5. Heat oil in a skillet over medium-high heat. 6. Add chicken and sear until browned. 7. Transfer chicken to a baking dish and bake for 20-25 minutes or until chicken is cooked through and cheese is melted.', 4);
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

