            package com.canerfk.mano.application;

            import com.canerfk.mano.application.util.LanguageManager;
            import javafx.application.Application;
            import javafx.application.Platform;
            import javafx.fxml.FXMLLoader;
            import javafx.scene.Scene;
            import javafx.stage.Stage;
            import java.io.IOException;
            import java.util.Locale;
            import java.util.ResourceBundle;

            public class AssemblerApplication extends Application {
                @Override
                public void start(Stage stage) throws IOException {
                    LanguageManager.setLanguage("en");
                    ResourceBundle bundle = ResourceBundle.getBundle(
                            "com/canerfk/mano/application/languages/messages",
                            new Locale("en")
                    );

                    FXMLLoader fxmlLoader = new FXMLLoader(
                            AssemblerApplication.class.getResource("assembler-view.fxml"),
                            bundle
                    );

                    Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                    AssemblerController controller = fxmlLoader.getController();

                    stage.setTitle("Mano Assembler");
                    stage.setScene(scene);
                    stage.show();

                    Platform.runLater(controller::updateUIText);
                }

                public static void main(String[] args) {
                    launch();
                }
            }