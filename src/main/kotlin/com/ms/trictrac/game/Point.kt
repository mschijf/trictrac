package com.ms.trictrac.game

class Point(
    initCheckers: Int,
    val pointIndex: Int,
    private val quadrant: Quadrant,
    board: OneColorPerspectiveBoard): CheckerContainer(pointIndex, initCheckers, board) {

    override var name = "p%d".format(pointIndex+1)

    fun isBlunt() = checkerCount == 1
    override fun isPlayable(): Boolean {
        if (quadrant.type != QuadrantType.START && board.bar.hasCheckers())
            return false
        if (checkerCount >= MAX_CHECKERS_PER_POINT)
            return false
        if (opponentPoint.checkerCount >= 2)
            return false
        if (quadrant.type == QuadrantType.HOME && board.getQuadrant(QuadrantType.START).hasCheckers())
            return false
        return true
    }

    private lateinit var opponentPoint : Point
    fun setOpponentPoint(point: Point) {
        opponentPoint = point
    }

    fun isBluntByOpponent() = opponentPoint.isBlunt()
    fun removeOpponentChecker() {
        if (!isBluntByOpponent())
            throw Exception("Try to remove an opponnent's stone, while it is not blunt")
        opponentPoint.removeChecker()
    }
    fun addOpponentChecker() {
        if (!opponentPoint.isEmpty())
            throw Exception("Try to add an opponnent's stone, while it is not empty")
        opponentPoint.addChecker()
    }

    override fun toString(): String {
        return "$checkerCount"
    }

}