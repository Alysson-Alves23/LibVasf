module com.libvasf{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.dotenv;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires org.jboss.jandex;
    requires org.slf4j;
    requires jdk.attach;

    opens com.libvasf.models to org.hibernate.orm.core;
    opens com.libvasf.controllers to javafx.fxml;
    opens com.libvasf.controllers.usuario to javafx.fxml;
    opens com.libvasf.controllers.emprestimo to javafx.fxml;
    opens com.libvasf.controllers.categoria to javafx.fxml;
    opens com.libvasf.controllers.livro to javafx.fxml;
    opens com.libvasf.controllers.cliente to javafx.fxml;

    exports com.libvasf;
    exports com.libvasf.controllers;
    exports com.libvasf.controllers.usuario;
    exports com.libvasf.controllers.usuario.viewControllers;
    opens com.libvasf.controllers.usuario.viewControllers to javafx.fxml;

}