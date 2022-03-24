package com.ms.trictrac.controller

import com.ms.trictrac.game.Board
import com.ms.trictrac.game.DEFAULT_BOARD_STRING
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

const val BOARD_COOKIE = "TRICTRACBOARD"
const val REQUESTPATH_BASE = "/trictrac/api/v1"

@RestController
@RequestMapping(REQUESTPATH_BASE)
class RestController @Autowired constructor(private val gameService: GameService) {

    @GetMapping("/board/")
    fun getBoard(httpServletResponse: HttpServletResponse,
                 @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD_STRING) boardString: String): BoardModel {
//        val (model, _) = gameService.getBoard(boardString)
        val (model, persistanceString) = gameService.getBoard(boardString)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @PostMapping("/move/{name}")
    fun doMove(httpServletResponse: HttpServletResponse,
               @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD_STRING) boardString: String,
               @PathVariable(name = "name") name: String): BoardModel {
        val (model, persistanceString) =  gameService.doMovePart(boardString, name)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @PostMapping("/undoMove/")
    fun undoMove(httpServletResponse: HttpServletResponse,
               @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD_STRING) boardString: String): BoardModel {
        val (model, persistanceString) =  gameService.undoMovePart(boardString)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    @GetMapping("/dices/")
    fun getDices(httpServletResponse: HttpServletResponse,
                 @CookieValue(value = BOARD_COOKIE, defaultValue = DEFAULT_BOARD_STRING) boardString: String): BoardModel {
        val (model, persistanceString) = gameService.getDices(boardString)
        httpServletResponse.addCookie(getNewCookie(persistanceString))
        return model
    }

    private fun getNewCookie(persistanceString: String): Cookie {
        val cookie = Cookie(BOARD_COOKIE, persistanceString)
        cookie.maxAge = 3600*24*365
        cookie.path = "/"
        return cookie
    }

}


