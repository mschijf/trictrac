package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import org.springframework.stereotype.Service

@Service
class GameService {
    fun getBoard(boardString: String): Pair<BoardModel, String> {
//        val tmp = "w0 z0  | w4 z2 0 w4 z2 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | w7 z0\n"
        val tmp = "0  | 4 0 0 4 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 7 \n" +
                  "11 | 0 2 0 0 2 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0"
        val board = Board(tmp)
        return BoardModel(board) to board.toString()
    }
}



