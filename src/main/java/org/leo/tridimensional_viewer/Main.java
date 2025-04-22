package org.leo.tridimensional_viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
  private String currentView = "editor.fxml";
  private static FXMLLoader loader;

  @Override
  public void start(Stage stage) throws IOException {


    loader = new FXMLLoader(Main.class.getResource(currentView));
    Scene scene = new Scene(loader.load(), 1000, 600);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  public static FXMLLoader getLoader() {
    return loader;
  }

  public String getCurrentView() {
    return currentView;
  }
}