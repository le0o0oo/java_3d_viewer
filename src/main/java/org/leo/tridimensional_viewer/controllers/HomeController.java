package org.leo.tridimensional_viewer.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.leo.tridimensional_viewer.Main;
import org.leo.tridimensional_viewer.managers.FilesManager;

import java.io.File;

public class HomeController {
  @FXML
  private Button openBtn;

  @FXML
  private void openFolder() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Folder");

    // Get the current stage from the button (or any other Node)
    Stage stage = (Stage) openBtn.getScene().getWindow();

    File selectedDirectory = directoryChooser.showDialog(stage);

    if (selectedDirectory != null) {
      System.out.println("Selected folder: " + selectedDirectory.getAbsolutePath());
      FilesManager.setWorkingDir(selectedDirectory.getAbsolutePath());
      Main.setScene("editor.fxml");
    }
  }

//  @FXML
//  private void initialize() {
//    // Create a PauseTransition with a specified duration (e.g., 3 seconds)
//    PauseTransition pause = new PauseTransition(Duration.millis(500));
//
//    // Set an action to be performed after the pause
//    pause.setOnFinished(event -> {
//      final String path = "yourpath";
//      FilesManager.setWorkingDir(path);
//      Main.setScene("editor.fxml");
//    });
//
//    // Start the pause
//    pause.play();
//  }
}
