package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardString: String): Pair<BoardModel, String> {
        val tmp = "w0 b9  | w4 b2 0 w4 b2 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | w7 b2"
        val board = Board(tmp)
        return BoardModel(board) to board.toString()
    }
}



