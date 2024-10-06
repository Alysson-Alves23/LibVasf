module com.libvasf{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.dotenv;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    opens com.libvasf.models to org.hibernate.orm.core;
    opens com.libvasf.controllers to javafx.fxml;
    opens com.libvasf.controllers.usuario to javafx.fxml;
    exports com.libvasf;
    exports com.libvasf.controllers;
    exports com.libvasf.controllers.usuario;

}