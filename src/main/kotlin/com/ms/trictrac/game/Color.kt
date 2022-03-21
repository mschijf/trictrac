package com.ms.trictrac.game

enum class Color(val letter: Char) {
    WHITE('W'){
        override fun opponent() = Color.BLACK
    },
    BLACK('B'){
        override fun opponent() = Color.WHITE
    };
    abstract fun opponent(): Color
}