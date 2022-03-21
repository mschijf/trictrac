package com.ms.trictrac.game

const val DEFAULT_BOARD_STRING = ""
class Board(boardString: String = DEFAULT_BOARD_STRING) {

    //format boardString
    //two lines separated by a "\n". Each line has 26 numbers
    //   number 1       : number of checkers in bar
    //   number 2 .. 25 : number of checkers in each point withc corresponding point index
    //   number 26      : number of checkers bearedOf

    val boardWhite: OneColorPerspectiveBoard
    val boardBlack: OneColorPerspectiveBoard

    init {
        if (boardString.isBlank()) {
            boardWhite = OneColorPerspectiveBoard(Color.WHITE)
            boardBlack = OneColorPerspectiveBoard(Color.BLACK)
        } else {
            val splitted = boardString.split('\n')
            if (splitted.size != 2)
                throw Exception("expected two strings separated by a '\n'")
            boardWhite = OneColorPerspectiveBoard(Color.WHITE, splitted[0])
            boardBlack = OneColorPerspectiveBoard(Color.BLACK, splitted[1])
        }
        boardWhite.pointList.forEach { it.setOpponentPoint(boardBlack.pointList[POINTS_PER_BOARD - it.pointIndex - 1]) }
        boardBlack.pointList.forEach { it.setOpponentPoint(boardWhite.pointList[POINTS_PER_BOARD - it.pointIndex - 1]) }
        if (boardWhite.pointList.any { it.checkerCount > 0 && it.opponentPoint.checkerCount > 0 })
            throw Exception("Both black and whit checkers on same Point")
    }

    override fun toString(): String {
        return boardWhite.toString() + "\n" + boardBlack.toString()
    }

}