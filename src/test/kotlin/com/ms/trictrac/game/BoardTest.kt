package com.ms.trictrac.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BoardTest {

    @Test
    fun testExactlyFifteenNumbers() {
        assertThrows<Exception> { Board("w15-b15|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b1") }
        assertThrows<Exception> { Board("w15-b15|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w1-b0") }
        assertThrows<Exception> { Board("w5-b0|w1-w1-w1-w1-w1-w1|0-w1-w1-0-0-w1|0-0-0-0-0-0|0-0-0-0-0-0|w0-b15") }
    }

    @Test
    fun testNotANumber() {
        assertThrows<Exception> { Board("w15-b15|0-0-0-0 q 0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b0") }
    }

    @Test
    fun testNot28Pairs() {
        assertThrows<Exception> { Board("w15-b15|0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b0") }
    }

    @Test
    fun testTooMuchCheckersOnAPoint() {
        assertThrows<Exception> { Board("w9-b15|0-0-0-0-0 w6|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b0") }
    }

    @Test
    fun testFirstTwo() {
        assertThrows<Exception> { Board("15-15|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b0") }
        assertThrows<Exception> { Board("w5-w10|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w0-b15") }
        assertThrows<Exception> { Board("b5-b10|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|b0-w15") }
    }

    @Test
    fun testLastTwo() {
        assertThrows<Exception> { Board("b15 w15|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0") }
    }
}