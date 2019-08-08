package fr.airweb.news.interfaces

import fr.airweb.news.models.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface AirWeb_Client
{
    @GET("/psg/psg.json")
    fun getNews(): Call<String>
}