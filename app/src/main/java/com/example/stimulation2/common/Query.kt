package com.example.stimulation2.common

import com.example.stimulation2.data.*
import okhttp3.*
import com.google.gson.GsonBuilder


class Query() {

    private var post = false
    private lateinit var request: Request

    // Формирование запроса
    fun getRequest(page: String, formBody: FormBody = FormBody.Builder().build()): Request {

        /* Нахождение значений post и select из Global.pages, сопоставимых с полученной страницей */
        for (i in Global.pages) {
            if (i[0] == page) {
                post = i[1].toString().toBoolean()
            }
        }

        request =
            if (post) requestPost(formBody, page)
            else requestNotPost(page)

        return request
    }

    private fun requestPost(formBody: FormBody, page: String): Request {
        return Request.Builder().url(Global.url + "/" + page + ".php").post(formBody).build()
    }

    private fun requestNotPost(page: String): Request {
        return Request.Builder().url(Global.url + "/" + page + ".php").build()
    }


    private var gson = GsonBuilder().create()

    // Парсинг json тип пользователя
    fun parseJsonTypeUser(json: String) = gson.fromJson(json, arrayOf<TokenAndTypeUser>().javaClass)!!

    // Парсинг json данные пользователя
    fun parseJsonUserData(json: String) = gson.fromJson(json, arrayOf<AuthorizationUser>().javaClass)!!

    // Парсинг json данные всех пользователей
    fun parseJsonUses(json: String) = gson.fromJson(json, arrayOf<ListUser>().javaClass)!!

    // Парсинг json рейтинг
    fun parseJsonRating(json: String) = gson.fromJson(json, arrayOf<Rating>().javaClass)!!

    // Парсинг json новое кол-во корон и монет после изменения
    fun parseJsonCrownAndMoney(json: String) = gson.fromJson(json, CrownAndMoney::class.java)!!

    // Парсинг json бонусы
    fun parseJsonBonus(json: String) = gson.fromJson(json, arrayOf<Bonus>().javaClass)!!
}
