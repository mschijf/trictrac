package com.ms.trictrac.game

abstract class CheckerContainer(
    val hashIndex: Int,
    protected val board: Board) {

    abstract var name: String
    abstract fun isPlayableFor(color: Color): Boolean

    private val checkerCount = IntArray(2)

    fun hasCheckers(color: Color) = checkerCount[color.ordinal] > 0
    fun checkerCount(color: Color) = checkerCount[color.ordinal]

    fun initCheckers(color: Color, initCheckerCount: Int) {
        checkerCount[color.ordinal] = initCheckerCount
    }

    fun addChecker(color: Color) {
        ++checkerCount[color.ordinal]
    }
    fun removeChecker(color: Color) {
        --checkerCount[color.ordinal]
    }
    fun isEmpty(color: Color) = checkerCount[color.ordinal] == 0

    override fun toString(): String {
        return "${Color.WHITE.letter}$checkerCount[Color.WHITE.ordinal] ${Color.BLACK.letter}$checkerCount[Color.BLACK.ordinal]"
    }

}