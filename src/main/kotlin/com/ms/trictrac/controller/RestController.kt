package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import com.ms.trictrac.game.DEFAULT_BOARD_STRING
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val BOARD_COOKIE = "TRICTRACBOARD"
const val REQUESTPATH_BASE = "/trictrac/api/v1"

@RestController
@RequestMapping(REQUESTPATH_BASE)
class RestController @Autowired constructor(private val gameService: GameService) {

    @GetMapping("/board/")
    fun getBoard(@CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD_STRING) boardString: String): BoardModel {
        val (model, _) = gameService.getBoard(boardString)
        return model
    }

}


