package org.leo.tridimensional_viewer.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class OffscreenRendererApp extends SimpleApplication {

  private FrameBuffer offBuffer;
  private Texture2D offTex;
  private ViewPort offView;
  private boolean hasRendered = false;
  private int frameCount = 0;
  ImageView imageElement;
  Box box;
  Geometry geom;
  private float angle = 0;

  public void setImageElement(ImageView imageElement) {
    this.imageElement = imageElement;
  }

  @Override
  public void simpleInitApp() {
    box = new Box(1, 1, 1);
    geom = new Geometry("Box", box);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    geom.setMaterial(mat);

    // Move the cube slightly forward so it's not at z=0
    geom.setLocalTranslation(0, 0, 0);
    rootNode.attachChild(geom);

    // Set up offscreen view
    int width = settings.getWidth();
    int height = settings.getHeight();

    offTex = new Texture2D(width, height, Image.Format.RGBA8);
    offBuffer = new FrameBuffer(width, height, 1);
    offBuffer.setDepthBuffer(Image.Format.Depth);
    offBuffer.setColorTexture(offTex);

    Camera offCam = new Camera(width, height);
    offCam.setFrustumPerspective(45f, (float) width / height, 1f, 1000f);
    offCam.setLocation(new Vector3f(4, 3, 10));
    offCam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

    offView = renderManager.createMainView("Offscreen View", offCam);
    offView.setClearFlags(true, true, true);
    offView.setBackgroundColor(ColorRGBA.Red); // red for contrast
    offView.setOutputFrameBuffer(offBuffer);
    offView.attachScene(rootNode);
  }

  @Override
  public void simpleUpdate(float tpf) {
    Quaternion q = new Quaternion();

    if (offView.isEnabled()) {
      angle += tpf;
      angle %= FastMath.TWO_PI;
      q.fromAngles(angle, 0, angle);

      geom.setLocalRotation(q);
      geom.updateLogicalState(tpf);
      geom.updateGeometricState();
    }

    if (imageElement == null) return;
    rootNode.updateLogicalState(tpf);
    rootNode.updateGeometricState();

    renderManager.renderViewPort(offView, tpf);

    int width = settings.getWidth();
    int height = settings.getHeight();

    ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
    renderer.readFrameBuffer(offBuffer, buffer);

    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int i = (x + (height - y - 1) * width) * 4;
        int r = buffer.get(i) & 0xFF;
        int g = buffer.get(i + 1) & 0xFF;
        int b = buffer.get(i + 2) & 0xFF;
        int a = buffer.get(i + 3) & 0xFF;
        int argb = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(x, y, argb);
      }
    }


    imageElement.setImage(convertToFxImage(img));
    //imageElement.setImage(new javafx.scene.image.Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLCL04mzrRXt7PmOaxXR8VNHpjzKnAEQ5gsA&s"));

    System.out.println("✅ Rendered img");
    hasRendered = true;
  }

  private static javafx.scene.image.Image convertToFxImage(BufferedImage image) {
    WritableImage wr = null;
    if (image != null) {
      wr = new WritableImage(image.getWidth(), image.getHeight());
      PixelWriter pw = wr.getPixelWriter();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          pw.setArgb(x, y, image.getRGB(x, y));
        }
      }
    }

    return new ImageView(wr).getImage();
  }
}
