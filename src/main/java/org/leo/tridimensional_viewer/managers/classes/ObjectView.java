package org.leo.tridimensional_viewer.managers.classes;

import com.jme3.system.JmeContext;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import org.leo.tridimensional_viewer.renderer.OffscreenImageUpdater;
import org.leo.tridimensional_viewer.renderer.OffscreenRenderingApp;

public class ObjectView implements TabInstance {
  String name, id;
  Tab element;
  OffscreenRenderingApp jmeApp;
  OffscreenImageUpdater imageUpdater;
  ImageView imageView;

  public ObjectView(String name, String id, Tab tabElement) {
    this.name = name;
    this.id = id;
    this.element = tabElement;

    // 1. Start the jME3 app
    this.jmeApp = new OffscreenRenderingApp();
    jmeApp.setShowSettings(true);
    jmeApp.start(JmeContext.Type.OffscreenSurface); // ✅ KEY LINE!

    // 2. Delay initialization until jME is fully started
    new Thread(() -> {
      try {
        // Wait for jME3 to initialize
        while (jmeApp.getRenderManager() == null) {
          Thread.sleep(100);
        }

        imageUpdater = new OffscreenImageUpdater(jmeApp);
        imageView = new ImageView();
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        StackPane container = new StackPane(imageView);
        imageView.fitWidthProperty().bind(container.widthProperty());
        imageView.fitHeightProperty().bind(container.heightProperty());

        Platform.runLater(() -> element.setContent(container));

        // 3. Start the update loop
        Thread updateLoop = new Thread(() -> {
          while (true) {
            imageUpdater.updateImage();
            WritableImage img = imageUpdater.getWritableImage();

            Platform.runLater(() -> {
              if (img != null) {
                imageView.setImage(img);
              }
            });

            try {
              Thread.sleep(16); // ~60fps
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });

        updateLoop.setDaemon(true);
        updateLoop.start();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void serialize() {
    // TODO: Save state
  }

  public void deserialize() {
    // TODO: Restore state
  }

  public void destroy() {
    jmeApp.stop(); // Clean up jME when the tab is closed
  }
}
