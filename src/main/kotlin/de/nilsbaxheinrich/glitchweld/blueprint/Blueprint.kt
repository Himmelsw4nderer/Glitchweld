package de.nilsbaxheinrich.glitchweld.blueprint

class Blueprint(depth:Int , width:Int , height:Int) {

    var grids: MutableList<Grid> = mutableListOf()

    init {
        repeat(height) {
            grids.add(Grid(depth, width, it))
        }
    }

    fun fill(block: Block) {
        grids[block.z].fill(block)
    }
}