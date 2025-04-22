package org.leo.tridimensional_viewer.renderer;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import javafx.application.Platform;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.nio.ByteBuffer;

public class OffscreenImageUpdater {

  private final OffscreenRenderingApp jmeApp;
  private final int width;
  private final int height;
  private final WritableImage writableImage;
  private final PixelWriter pixelWriter;

  public OffscreenImageUpdater(OffscreenRenderingApp app) {
    this.jmeApp = app;
    this.width = app.getAppSettings().getWidth();
    this.height = app.getAppSettings().getHeight();
    this.writableImage = new WritableImage(width, height);
    this.pixelWriter = writableImage.getPixelWriter();
  }

  public void updateImage() {
    if (jmeApp.getContext() == null || jmeApp.getRenderManager() == null) return;

    FrameBuffer fb = jmeApp.getOffscreenFramebuffer();
    Texture2D tex = jmeApp.getOffscreenTexture();
    if (fb == null || tex == null) return;

    jmeApp.enqueue(() -> {
      jmeApp.update();

      RenderManager rm = jmeApp.getRenderManager();
      ViewPort vp = jmeApp.getViewPort();
      Renderer renderer = jmeApp.getRenderer();

      // Bind framebuffer and render to it
      renderer.setFrameBuffer(fb);
      renderer.clearBuffers(true, true, true);
      rm.renderViewPort(vp, 0);
      renderer.setFrameBuffer(null);

      // Read pixels from framebuffer
      ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
      renderer.readFrameBuffer(fb, buffer);
      buffer.rewind();

      int[] pixels = new int[width * height];
      for (int i = 0; i < width * height; i++) {
        int r = buffer.get() & 0xFF;
        int g = buffer.get() & 0xFF;
        int b = buffer.get() & 0xFF;
        int a = buffer.get() & 0xFF;
        pixels[i] = (a << 24) | (r << 16) | (g << 8) | b;
      }

      // Push image to JavaFX thread
      Platform.runLater(() ->
          pixelWriter.setPixels(0, 0, width, height,
              PixelFormat.getIntArgbInstance(), pixels, 0, width)
      );

      return null;
    });
  }

  public WritableImage getWritableImage() {
    return writableImage;
  }
}
