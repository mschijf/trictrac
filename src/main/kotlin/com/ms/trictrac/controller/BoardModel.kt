package com.ms.trictrac.controller

import com.ms.trictrac.game.*

class BoardModel(board: Board) {
    val points = Array(POINTS_PER_BOARD) { i -> PointModel(board.pointList[i].checkerCount(Color.WHITE), board.pointList[i].checkerCount(Color.BLACK))}
    val bar = PointModel(board.bar.checkerCount(Color.WHITE), board.bar.checkerCount(Color.BLACK))
    val bearedOff = PointModel(board.bearedOff.checkerCount(Color.WHITE), board.bearedOff.checkerCount(Color.BLACK))

    class PointModel(val checkerCountWhite: Int, val checkerCountBlack: Int)
}

