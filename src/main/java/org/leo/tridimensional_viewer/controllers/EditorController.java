package org.leo.tridimensional_viewer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import org.leo.tridimensional_viewer.managers.TabManager;

import java.util.HashMap;
import java.util.Map;

public class EditorController {
  @FXML
  private TabPane tabPane;

  @FXML
  private void initialize() {
    System.out.println("EditorController initialized!");

    Tab newTab = new Tab("nomeoggettoboh");
    ImageView image = new ImageView();
    newTab.setContent(image);

    tabPane.getTabs().add(newTab);

    TabManager.addTab("tab test");
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  /**
   * Add a tab in a raw way. I recommend using TabManager.addTab
   *
   * @param name name of the tab
   * @param id   a unique id for it.
   * @return the created Tab
   * @throws IllegalArgumentException if the id is already in use
   */
  public Tab addTabRaw(String name, String id) {
    for (Tab t : tabPane.getTabs()) {
      if (id.equals(t.getId())) {
        throw new IllegalArgumentException("A tab with id '" + id + "' already exists.");
      }
    }

    Tab tab = new Tab(name);
    tab.setId(id);
    tab.setClosable(true);

    tabPane.getTabs().add(tab);
    System.out.println("Created tab with ID: " + id);

    return tab;
  }

  /**
   * Retrieve a tab by id
   *
   * @param id id of the tab
   * @return Tab object
   */
  public Tab getTabById(String id) {
    for (Tab t : tabPane.getTabs()) {
      if (id.equals(t.getId())) {
        return t;
      }
    }
    return null;
  }
}