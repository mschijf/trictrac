package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import com.ms.trictrac.game.Color
import org.springframework.stereotype.Service

@Service
class GameService {

    //private var board = Board("")

    fun getNewBoard(): Pair<BoardModel, String> {
        val board = Board()
        return BoardModel(board) to board.toString()
    }

    fun getBoard(boardString: String): Pair<BoardModel, String> {
        val board = Board(boardString)
        return BoardModel(board) to board.toString()
    }

    fun getDices(boardString: String): Pair<BoardModel, String> {
        val board = Board(boardString)
        board.roleDices()
        return BoardModel(board) to board.toString()
    }

    fun doMovePart(boardString: String, checkerContainerName: String): Pair<BoardModel, String> {
        val board = Board(boardString)
        val movePart = board.getMovePart(checkerContainerName)
        board.doMovePart(movePart)
        return BoardModel(board) to board.toString()
    }

    fun undoMovePart(boardString: String): Pair<BoardModel, String> {
        val board = Board(boardString)
        board.undoMovePart()
        return BoardModel(board) to board.toString()
    }

}



