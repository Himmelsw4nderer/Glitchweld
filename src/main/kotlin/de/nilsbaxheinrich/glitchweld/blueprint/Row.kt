package de.nilsbaxheinrich.glitchweld.blueprint

class Row(width:Int, itHeight:Int, itDepth:Int) {
    //blocks
    var blocks: MutableList<Block> = mutableListOf()


    init {
        repeat(width){
            blocks.add(Block(itDepth, it, itHeight))
        }
    }

    fun fill(block: Block) {
        blocks[block.y].add(block)
    }
}