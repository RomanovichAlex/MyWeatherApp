package by.romanovich.theweatherapp.repository

import okhttp3.Callback
//получает детализированную погоду с сервера
interface RepositoryDetails {
    //url запрос, callback ответ
    fun getWeatherFromServer(url:String,callback: Callback)
}