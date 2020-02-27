package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) throws Exception {
    StackPane root = new StackPane();

    WebView webView = new WebView();
    File html = new File("html/index.html");
    webView.getEngine().load(html.toURI().toString());

    root.getChildren().add(webView);

    // TODO: Fix aspect ratio 4k screens
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();

    Scene scene = new Scene(root, width, height);

    primaryStage.setTitle("Sportiza");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
