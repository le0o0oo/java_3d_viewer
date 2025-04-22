package org.leo.tridimensional_viewer.managers.classes;

public interface TabInstance {
  /**
   * Invoked when another tab gets clicked. Should save an eventual current state to be ready to load it in the future
   */
  public void serialize();

  /**
   * Invoked when the user clicks on the tab. Should load the saved state
   */
  public void deserialize();

  /**
   * Invoked when the tab gets closed.
   */
  public void destroy();
}
