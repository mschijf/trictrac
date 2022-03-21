package com.ms.trictrac.game

class OneColorPerspectiveBoard(
    private val color: Color,
    inputString: String) {

    val quadrantList = buildList(QUADRANTS_PER_BOARD) {
        for (quadrantType in QuadrantType.values())
            add(Quadrant(this@OneColorPerspectiveBoard, quadrantType))
    }
    val pointList = quadrantList.map{ quadrant -> quadrant.pointList}.flatten()
    val bar = Bar(POINTS_PER_BOARD, MAX_CHECKERS_PER_GAME, this)
    val bearedOff = BearedOff(POINTS_PER_BOARD+1, 0, this)

    init {
        val numberList = transformInputToIntList(inputString)
        if (numberList.isEmpty())
            throw Exception("worng format input String for board")
        bar.initCheckers(numberList.first())
        pointList.forEach { it.initCheckers(numberList[it.pointIndex+1]) }
        bearedOff.initCheckers(numberList.last())
    }

    private fun transformInputToIntList(inputString: String): List<Int> {
        val parts = inputString.split(' ', '|', ',').filter { it.isNotBlank() }
        if (parts.size != POINTS_PER_BOARD+2)
            return emptyList()
        val allNumbers = parts.mapNotNull { it.toIntOrNull() }
        if (allNumbers.size != POINTS_PER_BOARD+2)
            return emptyList()
        if (allNumbers.any {it < 0})
            return emptyList()
        if (allNumbers.sumOf { it } != MAX_CHECKERS_PER_GAME)
            return emptyList()
        if (allNumbers.subList(1, POINTS_PER_BOARD+1).any {it > MAX_CHECKERS_PER_POINT})
            return emptyList()
        return allNumbers
    }

    //------------------------------------------------------------------------------------------------------------------

    fun getQuadrant(quadrantType: QuadrantType) = quadrantList[quadrantType.ordinal]

    //------------------------------------------------------------------------------------------------------------------

    fun generateMoves(diceList: List<Int>): List<Move> {
        val moveList = generateAllMoves(diceList)
        return if (moveList.none { it.usedAllDice }) {
            moveList.distinctBy { it.hashValue }
        } else {
            moveList.filter {it.usedAllDice}.distinctBy { it.hashValue }
        }
    }

    fun generateAllMoves(diceList: List<Int>, moveBuilder: List<MovePart> = emptyList()): List<Move> {
        if (diceList.isEmpty()) {
            return listOf(Move(movePartList = moveBuilder, usedAllDice = true))
        }

        val possibleMoveParts = getPossibleMoveParts(diceList.first())
        if (possibleMoveParts.isEmpty()) {
            return listOf(Move(movePartList = moveBuilder, usedAllDice = false))
        }
        val allMoves = mutableListOf<Move>()
        for (movePart in possibleMoveParts) {
            doMovePart(movePart)
            allMoves += generateAllMoves(diceList.subList(fromIndex = 1, toIndex = diceList.size), moveBuilder.plus(movePart))
            undoMovePart(movePart)
        }
        return allMoves
    }

    private fun getPossibleMoveParts(dice: Int): List<MovePart> {
        if (bar.hasCheckers()) {
            return if (pointList[dice-1].isPlayable())
                listOf(MovePart(bar, pointList[dice-1]))
            else
                emptyList()
        }

        val movePartList = pointList
            .filter { it.hasCheckers() }
            .filter { it.pointIndex + dice < POINTS_PER_BOARD }
            .filter { pointList[it.pointIndex+dice].isPlayable() }
            .map { MovePart(it, pointList[it.pointIndex+dice], pointList[it.pointIndex+dice].isBluntByOpponent() ) }

        return if (bearedOff.isPlayable() && pointList[POINTS_PER_BOARD-dice].hasCheckers())
            movePartList.plus(MovePart(pointList[POINTS_PER_BOARD-dice], bearedOff))
        else
            movePartList
    }

    //------------------------------------------------------------------------------------------------------------------

    private fun doMovePart(movePart: MovePart) {
        movePart.from.removeChecker()
        movePart.to.addChecker()
        if (movePart.isCapture)
            (movePart.to as Point).removeOpponentChecker()
    }

    private fun undoMovePart(movePart: MovePart) {
        movePart.to.removeChecker()
        movePart.from.addChecker()
        if (movePart.isCapture)
            (movePart.to as Point).addOpponentChecker()
    }

    fun doMove(move: Move) {
        move.movePartList.forEach { doMovePart(it) }
    }

    //------------------------------------------------------------------------------------------------------------------

    override fun toString(): String {
        var result = "%c %2d    ".format(color.letter, bar.checkerCount)
        result += quadrantList.joinToString(separator = " : ") { it.toString() }
        return "$result    %2d".format(bearedOff.checkerCount)
    }

    fun toStringReversed(): String {
        var result = "%c %2d    ".format(color.letter, bearedOff.checkerCount)
        result += quadrantList.reversed().joinToString(separator = " : ") { it.toStringReversed() }
        return "$result    %2d".format(bar.checkerCount)
    }

}