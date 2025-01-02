package com.canerfk.mano.application.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DialogUtils {
    private static final String RESOURCE_PATH = "/com/canerfk/mano/application/info/";

    private static String loadContent(String fileName) {
        try (InputStream is = DialogUtils.class.getResourceAsStream(RESOURCE_PATH + fileName)) {
            if (is == null) {
                System.err.println("Resource not found: " + fileName);
                return "Error: Content could not be loaded.";
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error loading content from " + fileName + ": " + e.getMessage());
            e.printStackTrace();
            return "Error loading content: " + e.getMessage();
        }
    }

    public static void showInstructionSet() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mano Basic Computer - Instruction Set");
        alert.setHeaderText("Basic Computer Instructions");

        String content = loadContent("instructions.txt");
        TextArea textArea = createTextArea(content, 20, 50);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public static void showDocumentation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mano Assembly Documentation");
        alert.setHeaderText("How to Use Mano Assembler");

        String content = loadContent("documentation.txt");
        TextArea textArea = createTextArea(content, 30, 60);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    public static void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Mano Assembler");
        alert.setHeaderText("Mano Assembler v1.0");

        String content = loadContent("about.txt");
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static TextArea createTextArea(String content, int rows, int columns) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(rows);
        textArea.setPrefColumnCount(columns);
        return textArea;
    }
}