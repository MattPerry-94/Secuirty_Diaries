module org.example.security_diaries {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires jdk.jconsole;
    requires mysql.connector.j;
    requires javafx.graphics;

    opens org to javafx.fxml;
    exports org.security_diaries;
    opens org.security_diaries to javafx.fxml;
    opens org.models to javafx.base;
}