package org.leo.tridimensional_viewer.managers;

import javafx.scene.control.ProgressIndicator;
import org.leo.tridimensional_viewer.Main;
import org.leo.tridimensional_viewer.controllers.EditorController;

public class StatusManager {
  static EditorController controller;

  public static void init() {
    controller = Main.getLoader().getController();
  }

  /**
   * Sets the progress bar to an indeterminare state
   *
   * @param text status text
   */
  public static void indeterminate(String text) {
    if (controller == null) return;

    controller.status_progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    controller.status_lb.setText(text);
    controller.status_pane.setVisible(true);
  }

  public static void setProgress(String text) {
    if (controller == null) return;

    controller.status_progress.setProgress(0);
    controller.status_lb.setText(text);
    controller.status_pane.setVisible(true);
  }

  public static void setProgress(int value) {
    if (controller == null) return;

    controller.status_progress.setProgress(value);
  }

  /**
   * Hides the status bar
   */
  public static void hide() {
    if (controller == null) return;
    
    controller.status_pane.setVisible(false);
  }


}
