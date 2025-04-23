package org.leo.tridimensional_viewer.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.leo.tridimensional_viewer.managers.StatusManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

public class OffscreenRendererApp extends SimpleApplication {
  private boolean initialized = false;

  private FrameBuffer offBuffer;
  private Texture2D offTex;
  private ViewPort offView;
  private ImageView imageElement;
  private Box box;
  private Geometry geom;
  private float angle = 0;
  Spatial model;

  private ByteBuffer buffer;
  private BufferedImage img;

  private int currentWidth = -1;
  private int currentHeight = -1;
  private volatile int pendingResizeWidth = -1;
  private volatile int pendingResizeHeight = -1;

  public void setImageElement(ImageView imageElement) {
    this.imageElement = imageElement;
  }

  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void simpleInitApp() {
//    box = new Box(1, 1, 1);
//    geom = new Geometry("Box", box);
//    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//    mat.setColor("Color", ColorRGBA.Blue);
//    geom.setMaterial(mat);
//    geom.setLocalTranslation(0, 0, 0);


    model = assetManager.loadModel("Models/DamagedHelmet.glb");
    model.setLocalScale(1.0f);
    model.setLocalTranslation(0, 0, 0);

    rootNode.attachChild(model);

    DirectionalLight light = new DirectionalLight();
    light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
    rootNode.addLight(light);
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White.mult(0.3f));
    rootNode.addLight(ambient);

    cam.setLocation(new Vector3f(0, 2, 6));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

    //rootNode.attachChild(geom);

    int width = settings.getWidth();
    int height = settings.getHeight();

    doResize(width, height);

    StatusManager.hide();
    initialized = true;
  }

  @Override
  public void simpleUpdate(float tpf) {
    // Defer resize
    if (pendingResizeWidth > 2 && pendingResizeHeight > 2) {
      if (pendingResizeWidth != currentWidth || pendingResizeHeight != currentHeight) {
        doResize(pendingResizeWidth, pendingResizeHeight);
      }
      pendingResizeWidth = -1;
      pendingResizeHeight = -1;
    }

    if (offBuffer == null || buffer == null || img == null || offView == null) return;

    angle += tpf;
    angle %= FastMath.TWO_PI;
    Quaternion q = new Quaternion().fromAngles(angle, 0, angle);
    model.setLocalRotation(q);


    rootNode.updateLogicalState(tpf);
    rootNode.updateGeometricState();
    renderManager.renderViewPort(offView, tpf);

    renderer.readFrameBuffer(offBuffer, buffer);
    for (int y = 0; y < currentHeight; y++) {
      for (int x = 0; x < currentWidth; x++) {
        int i = (x + (currentHeight - y - 1) * currentWidth) * 4;
        int r = buffer.get(i) & 0xFF;
        int g = buffer.get(i + 1) & 0xFF;
        int b = buffer.get(i + 2) & 0xFF;
        int a = buffer.get(i + 3) & 0xFF;
        int argb = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(x, y, argb);
      }
    }

    if (imageElement != null) {
      imageElement.setImage(convertToFxImage(img));
    }
  }

  public void requestResize(int width, int height) {
    if (width > 2 && height > 2) {
      this.pendingResizeWidth = width;
      this.pendingResizeHeight = height;
    }
  }

  private void doResize(int width, int height) {
    if (width <= 2 || height <= 2) {
      System.out.println("Resize skipped: invalid dimensions (" + width + "x" + height + ")");
      return;
    }

    System.out.println("Resizing to: " + width + "x" + height);
    currentWidth = width;
    currentHeight = height;

    if (offView != null) {
      renderManager.removeMainView(offView);
    }

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
    //offView.setBackgroundColor(ColorRGBA.LightGray);
    offView.setOutputFrameBuffer(offBuffer);
    offView.attachScene(rootNode);

    buffer = BufferUtils.createByteBuffer(width * height * 4);
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  private static javafx.scene.image.Image convertToFxImage(BufferedImage image) {
    if (image == null) return null;

    WritableImage wr = new WritableImage(image.getWidth(), image.getHeight());
    PixelWriter pw = wr.getPixelWriter();
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        pw.setArgb(x, y, image.getRGB(x, y));
      }
    }
    return wr;
  }

  public void setModel(String absolutePath) {
    File file = new File(absolutePath);
    if (!file.exists()) {
      System.err.println("Model file does not exist: " + absolutePath);
      return;
    }

    // Detach old model if any
    if (model != null) {
      rootNode.detachChild(model);
    }

    // Get parent directory and filename
    String parentDir = file.getParent();   // <-- this is from java.io.File
    String filename = file.getName();

    // Unregister previous locator if necessary
    assetManager.unregisterLocator(parentDir, FileLocator.class);
    assetManager.registerLocator(parentDir, FileLocator.class);

    // Load model using just the filename (relative to the locator)
    model = assetManager.loadModel(filename);
    model.setLocalScale(1.0f);
    model.setLocalTranslation(0, 0, 0);

    rootNode.attachChild(model);
  }
}
