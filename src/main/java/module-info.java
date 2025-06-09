module org.chekizybra.practika {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.chekizybra.practika to javafx.fxml;
    exports org.chekizybra.practika;
}