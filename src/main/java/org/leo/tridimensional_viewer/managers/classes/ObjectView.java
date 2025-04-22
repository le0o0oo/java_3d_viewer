package org.leo.tridimensional_viewer.managers.classes;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.leo.tridimensional_viewer.renderer.OffscreenRendererApp;
//import org.leo.tridimensional_viewer.renderer.OffscreenRendererApp;

public class ObjectView implements TabInstance {
  private static final int RENDER_W = 800;
  private static final int RENDER_H = 600;

  private final String name;
  private final String id;
  private final Tab element;
  private final ImageView imageView;
  private final StackPane container;

  public ObjectView(String name, String id, Tab tabElement) {
    this.name = name;
    this.id = id;
    this.element = tabElement;

    imageView = new ImageView();
    imageView.setPreserveRatio(false);
    container = new StackPane(imageView);
    imageView.setImage(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLCL04mzrRXt7PmOaxXR8VNHpjzKnAEQ5gsA&s"));
    element.setContent(container);

    var app = new OffscreenRendererApp();
    app.setImageElement(imageView);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(800, 600);
    app.setSettings(settings);
    app.setShowSettings(false);
    app.start(JmeContext.Type.OffscreenSurface);
  }

  /**
   * Stop the renderer and timer when this view is destroyed.
   */
  public void destroy() {
    // There's no explicit stop() on SimpleApplication;
    // if you need to clean up, call:
    // rendererApp.stop();
  }

  public void serialize() { /* … */ }

  public void deserialize() { /* … */ }
}
