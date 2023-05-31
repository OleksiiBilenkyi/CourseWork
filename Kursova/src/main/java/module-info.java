module com.example.kursova {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jline;
    requires org.jetbrains.annotations;

    opens com.example.kursova to com.google.gson, javafx.fxml;
    exports com.example.kursova;
}