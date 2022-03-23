package com.ms.trictrac.game

const val DEFAULT_BOARD_STRING = ""
class Board(boardString: String = "") {

    //todo: diceToBePlayed
    //todo: movePartsPlayed
    //todo: movePart stored with indexes instead of objects??

    constructor(board: Board) : this(board.toString())

    val pointList = buildList(POINTS_PER_BOARD) {
        for (index in 0 until POINTS_PER_BOARD)
            add(Point(index, this@Board))
    }
    val bar = Bar(POINTS_PER_BOARD, this)
    val bearedOff = BearedOff(POINTS_PER_BOARD+1, this)
    val colorToMove: Color

    init {
        if (boardString.isNotBlank()) {
            //format: w0-b9|w3-b2-w1-w4-b2-0|0-0-0-0-0-0|0-0-0-0-0-0|0-0-0-0-0-0|w7-b2@w
            val split = boardString.split('@')
            if (split.size > 2)
                throw Exception("wrong format input String for board. Too many '@'")

            colorToMove = if (split.size == 2) determineColorToMove(split[1]) else Color.WHITE
            val pairList = transformInputToIntList(split[0])

            if (pairList.isEmpty())
                throw Exception("wrong format input String for board")

            bar.initCheckers(pairList[0].first!!, pairList[0].second)
            bar.initCheckers(pairList[1].first!!, pairList[1].second)

            for (i in 2 until POINTS_PER_BOARD+2) {
                if (pairList[i].first != null)
                    pointList[i-2].initCheckers(pairList[i].first!!, pairList[i].second)
            }

            bearedOff.initCheckers(pairList[POINTS_PER_BOARD+2].first!!, pairList[POINTS_PER_BOARD+2].second)
            bearedOff.initCheckers(pairList[POINTS_PER_BOARD+3].first!!, pairList[POINTS_PER_BOARD+3].second)
        } else {
            colorToMove = Color.WHITE
            bar.initCheckers(Color.WHITE, 15)
            bar.initCheckers(Color.BLACK, 15)
            pointList.forEach { it.initCheckers(Color.WHITE, 0); it.initCheckers(Color.BLACK, 0) }
            bearedOff.initCheckers(Color.WHITE, 0)
            bearedOff.initCheckers(Color.BLACK, 0)
        }
    }

    private fun determineColorToMove(s: String): Color {
        if (s.length != 1)
            throw Exception("Unexpected serie of characters after '@'")
        if (s[0].uppercaseChar() == 'W')
            return Color.WHITE
        if (s[0].uppercaseChar() == 'B')
            return Color.BLACK
        throw Exception("Unexpected character after '@'")
    }

    private fun transformInputToIntList(inputString: String): List<Pair<Color?, Int>> {
        val parts = inputString.split('|','-').filter { it.isNotBlank() }
        if (parts.size != POINTS_PER_BOARD+4)
            return emptyList()

        val allPairs = parts.filter { it.isNotBlank() }.mapNotNull{toPair(it)}
        if (allPairs.size != POINTS_PER_BOARD + 4)
            return emptyList()
        if (allPairs.filter { it.first == Color.WHITE }.sumOf { it.second } != MAX_CHECKERS_PER_GAME)
            return emptyList()
        if (allPairs.filter { it.first == Color.BLACK }.sumOf { it.second } != MAX_CHECKERS_PER_GAME)
            return emptyList()
        if (allPairs.subList(2, POINTS_PER_BOARD+2).any{ it.second > MAX_CHECKERS_PER_POINT})
            return emptyList()

        if (allPairs[0].first == allPairs[1].first || allPairs[0].first == null || allPairs[1].first == null)
            return emptyList()
        if (allPairs[POINTS_PER_BOARD+2].first == allPairs[POINTS_PER_BOARD+3].first || allPairs[POINTS_PER_BOARD+2].first == null || allPairs[POINTS_PER_BOARD+3].first == null)
            return emptyList()

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

    fun generateMoves(color: Color, diceList: List<Int>): List<Move> {
        val moveList = generateAllMoves(color, diceList)
        return if (moveList.none { it.usedAllDice }) {
            moveList.distinctBy { it.hashValue }
        } else {
            moveList.filter {it.usedAllDice}.distinctBy { it.hashValue }
        }
    }

    fun generateAllMoves(color: Color, diceList: List<Int>, moveBuilder: List<MovePart> = emptyList()): List<Move> {
        if (diceList.isEmpty()) {
            return listOf(Move(movePartList = moveBuilder, usedAllDice = true))
        }

        val possibleMoveParts = getPossibleMoveParts(color, diceList.first())
        if (possibleMoveParts.isEmpty()) {
            return listOf(Move(movePartList = moveBuilder, usedAllDice = false))
        }
        val allMoves = mutableListOf<Move>()
        for (movePart in possibleMoveParts) {
            doMovePart(movePart)
            allMoves += generateAllMoves(color, diceList.subList(fromIndex = 1, toIndex = diceList.size), moveBuilder.plus(movePart))
            undoMovePart(movePart)
        }
        return allMoves
    }

    private fun getPossibleMoveParts(color: Color, dice: Int): List<MovePart> {
        if (bar.hasCheckers(color)) {
            return if (pointList[dice-1].isPlayableFor(color))
                listOf(MovePart(color, bar, pointList[dice-1], pointList[dice-1].isBlunt(color.opponent())))
            else
                emptyList()
        }

        val movePartList = pointList
            .filter { it.hasCheckers(color) }
            .filter { it.pointIndex + dice < POINTS_PER_BOARD }
            .filter { pointList[it.pointIndex+dice].isPlayableFor(color) }
            .map { MovePart(color, it, pointList[it.pointIndex+dice], pointList[it.pointIndex+dice].isBlunt(color.opponent()) ) }

        return if (bearedOff.isPlayableFor(color) && pointList[POINTS_PER_BOARD-dice].hasCheckers(color))
            movePartList.plus(MovePart(color, pointList[POINTS_PER_BOARD-dice], bearedOff))
        else
            movePartList
    }

    fun getMovePart(checkerContainerName: String): MovePart {
        val moves = generateMoves(Color.WHITE, listOf(2, 3))
        val s = moves.map{ it.movePartList.first().from.name }
        val x = s.first{ it == checkerContainerName}
        val list = moves.filter{ it.movePartList.first().from.name == checkerContainerName }
        return list.first().movePartList.first()
    }

    //------------------------------------------------------------------------------------------------------------------

    //todo: (un)doMovePart is independent of board. Should it be a board Method?

    fun doMovePart(movePart: MovePart) {
        movePart.from.removeChecker(movePart.color)
        movePart.to.addChecker(movePart.color)
        if (movePart.isCapture) {
            movePart.to.removeChecker(movePart.color.opponent())
            bar.addChecker(movePart.color.opponent())
        }
    }

    fun undoMovePart(movePart: MovePart) {
        movePart.to.removeChecker(movePart.color)
        movePart.from.addChecker(movePart.color)
        if (movePart.isCapture) {
            movePart.to.addChecker(movePart.color.opponent())
            bar.removeChecker(movePart.color.opponent())
        }
    }

    fun doMove(move: Move) {
        move.movePartList.forEach { doMovePart(it) }
    }

    //------------------------------------------------------------------------------------------------------------------

    override fun toString(): String {
        return "$bar|${pointListToString()}|$bearedOff@${colorToMove.letter}"
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

}