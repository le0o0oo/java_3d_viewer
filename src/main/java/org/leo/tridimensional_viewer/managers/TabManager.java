package org.leo.tridimensional_viewer.managers;

import javafx.scene.control.Tab;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.UUID;

import org.leo.tridimensional_viewer.controllers.EditorController;
import org.leo.tridimensional_viewer.managers.classes.ObjectView;
import org.leo.tridimensional_viewer.managers.classes.TabInstance;
import org.leo.tridimensional_viewer.Main;

import java.util.ArrayList;

public class TabManager {
  static HashMap<String, TabInstance> tabs = new HashMap<String, TabInstance>();

  /**
   * Use only this function to add tabs
   *
   * @param name name of the tab
   */
  public static void addTab(String name) {
    EditorController controller = Main.getLoader().getController();
    String uniqueId = UUID.randomUUID().toString();

    Tab newTab = controller.addTabRaw(name, uniqueId);

    ObjectView view = new ObjectView(name, uniqueId, newTab);

    tabs.put(uniqueId, view);

    System.out.println("TabManager added tab: " + name + " with ID: " + uniqueId);
  }
}
