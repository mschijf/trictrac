package com.ms.trictrac.game

abstract class CheckerContainer(
    val hashIndex: Int,
    initCheckers: Int,
    protected val board: OneColorPerspectiveBoard) {

    abstract var name: String
    var checkerCount = initCheckers; private set
    fun hasCheckers() = checkerCount > 0

    fun initCheckers(initCheckerCount: Int) {
        checkerCount = initCheckerCount
    }

    fun addChecker() {
        ++checkerCount
    }
    fun removeChecker() {
        --checkerCount
    }
    fun isEmpty() = checkerCount == 0
    abstract fun isPlayable(): Boolean
}