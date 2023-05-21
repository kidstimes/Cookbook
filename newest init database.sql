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
text TEXT NOT NULL,
sender_id INT NOT NULL,
recipient_id INT NOT NULL,
recipe_id INT,
is_read BOOLEAN NOT NULL,
send_date Date NOT NULL,
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
INSERT INTO users (id, userName, password_hash, displayname)
VALUES (1, 'admin', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','admin');
INSERT INTO recipes (name, description, instructions, user_id)
VALUES
('Chicken Alfredo', 'Creamy pasta dish with chicken and parmesan cheese', '1. Cook pasta according to package instructions. \n2. In a separate pan, cook chicken until browned. \n3. Add heavy cream and parmesan cheese to the chicken and cook until thickened. \n4. Serve chicken mixture over cooked pasta.', 1),
('Beef Tacos', 'Tacos filled with seasoned ground beef and toppings', '1. Brown ground beef in a pan. \n2. Add taco seasoning and water to the beef and simmer. \n3. Warm tortillas in a separate pan. \n4. Assemble tacos with beef and desired toppings.', 1),
('Spaghetti Bolognese', 'Classic Italian dish with spaghetti and tomato-based sauce', '1. Cook spaghetti according to package instructions. \n2. In a separate pan, cook ground beef until browned. \n3. Add chopped onions and garlic to the beef and cook until softened. \n4. Add canned tomatoes, tomato paste, and seasonings to the beef mixture and simmer. \n5. Serve beef mixture over cooked spaghetti.', 1),
('Caprese Salad', 'Salad made with fresh mozzarella, tomatoes, and basil', '1. Slice fresh mozzarella and tomatoes. \n2. Layer mozzarella and tomato slices on a plate. \n3. Drizzle with olive oil and balsamic vinegar. \n4. Sprinkle with salt, pepper, and chopped fresh basil.', 1),
('Grilled Cheese', 'Classic sandwich made with bread and melted cheese', '1. Butter two slices of bread. \n2. Place cheese between the slices of bread. \n3. Cook sandwich on a hot skillet until bread is toasted and cheese is melted.', 1);
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
INSERT INTO recipes (name, description, instructions, user_id)
VALUES ('Beef and Penne Pasta', 'Pasta dish with ground beef and penne', '1. Cook penne pasta according to package instructions. \n2. Brown ground beef in a separate pan. \n3. Add onion and garlic to the beef and cook until softened. \n4. Add canned tomatoes, tomato paste, and taco seasoning to the beef and simmer. \n5. Serve beef mixture over cooked penne pasta.',1);
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
INSERT INTO recipes (name, description, instructions, user_id)
VALUES ('Chicken Parmesan', 'Breaded chicken served with tomato sauce and melted cheese', '1. Preheat the oven to 375°F. \n2. Pound chicken breasts until they are even in thickness. \n3. Mix breadcrumbs, parmesan cheese, and Italian seasoning in a shallow dish. \n4. Dip chicken in beaten egg, then coat with breadcrumb mixture. \n5. Heat oil in a skillet and cook chicken until browned. \n6. Transfer chicken to a baking dish, top with tomato sauce and mozzarella cheese. \n7. Bake for 20-25 minutes or until cheese is melted and bubbly.', 1);
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
INSERT INTO recipes (name, description, instructions, user_id)
VALUES ('Beef and Broccoli Stir-Fry', 'Stir-fried beef and broccoli in a savory sauce', '1. Cut beef into thin strips and marinate in soy sauce, cornstarch, and sugar. \n2. In a wok or large skillet, heat oil over high heat. \n3. Add garlic and ginger and stir-fry for 30 seconds. \n4. Add beef and stir-fry until browned. \n5. Add broccoli and stir-fry for 2-3 minutes. \n6. Mix together soy sauce, oyster sauce, and cornstarch. \n7. Add sauce to the wok and stir-fry until thickened.',1);
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
INSERT INTO recipes (name, description, instructions, user_id)
VALUES ('Caprese Stuffed Chicken', 'Chicken breasts stuffed with mozzarella cheese, tomatoes, and basil', '1. Preheat oven to 400°F. \n2. Cut a pocket in each chicken breast. \n3. Stuff each pocket with slices of mozzarella cheese, tomato, and fresh basil. \n4. Season chicken with salt and pepper. \n5. Heat oil in a skillet over medium-high heat. \n6. Add chicken and sear until browned. \n7. Transfer chicken to a baking dish and bake for 20-25 minutes or until chicken is cooked through and cheese is melted.',1);
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
INSERT INTO recipes (name, description, instructions, user_id)
VALUES
('Chicken Caesar Salad', 'A classic chicken salad recipe, featuring crunchy croutons and a creamy, garlic dressing. Ideal for lunch with friends.', '1. Heat oven to 200C/fan 180C/gas 6. Tear the ciabatta into big, ragged croutons or, if you prefer, cut with a bread knife. Spread over a large baking sheet or tray and sprinkle over 2/3rds of the olive oil. \n2. Rub the oil into the bread and season with a little salt if you like (sea salt crystals are best for this). Bake for 8-10 mins, turning the croutons a few times during cooking so they brown evenly. \n3. Rub the skinless, boneless chicken breasts with the rest of the olive oil, season. Place pan over a medium heat for 1 min, until hot, but not smoking. Lay the chicken on the pan (it will sizzle if its hot enough) and leave for 4 mins. \n4. Turn the chicken, then cook for 4 mins more. Check if its cooked by poking the tip of a sharp knife into the thickest part; there should be no sign of pink and juices will run clear. \n5. Bash the garlic cloves with the flat of a knife and peel off the skin. Crush with a garlic crusher. Mash the anchovies with a fork against the side of a small bowl. \n6. Grate a handful of parmesan cheese and mix with the garlic, anchovies, mayonnaise and white wine vinegar. Season to taste. It should be the consistency of yogurt if yours is thicker, stir in a few tsps water to thin it. \n7. Shave the cheese with a peeler. Tear the romaine lettuce into large pieces and put in a large bowl. Pull chicken into bite-size strips and scatter half over the leaves, along with half the croutons. \n8. Add most of the dressing and toss with your fingers. Scatter the rest of the chicken and croutons, then drizzle with the remaining dressing. Sprinkle the parmesan on top and serve straight away.', 1),
('Double Chocolate Cookies', 'This recipe makes the perfect small batch double chocolate cookies so you do not need to share any cookies with anyone else!', '1. Preheat the oven to 350°F and line a baking sheet with parchment paper. Set aside. \n2. Place the softened butter and brown sugar in a medium bowl. Use a fork or a whisk to mix together until smooth and homogenous. \n3. Scrape down the sides of the bowl and add the egg yolky and vanilla extract. Mix until combined. \n4. Scrape down the bowl and add the cocoa powder, flour, baking soda and salt. Mix until just combined and no streaks of flour remain. \n5. Fold in chocolate chips until evenly distributed. \n6. Divide the cookie dough into 2 big cookies and place onto your prepared cookie sheet 2 inches apart. \n7. Bake at 350°F for 11 minutes or until the edges are set but the centers are still gooey. Allow to cool for 10 minutes on the baking sheet until set. Enjoy!', 1),
('Chipotle Grilled Tofu', 'Tofu gets quick-marinated in an herby, citrus marinade reminiscent of Mojo sauce but with smoky notes, then grilled to perfection. The tofu does not stick and it turns out perfectly chewy and crispy!', '1. Drain the tofu. Slice the tofu crosswise into ⅓-inch slabs, about 8 to 11 slices per block. Arrange on a paper towel-lined cutting board and add a few paper towels on top. Weight with a heavy cookbook or cast-iron skillet. Press for 15 minutes to remove excess water. \n2. Make the marinade: To a blender or food processor, add the cilantro, chipotle peppers and adobo sauce, garlic, orange zest, orange juice, lime juice, olive oil, sugar, cumin, salt, and black pepper to taste. Blend until relatively smooth. Season to taste with more salt and pepper needed. Reserve 4 tablespoons of the marinade. \n3. Transfer tofu slabs to a shallow dish or pan and pour the rest of the marinade on top, using a pastry brush to evenly coat the tofu. Allow to marinate for at least 15 minutes at room temperature, or up to 1 hour in the fridge. \n4. Prepare the grill. Preheat a gas grill (between medium and medium-high) with the lid on for at least 10 minutes to get the grates really hot. Scrub down with a heavy-duty grill brush to remove any gunk or drippings. \n5. Set a small bowl with a few tablespoons of oil near the grill and oil up a wad of paper towels in the oil. Use a set of tongs to rub the preheated grill grates with the oil-soaked towels. Let the oil smoke for 40 seconds or so, then rub with oil again. Repeat three to four times total to build up a good layer of seasoning. \n6. Immediately before or as soon as you add the tofu, turn the burners directly under the tofu to low heat and close the lid. Grill the tofu for 8 to 10 minutes, until nice grill marks form. Using a pair of tongs and/or a thin metal spatula, carefully flip each tofu slab and grill for another 7 to 10 minutes. \n7. Once the tofu is grilled, brush it with the reserved marinade.', 1),
('Soy Sauce Fried Rice', 'Make perfect soy sauce fried rice in just 5 minutes! This recipe calls for basic ingredients, and easy preparation.', '1. Gently loosen the rice grains with a fork. Set aside. \n2. In a small bowl, mix light soy sauce, dark soy sauce, sesame oil, sugar, and white pepper. Set aside. \n3. Heat a wok/skillet over high heat until hot, then pour in oil. Stir in minced garlic and fry until it turns a little golden. \n4. Add the cooked rice. Mix with the garlic then use a spatula to spread it into a thin layer. Leave to fry undisturbed for around 20 seconds. Then flip and toss to heat further (loosen any lumps with the spatula). \n5. Once the rice becomes piping hot, pour the sauce mixture over and toss well to coat the grains evenly. \n6. Stir in chopped scallions before removing the wok/skillet from the heat. Dish out and serve immediately.', 1),
('Coconut Mango Passion Fruit Trifle', 'This delicious dessert is made with a buttery biscuit base and topped with creamy coconut whip and a sweet and fruity tropical topping. It is a light dessert that is perfect to enjoy in the spring and summer months.', '1. Place a sieve over a saucepan and strain out the passion fruit seeds. Set the seeds aside if desired, to use for garnish. \n2. With the saucepan over medium-high heat, add the mango and sugar. Cook at a gentle simmer for 4 minutes, until slightly thickened. Set aside to cool. \n3. In a large bowl, use an electric hand mixer to whip the coconut cream for a few minutes. Then add the sugar and vanilla and whip again. \n4. Add the cookies and butter to a food processor and blend into a crumb. Then add 2 Tbsp of the cookie crumbs to each serving glass and press down to form a crust on the bottom. \n5. In the now-empty food processor, blend the mango mixture until puréed. \n6. Spoon a heaping 0.25 cup of the coconut whipping cream on top of the cookie layer, followed by a tablespoon of the passion fruit mixture. Garnish as desired, and enjoy!', 1);
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES
('Ciabatta', 10, 0.5, 'loaf(s)'),
('Olive oil', 10, 1.5, 'tablespoon(s)'),
('Chicken breast', 10, 1, 'piece(s)'),
('Romaine lettuce', 10, 1, 'bunche(s)'),
('Garlic', 10, 1, 'clove(s)'),
('Anchovy', 10, 1, 'piece(s)'),
('Parmesan', 10, 1, 'block(s)'),
('Mayonnaise', 10, 2.5, 'tablespoon(s)'),
('White wine vinegar', 10, 0.5, 'tablespoon(s)'),
('Unsalted butter', 11, 2, 'tablespoon(s)'),
('Brown sugar', 11, 4, 'tablespoon(s)'),
('Egg', 11, 1, 'yolk(s)'),
('Vanilla extract', 11, 0.25, 'teaspoon(s)'),
('Cocoa powder', 11, 1.5, 'tablespoon(s)'),
('Baking soda', 11, 0.125, 'teaspoon(s)'),
('Salt', 11, 0.125, 'teaspoon(s)'),
('All purpose flour', 11, 4, 'tablespoon(s)'),
('Chocolate chips', 11, 3, 'tablespoon(s)'),
('Tofu', 12, 1, 'block(s)'),
('Cilantro', 12, 0.75, 'cup(s)'),
('Chipotle peppers in adobo', 12, 2, 'piece(s)'),
('Adobo sauce', 12, 2, 'teaspoon(s)'),
('Garlic', 12, 6, 'clove(s)'),
('Orange zest', 12, 2, 'teaspoon(s)'),
('Orange juice', 12, 0.3, 'cup(s)'),
('Lime juice', 12, 2.5, 'tablespoon(s)'),
('Olive oil', 12, 0.25, 'cup(s)'),
('Sugar', 12, 1, 'tablespoon(s)'),
('Cumin', 12, 1, 'teaspoon(s)'),
('Salt', 12, 1, 'teaspoon(s)'),
('Black pepper', 12, 0.125, 'teaspoon(s)'),
('Cooked white rice', 13, 3, 'cup(s)'),
('Light soy sauce', 13, 1, 'tablespoon(s)'),
('Dark soy sauce', 13, 0.5, 'tablespoon(s)'),
('Sesame oil', 13, 0.5, 'teaspoon(s)'),
('Sugar', 13, 0.5, 'teaspoon(s)'),
('White pepper', 13, 0.25, 'teaspoon(s)'),
('Vegetable oil', 13, 1, 'tablespoon(s)'),
('Garlic', 13, 4, 'clove(s)'),
('Scallion', 13, 1, 'stalk(s)'),
('Passion fruit', 14, 4, 'tablespoon(s)'),
('Frozen mango chunks', 14, 3, 'tablespoon(s)'),
('Granulated sugar', 14, 0.5, 'teaspoon(s)'),
('Coconut cream', 14, 0.3, 'cup(s)'),
('Powdered sugar', 14, 2, 'teaspoon(s)'),
('Vanilla extract', 14, 0.25, 'teaspoon(s)'),
('Biscoff cookie', 14, 7, 'piece(s)'),
('Vegan butter', 14, 2, 'teaspoon(s)');
INSERT INTO recipe_tags (recipe_id, tag_id)
VALUES
(10, 6),
(11, 2),
(11, 7),
(12, 1),
(12, 2),
(12, 3),
(12, 4),
(12, 5),
(13, 1),
(13, 2),
(13, 3),
(13, 6),
(14, 1),
(14, 2),
(14, 3),
(14, 7);

