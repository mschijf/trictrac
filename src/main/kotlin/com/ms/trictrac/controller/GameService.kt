package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import com.ms.trictrac.game.Color
import org.springframework.stereotype.Service

@Service
class GameService {
//    private var board = Board("w1-b9|w3-b1-b1-w4-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w7-b2@w")
//
//    private var board = Board("w0-b11|0-b2-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-w2-w1-0-w2|w10-b2@w")
private var board = Board("")

    fun getBoard(boardString: String): Pair<BoardModel, String> {
//        val tmp = "w1-b9|w3-b1-b1-w4-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w7-b2@w"
//        val tmp = "w0-b9|0-b1-b1-0-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-w0-0-w1-0|w14-b2@w"
//        val board = Board(boardString)
        return BoardModel(board, emptyList()) to board.toString()
    }

    fun getDices(boardString: String): Pair<BoardModel, String> {
//        val board = Board(boardString)
        board.roleDices()
        return BoardModel(board, emptyList()) to board.toString()
    }

    fun doMovePart(boardString: String, checkerContainerName: String): Pair<BoardModel, String> {
//        val board = Board(boardString)
        val movePart = board.getMovePart(checkerContainerName)
        board.doMovePart(movePart)
        return BoardModel(board, listOf(movePart)) to board.toString()
    }

    fun undoMovePart(boardString: String): Pair<BoardModel, String> {
//        val board = Board(boardString)
        board.undoMovePart()
        return BoardModel(board, emptyList()) to board.toString()
    }

}



