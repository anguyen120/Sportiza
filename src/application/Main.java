package application;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;

public class Main extends Application {
  private JavaConnector javaConnector = new JavaConnector(config.username, config.credential, config.databaseName);

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) {
    StackPane root = new StackPane();

    WebView webView = new WebView();
    File html = new File("html/index.html");
    webView.setContextMenuEnabled(false);
    WebEngine webEngine = webView.getEngine();
    webEngine.load(html.toURI().toString());

    webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener() {
              @Override
              public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != Worker.State.SUCCEEDED) {
                  return;
                }

                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", javaConnector);
              }
            }
    );

    root.getChildren().add(webView);

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());

    File logo = new File("html/images/logo.png");
    primaryStage.getIcons().add(new Image(logo.toURI().toString()));
    primaryStage.setTitle("Sportiza");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}