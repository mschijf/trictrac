package com.ms.trictrac.game

enum class Color(val letter: Char) {
    WHITE('w'){
        override fun opponent() = BLACK
    },
    BLACK('b'){
        override fun opponent() = WHITE
    };
    abstract fun opponent(): Color
}