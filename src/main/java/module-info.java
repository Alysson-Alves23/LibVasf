module com.libvasf.libvasf {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.libvasf to javafx.fxml;
    exports com.libvasf;
    exports com.libvasf.controllers;
    opens com.libvasf.controllers to javafx.fxml;
}