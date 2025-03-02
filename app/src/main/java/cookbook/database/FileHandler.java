package cookbook.database;

import java.io.*;  
import java.util.Scanner;
import java.util.ArrayList;

public class FileHandler {

    public ArrayList<String[]> getRecipes() {
        ArrayList<String[]> recipes = new ArrayList<String[]>();

        try {
            Scanner scanner = new Scanner(new File("app/src/main/java/cookbook/database/recipes.csv"));
            scanner.useDelimiter(";");

            while (scanner.hasNext()) {
                recipes.add(scanner.next().split(";"));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public ArrayList<String[]> getIngredients() {
        ArrayList<String[]> ingredients = new ArrayList<String[]>();

        try {
            Scanner scanner = new Scanner(new File("app/src/main/java/cookbook/database/ingredients.csv"));
            scanner.useDelimiter(";");

            while (scanner.hasNext()) {
                ingredients.add(scanner.next().split(";"));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<String[]> getTags() {
        ArrayList<String[]> tags = new ArrayList<String[]>();

        try {
            Scanner scanner = new Scanner(new File("app/src/main/java/cookbook/database/tags.csv"));
            scanner.useDelimiter(";");

            while (scanner.hasNext()) {
                tags.add(scanner.next().split(";"));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tags;
    }

}
