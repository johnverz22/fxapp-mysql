module com.johnverz.fxapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires jbcrypt;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.johnverz.fxapp to javafx.fxml;
    exports com.johnverz.fxapp;

    opens com.johnverz.fxapp.controller to javafx.fxml, javafx.base;
    exports com.johnverz.fxapp.controller;

}