package com.ms.trictrac.game

class BearedOff(hashIndex: Int, initCheckers: Int, board: OneColorPerspectiveBoard): CheckerContainer(hashIndex, initCheckers, board) {

    override fun isPlayable() = board.quadrantList.none { it.type != QuadrantType.HOME && it.hasCheckers() }
    override var name = "off"

}