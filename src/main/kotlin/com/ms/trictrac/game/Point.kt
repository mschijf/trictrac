package com.ms.trictrac.game

class Point(val pointIndex: Int, board: Board): CheckerContainer(pointIndex, board) {

    override var name = "p${pointIndex+1}"

    override fun isPlayableFor(color: Color): Boolean {
        if (!inStartQuadrant(color) && board.bar.hasCheckers(color))
            return false
        if (checkerCount(color) >= MAX_CHECKERS_PER_POINT)
            return false
        if (checkerCount(color.opponent()) >= 2)
            return false
        if (inHomeQuandrant(color) && board.pointList.any { it.inStartQuadrant(color) && it.hasCheckers(color) } )
            return false
        return true
    }

    fun inHomeQuandrant(color: Color) = if (color == Color.WHITE) pointIndex > 17 else pointIndex < 6
    fun inStartQuadrant(color: Color) = if (color == Color.WHITE) pointIndex < 6 else pointIndex > 17

    fun isBlunt(color: Color) = checkerCount(color) == 1

    override fun toString(): String {
        return if (checkerCount(Color.WHITE) > 0)
            "${Color.WHITE.letter}${checkerCount(Color.WHITE)}"
        else if (checkerCount(Color.BLACK) > 0)
            "${Color.BLACK.letter}${checkerCount(Color.BLACK)}"
        else
            "0"
    }
}