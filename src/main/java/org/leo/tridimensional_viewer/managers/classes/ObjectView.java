package org.leo.tridimensional_viewer.managers.classes;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.leo.tridimensional_viewer.managers.RendererManager;
import org.leo.tridimensional_viewer.renderer.OffscreenRendererApp;

public class ObjectView implements TabInstance {
  private static final int RENDER_W = 800;
  private static final int RENDER_H = 600;

  private final String name;
  private final String id;
  private final Tab element;
  private final ImageView imageView;
  private final StackPane container;
  private OffscreenRendererApp app;

  public ObjectView(String name, String id, Tab tabElement) {
    this.name = name;
    this.id = id;
    this.element = tabElement;

    imageView = new ImageView();
    imageView.setPreserveRatio(false);
    imageView.setImage(new Image("Interface/Logo/Monkey.jpg"));

    container = new StackPane(imageView);
    imageView.fitWidthProperty().bind(container.widthProperty());
    imageView.fitHeightProperty().bind(container.heightProperty());

    container.setMinSize(0, 0);
    container.setPrefSize(RENDER_W, RENDER_H);
    container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    element.setContent(container);

    RendererManager.setImageView(imageView);

    imageView.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
      System.out.println(RendererManager.scaleFactor);
      int newWidth = (int) (newBounds.getWidth() * RendererManager.scaleFactor);
      int newHeight = (int) (newBounds.getHeight() * RendererManager.scaleFactor);
      if (newWidth > 2 && newHeight > 2) {
        Platform.runLater(() -> RendererManager.getApp().requestResize(newWidth, newHeight));
      }
    });
  }

  public void destroy() {
    if (app != null) {
      app.stop(); // clean shutdown
    }
  }

  public void serialize() {
  }

  public void deserialize() {
  }
}
