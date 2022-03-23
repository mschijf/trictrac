package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import com.ms.trictrac.game.Color
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardString: String): Pair<BoardModel, String> {
        val tmp = "w1-b9|w3-b1-b1-w4-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w7-b2@w"
        val board = Board(tmp)
        return BoardModel(board, emptyList()) to board.toString()
    }

    fun doMovePart(boardString: String, checkerContainerName: String): Pair<BoardModel, String> {
        val board = Board(boardString)
        val movePart = board.getMovePart(checkerContainerName)
        board.doMovePart(movePart)
        return BoardModel(board, listOf(movePart)) to board.toString()
    }

}



