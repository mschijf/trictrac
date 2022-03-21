package com.ms.trictrac.game

class Bar(hashIndex: Int, board: Board): CheckerContainer(hashIndex, board) {
    override var name = "bar"
    override fun isPlayableFor(color: Color) = false
}