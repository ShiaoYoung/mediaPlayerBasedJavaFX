module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.rome4306.myMedioPlayer to javafx.fxml;
    exports com.rome4306.myMedioPlayer;
}