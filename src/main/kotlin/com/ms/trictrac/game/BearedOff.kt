package com.ms.trictrac.game

class BearedOff(hashIndex: Int, board: Board): CheckerContainer(hashIndex, board) {

    override var name = "off"
    override fun isPlayableFor(color: Color): Boolean {
        return board.pointList.none { !it.inHomeQuandrant(color) && it.hasCheckers(color) }
    }
}