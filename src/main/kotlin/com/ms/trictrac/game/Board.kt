package com.ms.trictrac.game

class Board(whiteBoardString: String, blackBoardString: String) {

    //format: two lines in separate variables. Each has 26 numbers
    //number 1       : number of checkers in bar
    //number 2 .. 25 : number of checkers in each point withc corresponding point index
    //number 26      : number of checkers bearedOf

    val boardWhite = OneColorPerspectiveBoard(Color.WHITE, whiteBoardString)
    val boardBlack = OneColorPerspectiveBoard(Color.BLACK, blackBoardString)

    init {
        boardWhite.pointList.forEach { it.setOpponentPoint(boardBlack.pointList[POINTS_PER_BOARD - it.pointIndex - 1]) }
        boardBlack.pointList.forEach { it.setOpponentPoint(boardWhite.pointList[POINTS_PER_BOARD - it.pointIndex - 1]) }
    }

    override fun toString(): String {
        return boardWhite.toString() + "\n" + boardBlack.toStringReversed()
    }

}