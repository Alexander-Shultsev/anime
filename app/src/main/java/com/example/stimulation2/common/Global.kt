package com.example.stimulation2.common

class Global {
    companion object {

        /* Данные авторизированного пользователя */
        var token = 0
        var type_user = ""

        /* id пользователя, которого выбирает менеджер для присвоения ему достижения */
        var id_change_user = 0

        /* Данные для API */
        val url = "http://a0616994.xsph.ru/"

        /* Список страниц для запросов */
        val pages = arrayOf (
            arrayOf("user_data", true), // название php страницы, есть ли в запросе post
            arrayOf("type_user", true),
            arrayOf("all_user", false),
            arrayOf("reg", true),
            arrayOf("ucmch", true),
            arrayOf("uptomanager", true),
            arrayOf("bonuses", false),
            arrayOf("trade", true)
        )
    }
}