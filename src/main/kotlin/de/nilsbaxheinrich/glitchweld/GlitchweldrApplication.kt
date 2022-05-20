package de.nilsbaxheinrich.glitchweld

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("edit-window-controller.fxml"))
        val scene = Scene(fxmlLoader.load(), 1080.0, 720.0)
        stage.title = "Glitchweldr"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}