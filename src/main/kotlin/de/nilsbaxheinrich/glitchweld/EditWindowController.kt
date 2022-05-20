package de.nilsbaxheinrich.glitchweld

import de.nilsbaxheinrich.glitchweld.blueprint.Block
import de.nilsbaxheinrich.glitchweld.blueprint.Blueprint
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints
import javafx.stage.FileChooser
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import kotlin.math.sqrt

class EditWindowController {

    //unsaved changes?
    private var unsavedChanges: Boolean = false

    //json data to edit
    private var blueprintJsonString: String = ""

    //blueprint to edit
    private lateinit var blueprint: Blueprint

    //selected grid
    private var selectedGrid: Int = 0

    //uiGrid
    @FXML
    private lateinit var uiGrid: GridPane

    //selected grid
    @FXML
    private lateinit var layerSelector: ChoiceBox<Int>

    @FXML
    fun initialize() {
        //set up layer selector
        layerSelector.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            selectedGrid = newValue.toInt()
            display()
        }

        println("EditWindowController initialized")
    }

    //close the application
    fun close() {
        //check if saved
        if (unsavedChanges) {
            //asking to save the changes
            val alert = Alert(AlertType.INFORMATION)
            alert.title = "Save Changes?"
            alert.headerText = "Do you want to save the changes?"
            alert.contentText = "If you don't save, your changes will be lost."
            alert.showAndWait()

            if (alert.result == ButtonType.OK) {
                //saving the changes
                println("Saving changes")
            } else {
                //discarding the changes
                println("Changes discarded")
            }
        }
        Platform.exit()
    }

    //save the changes
    fun save() {
        //create a file selection box to save the blueprint.json file
        val fileChooser = FileChooser()
        fileChooser.title = "Save Blueprint"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Blueprint", "*.json"))
        val file = fileChooser.showSaveDialog(null)


        println("Saving changes")
    }

    //load a new file
    fun load() {
        //create a file selection box for blueprint.json files
        val fileChooser = FileChooser()
        fileChooser.title = "Open Blueprint"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Blueprint", "*.json"))
        val file = fileChooser.showOpenDialog(null)

        //check if a file was selected
        if (file != null) {
            println("Loading file: " + file.absolutePath)
            //load the json file into blueprintJsonData
            blueprintJsonString = file.readText()
            //update the grid
            loadBlueprint()
        }
        println("Loading new file")
    }

    //create a new blueprint from the json data
    private fun loadBlueprint() {
        //disable the layer selection for loading the blueprint
        layerSelector.isDisable = true
        //list of all block with their positions
        val blocks = mutableListOf<Block>()
        val blueprintJsonData: JSONObject = JSONParser().parse(blueprintJsonString) as JSONObject
        val bodiesJSONArray = blueprintJsonData["bodies"] as JSONArray
        bodiesJSONArray.forEach { it1 ->
            val bodyJSONObject = it1 as JSONObject
            val childsJSONArray = bodyJSONObject["childs"] as JSONArray
            childsJSONArray.forEach { it2 ->
                val childJSONObject = it2 as JSONObject
                val posJSONObject = childJSONObject["pos"] as JSONObject

                //create a new block
                var x = (posJSONObject["x"] as Long).toInt()
                var y = (posJSONObject["y"] as Long).toInt()
                var z = (posJSONObject["z"] as Long).toInt()

                val xaxis = childJSONObject["xaxis"]
                val zaxis = childJSONObject["zaxis"]

                var xAxisInt = 0
                var zAxisInt = 0

                if (xaxis != null) {
                    xAxisInt = (xaxis as Long).toInt()
                }
                if (zaxis != null) {
                    zAxisInt = (zaxis as Long).toInt()
                }

                println("x1: $x, y1: $y, z1: $z")

                //get real position
                when (xAxisInt) {
                    -3 -> when (zAxisInt) {
                        -2 -> {
                            y--
                            z--
                        }
                        -1 -> {
                            x--
                            y--
                            z--
                        }
                        1 -> {
                            x++
                            z--
                        }
                        2 -> {
                            x--
                            z--
                        }
                    }
                    -2 -> when (zAxisInt) {
                        -3 -> {
                            x--
                            z--
                        }
                        -1 -> {
                            x--
                            y--
                        }
                        1 -> {
                            x--
                            z--
                        }
                        3 -> {
                            y--
                        }
                    }
                    -1 -> when (zAxisInt) {
                        -3 -> {
                            y--
                            z--
                        }
                        -2 -> {
                            x--
                            y--
                        }
                        2 -> {
                            y--
                            z++
                        }
                        3 -> {
                            x--
                            y--
                        }
                    }
                    1 -> when (zAxisInt) {
                        -3 -> {
                            x--
                            z--
                        }
                        -2 -> {
                            x--
                            z++
                        }
                    }
                    2 -> when (zAxisInt) {
                        -3 -> {
                            z--
                        }
                        -1 -> {
                            y--
                            z--
                        }
                    }
                    3 -> when (zAxisInt) {
                        -2 -> {
                            x--
                            y--
                        }
                        -1 -> {
                            x++
                            y--
                        }
                        1 -> {
                            y--
                        }
                    }
                }

                //calculate the distance from the edge of the grid
                val distanceFromEdge = sqrt((x*x + y*y + z*z).toDouble())

                println("x2: $x, y2: $y, z2: $z | distance: $distanceFromEdge")



                println("x: $xAxisInt,z: $zAxisInt")

                val color = childJSONObject["color"] as String

                val block = Block(x, y, z, color)
                //depth, width, height
                block.jsonObjects.add(childJSONObject)
                blocks.add(block)
            }

        }

        //getting the boundaries of the blueprint
        val offsetX: Int = blocks.minOf { it.x }
        val offsetY: Int = blocks.minOf { it.y }
        val offsetZ: Int = blocks.minOf { it.z }
        var maxX: Int = blocks.maxOf { it.x }
        var maxY: Int = blocks.maxOf { it.y }
        var maxZ: Int = blocks.maxOf { it.z }

        maxX += 1 + -offsetX
        maxY += 1 + -offsetY
        maxZ += 1 + -offsetZ

        println("offsetX:$offsetX, offsetY:$offsetY, offsetZ:$offsetZ")
        println("maxX:$maxX, maxY:$maxY, maxZ:$maxZ")

        //create the blueprint
        blueprint = Blueprint(maxX, maxY, maxZ)

        blocks.forEach {
            it.minus(offsetX, offsetY, offsetZ)
            blueprint.fill(it)
        }

        //reset the layer selector
        layerSelector.value = 0

        //update the grid
        display()

        println("${blueprint.grids.size} ${layerSelector.items.size}")
        //add or remove layers from layerSelector based on the blueprint
        if (blueprint.grids.size > layerSelector.items.size) {
            layerSelector.items.addAll(layerSelector.items.size  until blueprint.grids.size)
            println(layerSelector.items.size  until blueprint.grids.size)
            println(layerSelector.items)
        } else if (blueprint.grids.size < layerSelector.items.size) {
            layerSelector.items.removeIf { it >= blueprint.grids.size }
            println(layerSelector.items)
        }

        //enable the layer selection
        layerSelector.isDisable = false
    }

    private fun display() {
        //display the selected grid
        //clear the grid
        uiGrid.children.clear()
        uiGrid.rowConstraints.clear()
        uiGrid.columnConstraints.clear()

        //display the grid
        val grid = blueprint.grids[selectedGrid]
        val depth = grid.rows.size
        val width = grid.rows[0].blocks.size

        //row constraints
        repeat(depth) {
            val row = RowConstraints(Const.BLOCK_SIZE)
            uiGrid.rowConstraints.add(row)
        }

        //column constraints
        repeat(width) {
            val column = ColumnConstraints(Const.BLOCK_SIZE)
            uiGrid.columnConstraints.add(column)
        }

        //display the blocks
        repeat(depth) { x ->
            repeat(width) { y ->
                val block = grid.rows[x].blocks[y]
                uiGrid.add(block.anchorPane, x, y)
            }
        }
    }
}