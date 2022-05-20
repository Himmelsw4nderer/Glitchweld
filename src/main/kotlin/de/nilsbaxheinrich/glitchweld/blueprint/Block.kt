package de.nilsbaxheinrich.glitchweld.blueprint

import de.nilsbaxheinrich.glitchweld.Const
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import org.json.simple.JSONObject

class Block() {

    constructor(x: Int, y: Int, z: Int) : this() {
        this.x = x
        this.y = y
        this.z = z

        setupUI()
    }


    constructor(x: Int, y: Int, z: Int, color:String) : this() {
        this.x = x
        this.y = y
        this.z = z
        this.color = color

        setupUI()
    }

    private fun setupUI() {
        //set up the ui
        anchorPane.children.add(rectangle)
        anchorPane.children.add(label)

        //default values
        rectangle.opacity = Const.DEACTIVATED_OPACITY
        rectangle.stroke = Paint.valueOf("000000")
        rectangle.strokeWidth = 1.0
        rectangle.width = Const.BLOCK_SIZE
        rectangle.height = Const.BLOCK_SIZE

        //hover effect
        rectangle.setOnMouseEntered {
            rectangle.opacity = Const.HOVER_OPACITY
        }

        label.setOnMouseEntered {
            rectangle.opacity = Const.HOVER_OPACITY
        }

        rectangle.setOnMouseExited {
            //reset opacity
            if(jsonObjects.isEmpty()) {
                rectangle.opacity = Const.DEACTIVATED_OPACITY
            } else {
                rectangle.opacity = 1.0
            }
        }

        label.setOnMouseExited {
            //reset opacity
            if(jsonObjects.isEmpty()) {
                rectangle.opacity = Const.DEACTIVATED_OPACITY
            } else {
                rectangle.opacity = 1.0
            }
        }

        //on click
        rectangle.setOnMouseClicked {
            clicked()
        }

        label.setOnMouseClicked {
            clicked()
        }

        label.font = javafx.scene.text.Font.font("Arial", Const.BLOCK_SIZE)
        label.resize(Const.BLOCK_SIZE, Const.BLOCK_SIZE)
        label.alignment = javafx.geometry.Pos.CENTER
    }

    //rectangle
    @FXML
    var rectangle = Rectangle()
    //label
    @FXML
    var label = Label()
    //anchorpane
    @FXML
    var anchorPane = AnchorPane()

    var jsonObjects: MutableList<JSONObject> = mutableListOf()
    //position
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    var color: String = "777777"

    fun plus(x: Int,y: Int,z: Int): Block {
        this.x += x
        this.y += y
        this.z += z
        return this
    }
     fun minus(x: Int,y: Int,z: Int): Block {
         this.x -= x
         this.y -= y
         this.z -= z
         return this
     }

    fun add(block: Block) {
        jsonObjects.addAll(block.jsonObjects)
        //activate square
        rectangle.fill = Paint.valueOf(block.color)
        rectangle.opacity = 1.0

        label.text = jsonObjects.size.toString()
    }

    private fun clicked() {
        println("x:$x y:$y z:$z")
        jsonObjects.forEach {
            println("$it")
        }
    }
}