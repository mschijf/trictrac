package com.ms.trictrac.game

import java.util.*

class MovePart (
    val color: Color,
    val from: CheckerContainer,
    val to: CheckerContainer,
    val isCapture: Boolean = false) {

    val hashValue = Hash.move[from.hashIndex][from.checkerCount(color)] xor
            Hash.move[to.hashIndex][to.checkerCount(color)+1] xor
            if (isCapture) Hash.capture[to.hashIndex] else 0L

    override fun toString() = "${from.name}-${to.name}${if(isCapture) "+" else ""}"

    private object Hash {
        private val rnd = Random(0)
        val move = Array(POINTS_PER_BOARD+2) { LongArray(MAX_CHECKERS_PER_GAME+1){rnd.nextLong()} }
        val capture = LongArray(POINTS_PER_BOARD){ rnd.nextLong() }
    }
}
