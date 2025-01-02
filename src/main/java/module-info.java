module com.canerfk.mano {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;

    exports com.canerfk.mano.assembler;
    exports com.canerfk.mano.application;
    opens com.canerfk.mano.application to javafx.fxml;
}