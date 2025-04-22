package org.leo.tridimensional_viewer.renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {
  static Main app;

  public static void main(String[] args) {

    app = new Main();

    AppSettings settings = new AppSettings(true);
    //settings.setTitle("My Awesome Game");
    settings.setAudioRenderer(null);

    app.setSettings(settings);

    app.start();
  }

  public static Main getApp() {
    return app;
  }

  @Override
  public void simpleInitApp() {
    int width = 800;  // Width of the offscreen texture
    int height = 600; // Height of the offscreen texture

    Box b = new Box(1, 1, 1);
    Geometry geom = new Geometry("Box", b);

    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    geom.setMaterial(mat);

    rootNode.attachChild(geom);

  }

  @Override
  public void simpleUpdate(float tpf) {
    //TODO: add update code
  }

}