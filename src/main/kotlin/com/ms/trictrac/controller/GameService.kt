package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardString: String): Pair<BoardModel, String> {
        val tmp = "0  | 4 0 0 4 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 7 \n" +
                "15 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0"
        val board = Board(tmp)
        return BoardModel(board) to board.toString()
    }
}