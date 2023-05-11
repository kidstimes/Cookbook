package cookbook.model;

/**
 * The Ingredient class.
 */
public class Ingredient {

  private String name;
  private float quantity;
  private String measurementUnit;

  /**
   * Ingredient Constructor.
   *
   * @param name the name of the ingredients
   * @param quantity the quantity of the ingredient in the recipe
   * @param measurementUnit the measurement unit of the ingredient
   */
  public Ingredient(String name, float quantity, String measurementUnit) {
    this.name = name;
    this.quantity = quantity;
    this.measurementUnit = measurementUnit;
  }

  // start the getters and setters
  // setters mainly for the write recipe method later

  public String getName() {
    return name;
  }

  public float getQuantity() {
    return quantity;
  }

  public float getQuantity(int serving) {
    return quantity * serving;
  }

  public String getMeasurementUnit() {
    return measurementUnit;
  }

}
