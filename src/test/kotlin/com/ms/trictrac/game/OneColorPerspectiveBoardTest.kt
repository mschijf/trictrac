package com.ms.trictrac.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class OneColorPerspectiveBoardTest {

    @Test
    fun testExactlyFifteenNumbers() {
        //exactly fifteen checkers per player
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "15 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 1") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "15 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 1") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "5 |  1 1 1 1 1 1 , 0 1 1 0 0 1 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "5 |  1 1 1 1 1 1 , 0 1 1 0 0 1 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
    }

    @Test
    fun testNotANumber() {
        //not a number
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "15 | 0 q 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 1") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "15 | 0 q 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 1") }
    }

    @Test
    fun testNotANegativeNumber() {
        //not a number
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "16 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | -1") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "16 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | -1") }
    }

    @Test
    fun testNot26Number() {
        //not a number
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "15 | 0 0 0 0 0, 0 0 0 0 0 0, 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "15 | 0 0 0 0 0, 0 0 0 0 0 0, 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
    }

    @Test
    fun testTooMuchCheckersOnAPoint() {
        //not a number
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.WHITE, "9 | 6 0 0 0 0 0, 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
        assertThrows<Exception> { OneColorPerspectiveBoard(Color.BLACK, "9 | 6 0 0 0 0 0, 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0") }
    }

    @Test
    fun generateMovesTest() {
        val board = Board(
            whiteBoardString = "0  | 4 0 0 4 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 7",
            blackBoardString = "15 | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 | 0")
        val whiteBoard = board.boardWhite
        val moveList = whiteBoard.generateMoves(listOf(1,3))
        println(moveList)
    }

    @Test
    fun generateMovesTest2() {
        val board = Board(
            whiteBoardString = "0  | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 1 1 0 | 13",
            blackBoardString = "0  | 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 0 0 0 , 0 0 0 1 0 0 | 14")
        val whiteBoard = board.boardWhite
        val moveList = whiteBoard.generateMoves(listOf(1,1,2,2,5,5,6,6))
        println(moveList)
    }
}