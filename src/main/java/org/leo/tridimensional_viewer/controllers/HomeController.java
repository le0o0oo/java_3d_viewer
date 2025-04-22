package org.leo.tridimensional_viewer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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
}
