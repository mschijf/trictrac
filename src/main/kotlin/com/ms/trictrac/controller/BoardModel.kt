package com.ms.trictrac.controller

import com.ms.trictrac.game.*

class BoardModel(board: Board) {
    val points = Array(POINTS_PER_BOARD) { i -> PointModel(board.boardWhite.pointList[i].checkerCount, board.boardBlack.pointList[i].checkerCount)}
    val bar = PointModel(board.boardWhite.bar.checkerCount, board.boardBlack.bar.checkerCount)
    val bearedOff = PointModel(board.boardWhite.bearedOff.checkerCount, board.boardBlack.bearedOff.checkerCount)

    class PointModel(val checkerCountWhite: Int, val checkerCountBlack: Int)
}

