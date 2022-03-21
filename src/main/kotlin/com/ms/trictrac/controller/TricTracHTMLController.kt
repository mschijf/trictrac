package com.ms.trictrac.controller

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Template
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import com.github.jknack.handlebars.io.TemplateLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TricTracHTMLController {

    @GetMapping("/")
    fun index(): String {

        val loader: TemplateLoader = ClassPathTemplateLoader("/templates", ".hbs")
        val handlebars = Handlebars(loader)
        val template: Template = handlebars.compile("trictrac")
        return template.apply(null)
    }

    fun readFileUsingGetResource(fileName: String): String {
        val resource = this::class.java.getResource(fileName) ?: return "Cannot find file: '$fileName'"
        return resource.readText(Charsets.UTF_8)
    }
}


