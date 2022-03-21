package com.ms.trictrac.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val REQUESTPATH_BASE = "/trictrac/api/v1"

@RestController
@RequestMapping(REQUESTPATH_BASE)
class RestController {

    @GetMapping("/board/")
    fun getBoard(): String {
        return "Hallo Wereld"
    }

}


