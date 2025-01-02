package com.canerfk.mano.application;

import com.canerfk.mano.application.util.DialogUtils;
import com.canerfk.mano.application.util.LanguageManager;
import com.canerfk.mano.assembler.Assembler;
import com.canerfk.mano.assembler.SymbolTable;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class AssemblerController {
    @FXML private TableView<MachineCodeEntry> machineCodeTable;
    @FXML private TableView<SymbolTable.SymbolTableEntry> symbolTableView;
    @FXML private TextArea errorArea;
    @FXML private Label label_status_label;
    @FXML private MenuBar menuBar;
    @FXML private Menu fileMenu;

    public record MachineCodeEntry(String address, String hexCode, String binaryCode) {
    }
    @FXML private StackPane codeAreaContainer;
    private CodeArea codeArea;

    @FXML
    public void initialize() {
        setupCodeEditor();
        setupTables();
        LanguageManager.setLanguage("en");

        menuBar.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    try {
                        updateUIText();
                    } catch (Exception e) {
                        System.err.println("Error in updateUIText: " + e.getMessage());
                    }
                });
            }
        });
    }

    @FXML
    private void setEnglish() {
        LanguageManager.setLanguage("en");
        updateUIText();
    }

    @FXML
    private void setTurkish() {
        LanguageManager.setLanguage("tr");
        updateUIText();
    }

    @FXML
    public void updateUIText() {
        Scene scene = menuBar.getScene();
        if (scene != null) {
            updateTexts(scene.getRoot());
        }
    }

    private void updateTexts(Node node) {
        try {
            if (node instanceof MenuBar) {
                MenuBar menuBar = (MenuBar) node;
                for (Menu menu : menuBar.getMenus()) {
                    updateMenuTexts(menu);
                }
                return;
            }

            if (node instanceof Labeled && !(node instanceof MenuBar)) {
                updateLabeledNode((Labeled) node);
            }

            if (node instanceof TableView) {
                updateTableColumns((TableView<?>) node);
            }

            if (node instanceof TabPane) {
                updateTabPane((TabPane) node);
            }

            if (node instanceof Control) {
                updateTooltip((Control) node);
            }

            if (node instanceof Parent) {
                Parent parent = (Parent) node;
                for (Node child : parent.getChildrenUnmodifiable()) {
                    updateTexts(child);
                }
            }
        } catch (Exception e) {
            System.err.println("[" + getCurrentDateTime() + "] Error updating texts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateLabeledNode(Labeled labeled) {
        String id = labeled.getId();
        if (id != null && !labeled.textProperty().isBound()) {
            String key = determineKey(id, labeled);
            String text = LanguageManager.getString(key);
            if (text != null) {
                labeled.setText(text);
                logUpdate("Labeled", id, key, text);
            }
        }
    }

    private void updateMenuTexts(Menu menu) {
        String id = menu.getId();
        if (id != null && !menu.textProperty().isBound()) {
            String key = id.replace("_", ".");
            String text = LanguageManager.getString(key);
            if (text != null) {
                menu.setText(text);
                logUpdate("Menu", id, key, text);
            }
        }

        for (MenuItem item : menu.getItems()) {
            if (item instanceof Menu) {
                updateMenuTexts((Menu) item);
            } else {
                updateMenuItem(item);
            }
        }
    }

    private void updateMenuItem(MenuItem item) {
        String id = item.getId();
        if (id != null && !item.textProperty().isBound()) {
            String key = id.replace("_", ".");
            String text = LanguageManager.getString(key);
            if (text != null) {
                item.setText(text);
                logUpdate("MenuItem", id, key, text);
            }
        }
    }

    private void updateTableColumns(TableView<?> tableView) {
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            String id = column.getId();
            if (id != null && !column.textProperty().isBound()) {
                String key = "table.column." + id.replace("column_", "");
                String text = LanguageManager.getString(key);
                if (text != null) {
                    column.setText(text);
                    logUpdate("TableColumn", id, key, text);
                }
            }
        }
    }

    private void updateTabPane(TabPane tabPane) {
        for (Tab tab : tabPane.getTabs()) {
            String id = tab.getId();
            if (id != null && !tab.textProperty().isBound()) {
                String key = "label." + id.substring(id.indexOf('_') + 1);
                String text = LanguageManager.getString(key);
                if (text != null) {
                    tab.setText(text);
                    logUpdate("Tab", id, key, text);
                }
            }
            if (tab.getContent() != null) {
                updateTexts(tab.getContent());
            }
        }
    }

    private void updateTooltip(Control control) {
        Tooltip tooltip = control.getTooltip();
        if (tooltip != null) {
            String id = control.getId();
            if (id != null) {
                String tooltipKey = "tooltip." + id.replace("_", ".");
                String text = LanguageManager.getString(tooltipKey);
                if (text != null) {
                    tooltip.setText(text);
                    logUpdate("Tooltip", id, tooltipKey, text);
                }
            }
        }
    }

    private String determineKey(String id, Node node) {
        if (node instanceof Button) {
            return "button." + id.replace("button_", "");
        } else if (node instanceof Label) {
            return "label." + id.replace("label_", "");
        } else {
            return id.replace("_", ".");
        }
    }

    private void logUpdate(String type, String id, String key, String text) {
        if (shouldLogUpdates()) {
            System.out.printf("[%s] Updated %s: ID=%s, Key=%s, Text='%s'%n",
                    getCurrentDateTime(), type, id, key, text);
        }
    }

    private boolean shouldLogUpdates() {
        return true;
    }

    private String getCurrentDateTime() {
        return java.time.LocalDateTime.now().toString();
    }
    private void setupTables() {
        TableColumn<MachineCodeEntry, String> addressCol = new TableColumn<>("Address");
        TableColumn<MachineCodeEntry, String> hexCol = new TableColumn<>("Hex");
        TableColumn<MachineCodeEntry, String> binaryCol = new TableColumn<>("Binary");

        addressCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().address()));
        hexCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().hexCode()));
        binaryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().binaryCode()));

        machineCodeTable.getColumns().setAll(addressCol, hexCol, binaryCol);

        TableColumn<SymbolTable.SymbolTableEntry, String> symbolCol = new TableColumn<>("Symbol");
        TableColumn<SymbolTable.SymbolTableEntry, String> symAddressCol = new TableColumn<>("Address");

        symbolCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().symbol()));
        symAddressCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().address()));

        symbolTableView.getColumns().setAll(symbolCol, symAddressCol);
    }
    private void setupCodeEditor() {
        codeArea = new CodeArea();

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.setStyle(
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                        "-fx-font-size: 14px;"+
                        "-fx-caret-color: white;"   
        );


        String template = """
            ORG 100
            START, LDA NUM    ; Load number
            ADD ONE          ; Add 1
            STA SUM         ; Store result
            HLT             ; Halt program
            NUM, DEC 10     ; Number to increment
            ONE, DEC 1      ; Constant 1
            SUM, DEC 0      ; Result location
            END""";
        codeArea.replaceText(template);

        codeAreaContainer.getChildren().add(codeArea);
    }
    @FXML
    private void assembleCode() {
        try {
            errorArea.clear();
            String inputCode = codeArea.getText();
            List<String> sourceCode = Arrays.asList(inputCode.split("\n"));
            Assembler assembler = new Assembler(sourceCode);

            assembler.firstPass();
            updateSymbolTable(assembler.getSymbolTable());

            assembler.secondPass();
            String machineCode = assembler.getMachineCode();
            updateMachineCodeTable(machineCode);

            label_status_label.setText("Assembly completed successfully");

        } catch (Exception e) {
            errorArea.setText("Error: " + e.getMessage());
            label_status_label.setText("Assembly failed");
        }
    }

    @FXML
    private void newFile() {
        codeArea.clear();
        errorArea.clear();
        machineCodeTable.getItems().clear();
        symbolTableView.getItems().clear();
        label_status_label.setText("New file created");
    }

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Assembly File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Assembly files (*.asm)", "*.asm")
        );

        File file = fileChooser.showOpenDialog(codeArea.getScene().getWindow());
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                codeArea.replaceText(content);
                label_status_label.setText("File opened: " + file.getName());
            } catch (IOException e) {
                errorArea.setText("Error opening file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Assembly File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Assembly files (*.asm)", "*.asm")
        );

        File file = fileChooser.showSaveDialog(label_status_label.getScene().getWindow());
        if (file != null) {
            try {
                Files.write(file.toPath(), codeArea.getText().getBytes());
                label_status_label.setText("File saved: " + file.getName());
            } catch (IOException e) {
                errorArea.setText("Error saving file: " + e.getMessage());
            }
        }
    }
    @FXML
    private void saveHex() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Hex Code");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Hex Code", "*.hex")
        );

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                StringBuilder hexContent = new StringBuilder();
                for (MachineCodeEntry entry : machineCodeTable.getItems()) {
                    hexContent.append(entry.hexCode()).append("\n");
                }

                Files.writeString(file.toPath(), hexContent.toString());
                label_status_label.setText("Hex code saved: " + file.getName());
            } catch (IOException e) {
                errorArea.setText("Error saving hex code: " + e.getMessage());
            }
        }
    }

    private void updateMachineCodeTable(String machineCode) {
        ObservableList<MachineCodeEntry> entries = FXCollections.observableArrayList();
        String[] lines = machineCode.split("\n");

        for (String line : lines) {
            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    entries.add(new MachineCodeEntry(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim()
                    ));
                }
            }
        }

        machineCodeTable.setItems(entries);
    }

    private void updateSymbolTable(SymbolTable symbolTable) {
        ObservableList<SymbolTable.SymbolTableEntry> entries =
                FXCollections.observableArrayList(symbolTable.getEntries());
        symbolTableView.setItems(entries);
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    @FXML
    private void showInstructionSet() {
        DialogUtils.showInstructionSet();
    }

    @FXML
    private void showDocumentation() {
        DialogUtils.showDocumentation();
    }

    @FXML
    private void showAbout() {
        DialogUtils.showAbout();
    }
}


