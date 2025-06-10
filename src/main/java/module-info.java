module org.chekizybra.practika {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires okhttp3;
    requires jdk.jsobject;
    requires org.json;

    opens org.chekizybra.practika to javafx.fxml;
    exports org.chekizybra.practika;
}