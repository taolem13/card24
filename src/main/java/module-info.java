module org.example.card24 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;


    opens org.example.card24 to javafx.fxml;
    exports org.example.card24;
}