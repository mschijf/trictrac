package com.ms.trictrac.game

class Bar(hashIndex: Int, initCheckers: Int, board: OneColorPerspectiveBoard): CheckerContainer(hashIndex, initCheckers, board) {
    override fun isPlayable() = false
    override var name = "bar"
}