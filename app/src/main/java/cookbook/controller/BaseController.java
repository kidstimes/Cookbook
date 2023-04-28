package cookbook.controller;

import javafx.scene.Node;

/**
 * The interface for the controllers that handle various functionalities.
 */
public interface BaseController {

  /**
   * Get the view to display.
   *
   * @return the central Node of the view
   */
  Node getView();
}
