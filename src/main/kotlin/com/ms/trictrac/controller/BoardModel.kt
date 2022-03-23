package com.ms.trictrac.controller

import com.ms.trictrac.game.*

class BoardModel(board: Board, movedPartList: List<MovePart>) {
    val points : Array<PointModel>
    val bar : PointModel
    val bearedOff : PointModel
    val whiteToMove: Boolean

    init {
        whiteToMove = board.colorToMove == Color.WHITE
        movedPartList.reversed().forEach { board.undoMovePart(it) }
        val oldBoard = Board(board)
        movedPartList.forEach { board.doMovePart(it) }

        val moves = board.generateMoves(listOf(2))
        val playablePoints = if (moves.isEmpty()) emptyList() else moves.map{it.movePartList.first().from}

        points = Array(POINTS_PER_BOARD) {i -> PointModel(
            name = board.pointList[i].name,
            checkerCountWhite = board.pointList[i].checkerCount(Color.WHITE),
            checkerCountBlack = board.pointList[i].checkerCount(Color.BLACK),
            deltaWhite = board.pointList[i].checkerCount(Color.WHITE) - oldBoard.pointList[i].checkerCount(Color.WHITE),
            deltaBlack = board.pointList[i].checkerCount(Color.BLACK) - oldBoard.pointList[i].checkerCount(Color.BLACK),
            isPlayableWhite = whiteToMove && playablePoints.any{it == board.pointList[i]},
            isPlayableBlack = !whiteToMove && playablePoints.any{it == board.pointList[i]})
        }

        bar = PointModel(
            name = board.bar.name,
            checkerCountWhite = board.bar.checkerCount(Color.WHITE),
            checkerCountBlack = board.bar.checkerCount(Color.BLACK),
            deltaWhite = board.bar.checkerCount(Color.WHITE) - oldBoard.bar.checkerCount(Color.WHITE),
            deltaBlack = board.bar.checkerCount(Color.BLACK) - oldBoard.bar.checkerCount(Color.BLACK),
            isPlayableWhite = whiteToMove && playablePoints.any{it == board.bar},
            isPlayableBlack = !whiteToMove && playablePoints.any{it == board.bar})

        bearedOff = PointModel(
            name = board.bearedOff.name,
            checkerCountWhite = board.bearedOff.checkerCount(Color.WHITE),
            checkerCountBlack = board.bearedOff.checkerCount(Color.BLACK),
            deltaWhite = board.bearedOff.checkerCount(Color.WHITE) - oldBoard.bearedOff.checkerCount(Color.WHITE),
            deltaBlack = board.bearedOff.checkerCount(Color.BLACK) - oldBoard.bearedOff.checkerCount(Color.BLACK),
            isPlayableWhite = whiteToMove && playablePoints.any{it == board.bearedOff},
            isPlayableBlack = !whiteToMove && playablePoints.any{it == board.bearedOff})
    }

    class PointModel(
        val name: String,
        val checkerCountWhite: Int,
        val checkerCountBlack: Int,
        val deltaWhite: Int,
        val deltaBlack: Int,
        val isPlayableWhite: Boolean,
        val isPlayableBlack: Boolean)
}

