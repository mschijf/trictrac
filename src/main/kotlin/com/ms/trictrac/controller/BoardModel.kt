package com.ms.trictrac.controller

import com.ms.trictrac.game.*

//todo: remove delta fields
class BoardModel(board: Board) {
    val points : Array<PointModel>
    val bar : PointModel
    val bearedOff : PointModel
    val whiteToMove: Boolean
    val diceList = board.diceValues
    val activeDiceIndex = board.activeDiceIndex()
    val takeBackPossible: Boolean
    val roleDicePossible: Boolean

    init {
        whiteToMove = board.colorToMove == Color.WHITE
        val moves = board.generateAllMoves(diceList.subList(activeDiceIndex, diceList.size))
        val playablePoints = if (moves.isEmpty()) emptyList() else moves.map{it.movePartList.first().from}

        takeBackPossible = board.movePartStack.isNotEmpty()
        roleDicePossible = moves.isEmpty()

        points = Array(POINTS_PER_BOARD) {i -> PointModel(
            name = board.pointList[i].name,
            checkerCountWhite = board.pointList[i].checkerCount(Color.WHITE),
            checkerCountBlack = board.pointList[i].checkerCount(Color.BLACK),
            deltaWhite = 0,
            deltaBlack = 0,
            isPlayableWhite = whiteToMove && playablePoints.any{it == board.pointList[i]},
            isPlayableBlack = !whiteToMove && playablePoints.any{it == board.pointList[i]})
        }

        bar = PointModel(
            name = board.bar.name,
            checkerCountWhite = board.bar.checkerCount(Color.WHITE),
            checkerCountBlack = board.bar.checkerCount(Color.BLACK),
            deltaWhite = 0,
            deltaBlack = 0,
            isPlayableWhite = whiteToMove && playablePoints.any{it == board.bar},
            isPlayableBlack = !whiteToMove && playablePoints.any{it == board.bar})

        bearedOff = PointModel(
            name = board.bearedOff.name,
            checkerCountWhite = board.bearedOff.checkerCount(Color.WHITE),
            checkerCountBlack = board.bearedOff.checkerCount(Color.BLACK),
            deltaWhite = 0,
            deltaBlack = 0,
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

