package com.ms.trictrac.game

enum class QuadrantType {
    START,
    OUTER1,
    OUTER2,
    HOME
}

class Quadrant (board: OneColorPerspectiveBoard,
                val type: QuadrantType) {
    val pointList = buildList(POINTS_PER_QUADRANT) {
        for (pointIndex in type.ordinal * POINTS_PER_QUADRANT until (type.ordinal + 1) * POINTS_PER_QUADRANT)
            add(Point(0, pointIndex, this@Quadrant, board))
    }

    fun hasCheckers() = pointList.any { it.hasCheckers() }

    override fun toString(): String {
        return pointList.joinToString (separator = " ") { it.toString() }
    }

    fun toStringReversed(): String {
        return pointList.reversed().joinToString (separator = " ") { it.toString() }
    }
}