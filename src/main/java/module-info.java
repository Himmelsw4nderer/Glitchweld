module de.nilsbaxheinrich.glitchweld {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.ikonli.javafx;

    opens de.nilsbaxheinrich.glitchweld to javafx.fxml;
    exports de.nilsbaxheinrich.glitchweld;
}