CREATE DATABASE IF NOT EXISTS cookbook;
use cookbook;
CREATE TABLE users (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(255) COLLATE utf8mb4_bin UNIQUE NOT NULL,
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
('Grilled Cheese', 'Classic sandwich made with bread and melted cheese', '1. Butter two slices of bread. \n2. Place cheese between the slices of bread. \n3. Cook sandwich on a hot skillet until bread is toasted and cheese is melted.', 1, '2023-05-16'),
('Beef and Penne Pasta', 'Pasta dish with ground beef and penne', '1. Cook penne pasta according to package instructions. \n2. Brown ground beef in a separate pan. \n3. Add onion and garlic to the beef and cook until softened. \n4. Add canned tomatoes, tomato paste, and taco seasoning to the beef and simmer. \n5. Serve beef mixture over cooked penne pasta.',1, '2023-05-16'),
('Chicken Parmesan', 'Breaded chicken served with tomato sauce and melted cheese', '1. Preheat the oven to 375°F. \n2. Pound chicken breasts until they are even in thickness. \n3. Mix breadcrumbs, parmesan cheese, and Italian seasoning in a shallow dish. \n4. Dip chicken in beaten egg, then coat with breadcrumb mixture. \n5. Heat oil in a skillet and cook chicken until browned. \n6. Transfer chicken to a baking dish, top with tomato sauce and mozzarella cheese. \n7. Bake for 20-25 minutes or until cheese is melted and bubbly.', 1, '2023-05-16'),
('Beef and Broccoli Stir-Fry', 'Stir-fried beef and broccoli in a savory sauce', '1. Cut beef into thin strips and marinate in soy sauce, cornstarch, and sugar. \n2. In a wok or large skillet, heat oil over high heat. \n3. Add garlic and ginger and stir-fry for 30 seconds. \n4. Add beef and stir-fry until browned. \n5. Add broccoli and stir-fry for 2-3 minutes. \n6. Mix together soy sauce, oyster sauce, and cornstarch. \n7. Add sauce to the wok and stir-fry until thickened.',1, '2023-05-16'),
('Caprese Stuffed Chicken', 'Chicken breasts stuffed with mozzarella cheese, tomatoes, and basil', '1. Preheat oven to 400°F. \n2. Cut a pocket in each chicken breast. \n3. Stuff each pocket with slices of mozzarella cheese, tomato, and fresh basil. \n4. Season chicken with salt and pepper. \n5. Heat oil in a skillet over medium-high heat. \n6. Add chicken and sear until browned. \n7. Transfer chicken to a baking dish and bake for 20-25 minutes or until chicken is cooked through and cheese is melted.',1, '2023-05-16'),
('Chicken Caesar Salad', 'A classic chicken salad recipe. Ideal for lunch with friends.', '1. Heat oven to 200C/fan 180C/gas 6. Tear the ciabatta into big, ragged croutons or, if you prefer, cut with a bread knife. Spread over a large baking sheet or tray and sprinkle over 2/3rds of the olive oil. \n2. Rub the oil into the bread and season with a little salt if you like (sea salt crystals are best for this). Bake for 8-10 mins, turning the croutons a few times during cooking so they brown evenly. \n3. Rub the skinless, boneless chicken breasts with the rest of the olive oil, season. Place pan over a medium heat for 1 min, until hot, but not smoking. Lay the chicken on the pan (it will sizzle if its hot enough) and leave for 4 mins. \n4. Turn the chicken, then cook for 4 mins more. Check if its cooked by poking the tip of a sharp knife into the thickest part; there should be no sign of pink and juices will run clear. \n5. Bash the garlic cloves with the flat of a knife and peel off the skin. Crush with a garlic crusher. Mash the anchovies with a fork against the side of a small bowl. \n6. Grate a handful of parmesan cheese and mix with the garlic, anchovies, mayonnaise and white wine vinegar. Season to taste. It should be the consistency of yogurt if yours is thicker, stir in a few tsps water to thin it. \n7. Shave the cheese with a peeler. Tear the romaine lettuce into large pieces and put in a large bowl. Pull chicken into bite-size strips and scatter half over the leaves, along with half the croutons. \n8. Add most of the dressing and toss with your fingers. Scatter the rest of the chicken and croutons, then drizzle with the remaining dressing. Sprinkle the parmesan on top and serve straight away.', 1, '2023-05-21'),
('Double Chocolate Cookies', 'This recipe makes the perfect small batch double chocolate cookies.', '1. Preheat the oven to 350°F and line a baking sheet with parchment paper. Set aside. \n2. Place the softened butter and brown sugar in a medium bowl. Use a fork or a whisk to mix together until smooth and homogenous. \n3. Scrape down the sides of the bowl and add the egg yolky and vanilla extract. Mix until combined. \n4. Scrape down the bowl and add the cocoa powder, flour, baking soda and salt. Mix until just combined and no streaks of flour remain. \n5. Fold in chocolate chips until evenly distributed. \n6. Divide the cookie dough into 2 big cookies and place onto your prepared cookie sheet 2 inches apart. \n7. Bake at 350°F for 11 minutes or until the edges are set but the centers are still gooey. Allow to cool for 10 minutes on the baking sheet until set. Enjoy!', 1, '2023-05-21'),
('Chipotle Grilled Tofu', 'The tofu does not stick and it turns out perfectly chewy and crispy!', '1. Drain the tofu. Slice the tofu crosswise into ⅓-inch slabs, about 8 to 11 slices per block. Arrange on a paper towel-lined cutting board and add a few paper towels on top. Weight with a heavy cookbook or cast-iron skillet. Press for 15 minutes to remove excess water. \n2. Make the marinade: To a blender or food processor, add the cilantro, chipotle peppers and adobo sauce, garlic, orange zest, orange juice, lime juice, olive oil, sugar, cumin, salt, and black pepper to taste. Blend until relatively smooth. Season to taste with more salt and pepper needed. Reserve 4 tablespoons of the marinade. \n3. Transfer tofu slabs to a shallow dish or pan and pour the rest of the marinade on top, using a pastry brush to evenly coat the tofu. Allow to marinate for at least 15 minutes at room temperature, or up to 1 hour in the fridge. \n4. Prepare the grill. Preheat a gas grill (between medium and medium-high) with the lid on for at least 10 minutes to get the grates really hot. Scrub down with a heavy-duty grill brush to remove any gunk or drippings. \n5. Set a small bowl with a few tablespoons of oil near the grill and oil up a wad of paper towels in the oil. Use a set of tongs to rub the preheated grill grates with the oil-soaked towels. Let the oil smoke for 40 seconds or so, then rub with oil again. Repeat three to four times total to build up a good layer of seasoning. \n6. Immediately before or as soon as you add the tofu, turn the burners directly under the tofu to low heat and close the lid. Grill the tofu for 8 to 10 minutes, until nice grill marks form. Using a pair of tongs and/or a thin metal spatula, carefully flip each tofu slab and grill for another 7 to 10 minutes. \n7. Once the tofu is grilled, brush it with the reserved marinade.', 1, '2023-05-21'),
('Soy Sauce Fried Rice', 'Make perfect soy sauce fried rice in just 5 minutes!', '1. Gently loosen the rice grains with a fork. Set aside. \n2. In a small bowl, mix light soy sauce, dark soy sauce, sesame oil, sugar, and white pepper. Set aside. \n3. Heat a wok/skillet over high heat until hot, then pour in oil. Stir in minced garlic and fry until it turns a little golden. \n4. Add the cooked rice. Mix with the garlic then use a spatula to spread it into a thin layer. Leave to fry undisturbed for around 20 seconds. Then flip and toss to heat further (loosen any lumps with the spatula). \n5. Once the rice becomes piping hot, pour the sauce mixture over and toss well to coat the grains evenly. \n6. Stir in chopped scallions before removing the wok/skillet from the heat. Dish out and serve immediately.', 1, '2023-05-21'),
('Coconut Mango Passion Fruit Trifle', 'It is a light dessert that is perfect to enjoy in the spring and summer months.', '1. Place a sieve over a saucepan and strain out the passion fruit seeds. Set the seeds aside if desired, to use for garnish. \n2. With the saucepan over medium-high heat, add the mango and sugar. Cook at a gentle simmer for 4 minutes, until slightly thickened. Set aside to cool. \n3. In a large bowl, use an electric hand mixer to whip the coconut cream for a few minutes. Then add the sugar and vanilla and whip again. \n4. Add the cookies and butter to a food processor and blend into a crumb. Then add 2 Tbsp of the cookie crumbs to each serving glass and press down to form a crust on the bottom. \n5. In the now-empty food processor, blend the mango mixture until puréed. \n6. Spoon a heaping 0.25 cup of the coconut whipping cream on top of the cookie layer, followed by a tablespoon of the passion fruit mixture. Garnish as desired, and enjoy!', 1, '2023-05-21'),
('Vanilla Crème Brûlée', 'Five simple ingredients: cream, vanilla, salt, eggs and sugar.', '1. Heat oven to 325 degrees. In a saucepan, combine cream, vanilla bean and salt and cook over low heat just until hot. Let sit for a few minutes, then discard vanilla bean. \n2. In a bowl, beat yolks and sugar together until light. Stir about a quarter of the cream into this mixture, then pour sugar-egg mixture into cream and stir. Pour into four 6-ounce ramekins and place ramekins in a baking dish; fill dish with boiling water halfway up the sides of the dishes. Bake for 30 to 40 minutes, or until centers are barely set. Cool completely. Refrigerate for several hours and up to a couple of days. \n3. When ready to serve, top each custard with about a teaspoon of sugar in a thin layer. Place ramekins in a broiler 2 to 3 inches from heat source. Turn on broiler. Cook until sugar melts and browns or even blackens a bit, about 5 minutes. Serve within two hours.', 1, '2023-05-21'),
('Crêpes', 'This simple but delicious crêpe recipe can be made in minutes from ingredients that everyone has on hand.', '1. Whisk flour and eggs together in a large mixing bowl; gradually add in milk and water, stirring to combine. Add salt and melted butter; beat until smooth. \n2. Heat a lightly oiled griddle or frying pan over medium-high heat. Pour or scoop the batter onto the griddle, using approximately 1/4 cup for each crêpe. Tilt the pan with a circular motion so that the batter coats the surface evenly. \n3. Cook until the top of the crêpe is no longer wet and the bottom has turned light brown, 1 to 2 minutes. Run a spatula around the edge of the skillet to loosen the crêpe; flip and cook until the other side has turned light brown, about 1 minute more. Serve hot.', 1, '2023-05-21'),
('Braised Tofu Sichuan Style', 'Braised tofu tastes amazing when made in Sichuan style.', '1. Slice the tofu into triangle pieces, about 2 cm thick. Pat dry their surface with kitchen paper. \n2. In a flat-bottomed wok or a frying pan (preferably a deep one as it will be used for the braising step later), heat up oil enough to cover about half the thickness of the tofu. Test with the tip of a chopstick. If bubbles appear around it, the oil is hot enough. Gently slide in the tofu pieces one by one. Turn down the heat to medium and leave to fry. Flip over once the first side turns golden. When both sides are done, transfer to a plate lined with kitchen paper to absorb excess oil. \n3. Pour out most of the oil leaving just a little in the wok/pan. Fry garlic and Sichuan chili bean paste over medium heat until fragrant. \n4. Add shiitake mushroom, the mushroom water, dark soy sauce and sugar. Bring it to a boil. \n5. Put in the fried tofu and chili pepper. Stir around then cover with a lid. Leave to braise over low heat for 2 mins. Add a little more water if needed. \n6. Pour in the starch water. Give everything a quick stir then garnish with scallions. Dish out and serve immediately.', 1, '2023-05-21'),
('Chinese Chicken Noodle Soup', 'An umami-rich, all-in-one dish made with simple ingredients.', '1. Put dried shiitake mushrooms in a large bowl. Pour in 6 cups (1400ml) of water. Leave to soak overnight (or at least 4 hours). \n2. Put the chicken legs (or thighs) into a pot. Add the rehydrated mushrooms, along with the soaking water (leave out any residue at the bottom of the bowl). Bring to a full boil. Skim off any foam floating on the surface. \n3. Add Shaoxing rice wine, ginger, and the white part of the scallions. Turn the heat to the lowest. Cover the pot with a tight lid. Leave to gently simmer for 30 minutes or so until the meat becomes very tender. \n4. Transfer the chicken legs to a plate to cool. Discard the ginger and scallions. Add light soy sauce, salt, white pepper, and sesame oil to the broth. Taste to adjust if necessary. \n5. Once the chicken is cool to touch, remove the skin and tear the meat into bite-sized shreds. \n6. In a separate pot, bring plenty of water to a full boil. Cook the noodles over medium-high heat until fully cooked. At the very end of this process, add bok choy for a quick blanch (20 seconds). \n7. Drain the noodles and bok choy. Place them into serving bowls. Add the broth and mushrooms. Top with the shredded chicken and the green part of the scallions (finely chopped). Optionally, drizzle some chili oil over.', 1, '2023-05-21'),
('Falafel', 'Authentic falafel recipe, crisp outside and tender in the middle; combining chickpeas, fresh herbs, and spices.', '1. Transfer the dried chickpeas to a large bowl and cover them with cold water. Leave them to soak for at least 8 hours or overnight (up to 24 hours). Then drain and rinse them well. \n2. In a food processor, roughly chop the onion and garlic. \n3. Add the chickpeas and pulse into a crumb consistency before adding the cilantro and parsley and pulse a few more times into a homogenous mix. Be careful not to over-process it into a paste. There should be some texture. \n4. Transfer the falafel dough back to the large bowl and combine with the falafel spice and salt, mixing with a spoon. \n5. When you are ready to cook the falafel, add the baking powder and mix. Cover and chill the bowl in the refrigerator for 30 minutes (an hour if you have the time) before frying. \n6. To form the falafel, you can use a specific falafel scoop or a spoon (and hand-shape them into balls or slightly flattened disks). Be gentle, so they stay fluffy. \n7. In a large, heavy-based skillet, add 1.5-2 inches of oil (you need enough to cover the falafels) and heat over high heat until it gently bubbles). \n8. Carefully lower 5-6 balls/patties into the oil (cook in batches to avoid overcrowding the pan). Cook them for 3-4 minutes, flipping and continuing to cook until golden brown all over. \n9. Use a slotted spoon to remove them from the oil onto a layer of paper towels to drain.', 1, '2023-05-21');
INSERT INTO ingredients (name, recipe_id, quantity, measurementUnit)
VALUES
('Penne Pasta', 1, 12, 'ounce(s)'),
('Boneless Chicken Breast', 1, 3, 'piece(s)'),
('Heavy Cream', 1, 2, 'cup(s)'),
('Parmesan Cheese', 1, 1, 'cup(s)'),
('Ground Beef', 2, 1, 'pound(s)'),
('Taco Seasoning', 2, 1, 'package(s)'),
('Tortillas', 2, 8, 'piece(s)'),
('Spaghetti', 3, 8, 'ounce(s)'),
('Ground Beef', 3, 1, 'pound(s)'),
('Onion', 3, 1, 'medium'),
('Garlic', 3, 2, 'clove(s)'),
('Canned Tomatoes', 3, 28, 'ounce(s)'),
('Tomato Paste', 3, 6, 'ounce(s)'),
('Fresh Mozzarella', 4, 8, 'ounce(s)'),
('Tomatoes', 4, 2, 'large'),
('Basil', 4, 1, 'cup(s)'),
('Olive Oil', 4, 2, 'tablespoon(s)'),
('Balsamic Vinegar', 4, 2, 'tablespoon(s)'),
('Bread', 5, 8, 'slice(s)'),
('Cheddar Cheese', 5, 8, 'slice(s)'),
('Penne Pasta', 6, 8, 'ounce(s)'),
('Ground Beef', 6, 1, 'pound(s)'),
('Onion', 6, 1, 'medium'),
('Garlic', 6, 3, 'clove(s)'),
('Canned Tomatoes', 6, 14.5, 'ounce(s)'),
('Tomato Paste', 6, 2, 'tablespoon(s)'),
('Taco Seasoning', 6, 2, 'tablespoon(s)'),
('Boneless Chicken Breast', 7, 4, 'piece(s)'),
('Breadcrumbs', 7, 1, 'cup(s)'),
('Parmesan Cheese', 7, 1/2, 'cup(s)'),
('Italian Seasoning', 7, 1, 'teaspoon(s)'),
('Egg', 7, 2, 'large'),
('Olive Oil', 7, 2, 'tablespoon(s)'),
('Tomato Sauce', 7, 1, 'cup(s)'),
('Mozzarella Cheese', 7, 1, 'cup(s)'),
('Beef Sirloin Steak', 8, 1, 'pound(s)'),
('Soy Sauce', 8, 2, 'tablespoon(s)'),
('Cornstarch', 8, 1, 'tablespoon(s)'),
('Sugar', 8, 1, 'teaspoon(s)'),
('Vegetable Oil', 8, 2, 'tablespoon(s)'),
('Garlic', 8, 2, 'clove(s)'),
('Ginger', 8, 1, 'inch(es)'),
('Broccoli Florets', 8, 4, 'cup(s)'),
('Oyster Sauce', 8, 2, 'tablespoon(s)'),
('Boneless Chicken Breast', 9, 4, 'piece(s)'),
('Mozzarella Cheese', 9, 4, 'slice(s)'),
('Tomatoes', 9, 2, 'medium'),
('Fresh Basil', 9, 8, 'leave(s)'),
('Salt', 9, 1/2, 'teaspoon(s)'),
('Black Pepper', 9, 1/4, 'teaspoon(s)'),
('Olive Oil', 9, 2, 'tablespoon(s)'),
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
('Vegan butter', 14, 2, 'teaspoon(s)'),
('Heavy cream', 15, 1, 'cup(s)'),
('Vanilla extract', 15, 0.5, 'teaspoon(s)'),
('Salt', 15, 0.125, 'teaspoon(s)'),
('Egg', 15, 2.5, 'yolk(s)'),
('Sugar', 15, 0.25, 'cup(s)'),
('All purpose flour', 16, 0.5, 'cup(s)'),
('Egg', 16, 1, 'whole'),
('Milk', 16, 0.25, 'cup(s)'),
('Water', 16, 0.25, 'cup(s)'),
('Salt', 16, 0.125, 'teaspoon(s)'),
('Butter', 16, 1, 'tablespoon(s)'),
('Tofu', 17, 7, 'ounce(s)'),
('Garlic', 17, 3, 'clove(s)'),
('Sichuan chili bean paste', 17, 1.5, 'tablespoon(s)'),
('Dried shiitake mushroom', 17, 4, 'piece(s)'),
('Water', 17, 0.5, 'cup'),
('Dark soy sauce', 17, 0.5, 'teaspoon(s)'),
('Sugar', 17, 0.5, 'teaspoon(s)'),
('Fresh chili pepper', 17, 4, 'piece(s)'),
('Tapioca starch', 17, 1, 'teaspoon(s)'),
('Scallion', 17, 1, 'stalk(s)'),
('Dried shiitake mushroom', 18, 4, 'piece(s)'),
('Water', 18, 3, 'cup(s)'),
('Chicken leg', 18, 1, 'piece(s)'),
('Shaoxing rice wine', 18, 1, 'tablespoon(s)'),
('Ginger', 18, 2, 'slice(s)'),
('Scallion', 18, 1, 'stalk(s)'),
('Light soy sauce', 18, 1, 'teaspoon(s)'),
('Salt', 18, 0.25, 'teaspoon(s)'),
('Ground white pepper', 18, 0.06, 'teaspoon(s)'),
('Sesame oil', 18, 0.13, 'teaspoon(s)'),
('Noodles', 18, 2, 'portion(s)'),
('Bok choy', 18, 1, 'head(s)'),
('Chickpeas', 19, 2, 'cup(s)'),
('Onion', 19, 0.5, 'medium'),
('Garlic', 19, 1.5, 'clove(s)'),
('Cilantro', 19, 0.75, 'cup(s)'),
('Parsley', 19, 0.75, 'cup(s)'),
('Falafel spice', 19, 0.5, 'tablespoon(s)'),
('Salt', 19, 0.25, 'teaspoon(s)'),
('Baking powder', 19, 0.25, 'teaspoon(s)'),
('Sunflower oil', 19, 2, 'cup(s)');
INSERT INTO tags (name) VALUES
('vegan'),
('vegetarian'),
('lactose free'),
('gluten free'),
('starter'),
('main course'),
('dessert and sweets');
INSERT INTO recipe_tags (recipe_id, tag_id) VALUES
(1, 6),
(2, 6),
(2, 4),
(3, 6),
(4, 5),
(4, 2),
(4, 4),
(5, 6),
(5, 2),
(6, 3),
(6, 4),
(6, 6),
(7, 4),
(7, 6),
(8, 5),
(9, 1),
(9, 2),
(9, 4),
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
(14, 7),
(15, 4),
(15, 7),
(16, 7),
(17, 1),
(17, 2),
(17, 3),
(17, 4),
(17, 6),
(18, 3),
(18, 6),
(19, 1),
(19, 2),
(19, 3),
(19, 4),
(19, 5);
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