package org.leo.tridimensional_viewer.managers;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.ImageView;
import org.leo.tridimensional_viewer.renderer.OffscreenRendererApp;

public class RendererManager {
  private static OffscreenRendererApp app;
  public static double scaleFactor = 0.5;
  private static ImageView imageView;

  public static void setImageView(ImageView imageView) {
    RendererManager.imageView = imageView;
  }

  public static void init() {
    final double scaleFactor = 0.5; // 50% resolution
    // Wait for layout to complete before starting JME app
    Platform.runLater(() -> {
//      double width = imageView.getBoundsInParent().getWidth();
//      double height = imageView.getBoundsInParent().getHeight();

      double width = 80;
      double height = 60;

      if (width < 2 || height < 2) return;

      app = new OffscreenRendererApp();
      app.setImageElement(imageView);
      AppSettings settings = new AppSettings(true);
      settings.setResolution((int) width, (int) height);
      app.setSettings(settings);
      app.setShowSettings(false);
      app.start(JmeContext.Type.OffscreenSurface);
    });
  }

  public static OffscreenRendererApp getApp() {
    return app;
  }


}
