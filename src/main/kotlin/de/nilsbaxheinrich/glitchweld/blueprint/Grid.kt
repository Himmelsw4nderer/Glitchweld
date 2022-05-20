package de.nilsbaxheinrich.glitchweld.blueprint

class Grid(depth:Int , width:Int, itHeight:Int) {
    //rows
    var rows: MutableList<Row> = mutableListOf()

    init {
        repeat(depth) {
            rows.add(Row(width, itHeight, it))
        }
    }

    fun fill(block: Block) {
        rows[block.x].fill(block)
    }
}