package com.ms.trictrac.game

class Move (
    val movePartList: List<MovePart>,
    val usedAllDice: Boolean) {

    var hashValue: Long ; private set
    init {
        hashValue = 0L
        movePartList.forEach { hashValue = hashValue xor it.hashValue }
    }

    override fun toString() = "<${movePartList.joinToString { it.toString() } + if (usedAllDice) "" else ", ~~"}>"
}