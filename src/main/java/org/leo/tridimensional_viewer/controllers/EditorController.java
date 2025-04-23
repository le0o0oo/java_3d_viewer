package org.leo.tridimensional_viewer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.leo.tridimensional_viewer.Main;
import org.leo.tridimensional_viewer.managers.FilesManager;
import org.leo.tridimensional_viewer.managers.RendererManager;
import org.leo.tridimensional_viewer.managers.StatusManager;
import org.leo.tridimensional_viewer.managers.TabManager;
import org.leo.tridimensional_viewer.managers.classes.ObjectView;
import org.leo.tridimensional_viewer.managers.classes.TabInstance;
import org.leo.tridimensional_viewer.renderer.OffscreenRendererApp;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EditorController {
  @FXML
  private TabPane tabPane;
  @FXML
  private ListView files_list;
  @FXML
  private Slider resolution_slider;
  @FXML
  private Label resolution_label;

  @FXML
  public HBox status_pane;
  @FXML
  public Label status_lb;
  @FXML
  public ProgressBar status_progress;

  @FXML
  private void initialize() {
    status_pane.setVisible(false);
    StatusManager.init();
    if (!RendererManager.getApp().isInitialized()) StatusManager.indeterminate("Initializing engine");
    System.out.println("EditorController initialized!");

    //TabManager.addTab("tab test");
    files_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null) return;

      System.out.println("Selected item: " + newValue);
      if (TabManager.existsByName(newValue.toString())) {
        tabPane.getSelectionModel().select(getTabByName(newValue.toString()));
        return;
      }
      var tabs = TabManager.addTab(newValue.toString());
      TabInstance oldTab = tabs.getKey();
      TabInstance currentTab = tabs.getValue();
      if (oldTab != null) {
        oldTab.serialize();
      }
      currentTab.init();


      tabPane.getSelectionModel().select(getTabByName(newValue.toString()));
    });

    resolution_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      int value = (int) newValue.doubleValue();
      resolution_label.setText("Resolution: " + String.valueOf(value) + "%");
      RendererManager.scaleFactor = (double) value / 100;
      RendererManager.resize();
    });

    tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
      if (newTab != null) {
        System.out.println("Selected Tab: " + newTab.getText());
        TabInstance tabObject = TabManager.getTabById(newTab.getId());
        TabInstance oldTabObj = TabManager.getTabById(oldTab.getId());

        if (oldTabObj != null) oldTabObj.serialize();
        tabObject.deserialize();
      }
    });


    updateFilesList();
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  public static Tab getTabByName(String name) {
    EditorController controller = Main.getLoader().getController();
    Tab tabRes = null;

    for (var tab : controller.getTabPane().getTabs()) {
      if (name.equals(tab.getText())) tabRes = tab;
    }

    return tabRes;
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

  public void updateFilesList() {
    files_list.getItems().clear();
    FilesManager.files.forEach(file -> {
      files_list.getItems().add(file);
    });
  }
}