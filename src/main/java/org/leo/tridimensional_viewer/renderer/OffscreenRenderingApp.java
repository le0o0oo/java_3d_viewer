// OffscreenRenderingApp.java
package org.leo.tridimensional_viewer.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class OffscreenRenderingApp extends SimpleApplication {

  private FrameBuffer offscreenFb;
  private Texture2D offscreenTex;

  public OffscreenRenderingApp() {
    AppSettings settings = new AppSettings(true);
    settings.setWidth(800);
    settings.setHeight(600);
    settings.setAudioRenderer(null);
    settings.setFullscreen(false);
    settings.setResizable(false);
    settings.setFrameRate(60);
    setSettings(settings);
    setShowSettings(false);
  }

  @Override
  public void simpleInitApp() {
    int w = settings.getWidth(), h = settings.getHeight();
    offscreenFb = new FrameBuffer(w, h, 1);
    offscreenTex = new Texture2D(w, h, Format.RGBA8);
    offscreenFb.setColorTexture(offscreenTex);
    offscreenFb.setDepthBuffer(Format.Depth);
    viewPort.setOutputFrameBuffer(offscreenFb);

    // clear to red to debug
    viewPort.setBackgroundColor(ColorRGBA.Red);

    // add a blue cube
    Geometry cube = new Geometry("Cube", new Box(1, 1, 1));
    Material mat = new Material(assetManager,
        "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    cube.setMaterial(mat);
    rootNode.attachChild(cube);

    // position camera so cube is visible
    cam.setLocation(new Vector3f(0, 0, 5));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    flyCam.setEnabled(false);
  }

  public FrameBuffer getOffscreenFramebuffer() {
    return offscreenFb;
  }

  public Texture2D getOffscreenTexture() {
    return offscreenTex;
  }

  // We'll read the FB on demand; no extra helpers here
}
