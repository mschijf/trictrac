package com.ms.trictrac.game

import java.util.*

const val DEFAULT_BOARD_STRING = ""
private val random = Random()

class Board(boardString: String = "") {

    //todo: movePart stored with indexes instead of objects??

    constructor(board: Board) : this(board.toString())

    val pointList = buildList(POINTS_PER_BOARD) {
        for (index in 0 until POINTS_PER_BOARD)
            add(Point(index, this@Board))
    }
    val bar = Bar(POINTS_PER_BOARD, this)
    val bearedOff = BearedOff(POINTS_PER_BOARD+1, this)
    var colorToMove = Color.WHITE; private set
    var diceValues = emptyList<Int>() ; private set
    var movePartStack = Stack<MovePart>()

    init {
        if (boardString.isNotBlank()) {
            //format: w0-b9|w3-b2-w1-w4-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w7-b2@w@1-2-3-4-5-6@mv1-mv2+-mv3

            val split = boardString.split('@')
            if (split.size != 4)
                throw Exception("wrong format input String for board. Needed 3 '@'")

            initializeBoard(split[0])
            initializeColorToMove(split[1])
            initializeDiceList(split[2])
            initializeMovePartStack(split[3])
        } else {
            colorToMove = Color.WHITE
            bar.initCheckers(Color.WHITE, 15)
            bar.initCheckers(Color.BLACK, 15)
            pointList.forEach { it.initCheckers(Color.WHITE, 0); it.initCheckers(Color.BLACK, 0) }
            bearedOff.initCheckers(Color.WHITE, 0)
            bearedOff.initCheckers(Color.BLACK, 0)
        }
    }

    private fun initializeBoard(inputString: String) {
        val pairList = transformInputToIntList(inputString)
        bar.initCheckers(pairList[0].first!!, pairList[0].second)
        bar.initCheckers(pairList[1].first!!, pairList[1].second)

        for (i in 2 until POINTS_PER_BOARD+2) {
            if (pairList[i].first != null)
                pointList[i-2].initCheckers(pairList[i].first!!, pairList[i].second)
        }

        bearedOff.initCheckers(pairList[POINTS_PER_BOARD+2].first!!, pairList[POINTS_PER_BOARD+2].second)
        bearedOff.initCheckers(pairList[POINTS_PER_BOARD+3].first!!, pairList[POINTS_PER_BOARD+3].second)
    }

    private fun initializeColorToMove(inputString: String) {
        if (inputString.length != 1)
            throw Exception("Unexpected serie of characters after '@'")

        colorToMove = if (inputString[0].uppercaseChar() == 'W')
            Color.WHITE
        else if (inputString[0].uppercaseChar() == 'B')
            Color.BLACK
        else
            throw Exception("Unexpected character after '@'")
    }

    private fun initializeMovePartStack(inputString: String) {
        val parts = inputString.split('-').filter { it.isNotBlank() }
        if (parts.size > 8)
            throw Exception("Format error in input string, movePartStack: error1")
        if (parts.filter{ correctName(it) }.size != parts.size)
            throw Exception("Format error in input string, movePartStack: error2")
        movePartStack.clear()
        parts.withIndex().forEach { (index, name) -> movePartStack.push(createMovePart(index, name)) }
    }

    private fun correctName(name: String): Boolean {
        val tmpName = if (name.length > 1 && name[name.length-1] == '+')
            name.substring(0, name.length-1)
        else
            name
        return (bar.name == tmpName || pointList.any { it.name == tmpName })
    }

    private fun createMovePart(index: Int, name : String) : MovePart {
        val dice = diceValues[index]
        val tmpName = if (name.length > 1 && name[name.length-1] == '+')
            name.substring(0, name.length-1)
        else
            name

        val from = if (bar.name == tmpName) bar else pointList.first { it.name == tmpName }
        val to = if (from == bar) {
            val newPointIndex = if (colorToMove == Color.WHITE) dice-1 else (POINTS_PER_BOARD - dice)
            pointList[newPointIndex]
        } else {
            val factor = if (colorToMove == Color.WHITE) 1 else -1
            val newPointIndex = (from as Point).pointIndex + factor*dice
            if (newPointIndex == POINTS_PER_BOARD || newPointIndex == -1) {
                bearedOff
            } else {
                pointList[newPointIndex]
            }
        }
        return MovePart(colorToMove, from, to, tmpName != name)
    }


    private fun initializeDiceList(inputString: String) {
        val parts = inputString.split('-').filter { it.isNotBlank() }
        if (parts.size > 8 || parts.size % 2 == 1)
            throw Exception("Format error in input string, dice list: error1")
        diceValues = parts
            .mapNotNull{it.toIntOrNull()}
            .filter{ it in 1..6 }
        if (diceValues.size != parts.size)
            throw Exception("Format error in input string, dice list: error2")
    }

    private fun transformInputToIntList(inputString: String): List<Pair<Color?, Int>> {
        val parts = inputString.split('|','-').filter { it.isNotBlank() }
        if (parts.size != POINTS_PER_BOARD+4)
            throw Exception("Format problems in inputString: Error 1")

        val allPairs = parts.filter { it.isNotBlank() }.mapNotNull{toPair(it)}
        if (allPairs.size != POINTS_PER_BOARD + 4)
            throw Exception("Format problems in inputString: Error 2")
        if (allPairs.filter { it.first == Color.WHITE }.sumOf { it.second } != MAX_CHECKERS_PER_GAME)
            throw Exception("Format problems in inputString: Error 3")
        if (allPairs.filter { it.first == Color.BLACK }.sumOf { it.second } != MAX_CHECKERS_PER_GAME)
            throw Exception("Format problems in inputString: Error 4")
        if (allPairs.subList(2, POINTS_PER_BOARD+2).any{ it.second > MAX_CHECKERS_PER_POINT})
            throw Exception("Format problems in inputString: Error 5")

        if (allPairs[0].first == allPairs[1].first || allPairs[0].first == null || allPairs[1].first == null)
            throw Exception("Format problems in inputString: Error 6")
        if (allPairs[POINTS_PER_BOARD+2].first == allPairs[POINTS_PER_BOARD+3].first || allPairs[POINTS_PER_BOARD+2].first == null || allPairs[POINTS_PER_BOARD+3].first == null)
            throw Exception("Format problems in inputString: Error 7")

        return allPairs
    }

    private fun toPair(s: String): Pair<Color?, Int>? {
        if (s.length == 1 && s == "0")
            return Pair(null, 0)
        if (s.length <= 3 && getColorFromChar(s[0]) != null && s.substring(1, s.length).toIntOrNull() != null)
            return Pair(getColorFromChar(s[0]), s.substring(1, s.length).toInt())
        return null
    }

    private fun getColorFromChar(letter: Char) : Color? {
        return Color.values().firstOrNull{ it.letter.lowercaseChar() == letter.lowercaseChar()}
    }

    //------------------------------------------------------------------------------------------------------------------

    private fun roleDice() = 1 + random.nextInt(6)
    fun roleDices() {
        if (diceValues.size == 2) {
            colorToMove = colorToMove.opponent()
        }
        movePartStack.clear()

        val dices = listOf (roleDice(), roleDice())

        diceValues = if (dices[0] == dices[1]) {
            listOf(dices[0], dices[0], 7-dices[0], 7-dices[0])
        } else if ( (dices[0] * dices[1] == 2)) {
            listOf(1,1,2,2,5,5,6,6)
        } else {
            dices.sorted()
        }

    }

    fun activeDiceIndex() = movePartStack.size

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
            return if (moveBuilder.isEmpty())
                emptyList()
            else
                listOf(Move(movePartList = moveBuilder, usedAllDice = true))
        }

        val possibleMoveParts = getPossibleMoveParts(diceList.first())
        if (possibleMoveParts.isEmpty()) {
            return if (moveBuilder.isEmpty())
                emptyList()
            else
                listOf(Move(movePartList = moveBuilder, usedAllDice = false))
        }
        val allMoves = mutableListOf<Move>()
        for (movePart in possibleMoveParts) {
            doMovePart(movePart)
            allMoves += generateAllMoves(diceList.subList(fromIndex = 1, toIndex = diceList.size), moveBuilder.plus(movePart))
            undoMovePart()
        }
        return allMoves
    }


    private fun getPossibleMoveParts(dice: Int): List<MovePart> {
        val color = colorToMove
        if (bar.hasCheckers(color)) {
            val pointIndex = if (color == Color.WHITE) dice-1 else (POINTS_PER_BOARD - dice)
            return if (pointList[pointIndex].isPlayableFor(color))
                listOf(MovePart(color, bar, pointList[pointIndex], pointList[pointIndex].isBlunt(color.opponent())))
            else
                emptyList()
        }

        val factor = if (color == Color.WHITE) 1 else -1
        val movePartList = pointList
            .asSequence()
            .filter { it.hasCheckers(color) }
            .filter { it.pointIndex + factor*dice < POINTS_PER_BOARD }
            .filter { it.pointIndex + factor*dice >= 0 }
            .filter { pointList[it.pointIndex+factor*dice].isPlayableFor(color) }
            .map { MovePart(color, it, pointList[it.pointIndex+factor*dice], pointList[it.pointIndex+factor*dice].isBlunt(color.opponent()) ) }
            .toList()

        val pointIndex = if (color == Color.WHITE) POINTS_PER_BOARD-dice else dice-1
        return if (bearedOff.isPlayableFor(color) && pointList[pointIndex].hasCheckers(color))
            movePartList.plus(MovePart(color, pointList[pointIndex], bearedOff))
        else
            movePartList
    }

    fun getMovePart(checkerContainerName: String): MovePart {
        val moves = generateAllMoves(diceValues.subList(activeDiceIndex(), diceValues.size))
        val list = moves.filter{ it.movePartList.first().from.name == checkerContainerName }
        return list.first().movePartList.first()
    }

    //------------------------------------------------------------------------------------------------------------------

    //todo: (un)doMovePart is independent of board. Should it be a board Method?

    private fun endOfMove() = (activeDiceIndex() >= diceValues.size || getPossibleMoveParts(diceValues[activeDiceIndex()]).isEmpty())

    fun doMovePart(movePart: MovePart) {
        movePart.from.removeChecker(movePart.color)
        movePart.to.addChecker(movePart.color)
        if (movePart.isCapture) {
            movePart.to.removeChecker(movePart.color.opponent())
            bar.addChecker(movePart.color.opponent())
        }
        movePartStack.push(movePart)
    }

    fun undoMovePart() {
        val movePart = movePartStack.pop()
        movePart.to.removeChecker(movePart.color)
        movePart.from.addChecker(movePart.color)
        if (movePart.isCapture) {
            movePart.to.addChecker(movePart.color.opponent())
            bar.removeChecker(movePart.color.opponent())
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    override fun toString(): String {
        return "$bar|${pointListToString()}|$bearedOff" +
                "@${colorToMove.letter}" +
                "@${diceListToString()}" +
                "@${movePartStackToString()}"
    }

    private fun pointListToString(): String {
        var s = ""
        for (quadrant in 0 until 4) {
            val subList = pointList.subList(quadrant* POINTS_PER_QUADRANT, (quadrant+1)* POINTS_PER_QUADRANT)
            if (quadrant != 0)
                s += "|"
            s += subList.joinToString ("-"){ it.toString() }
        }
        return s
    }

    private fun movePartStackToString(): String {
        return movePartStack.joinToString ("-"){ it.toShortString() }
    }

    private fun diceListToString(): String {
        return diceValues.joinToString ("-"){ it.toString() }
    }

}