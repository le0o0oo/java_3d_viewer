package org.leo.tridimensional_viewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.leo.tridimensional_viewer.controllers.EditorController;
import org.leo.tridimensional_viewer.managers.RendererManager;
import org.leo.tridimensional_viewer.managers.StatusManager;

import java.io.IOException;

public class Main extends Application {
  private static String currentView = "home.fxml";
  private static FXMLLoader loader;
  private static Stage primaryStage;

  @Override
  public void start(Stage stage) throws IOException {
    primaryStage = stage;
    loader = new FXMLLoader(Main.class.getResource(currentView));
    Scene scene = new Scene(loader.load(), 1000, 600);
    stage.setTitle("3D viewer");
    stage.setScene(scene);
    stage.show();

    RendererManager.init();

    // Add an on-close request event
    stage.setOnCloseRequest(event -> {
      RendererManager.getApp().stop();
    });
  }

  public static void setScene(String fxmlFile) {
    try {
      loader = new FXMLLoader(Main.class.getResource(fxmlFile));
      Scene newScene = new Scene(loader.load(), 1000, 600);
      primaryStage.setScene(newScene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
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