package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.model.WeatherDTO
//получает детализированную погоду с сервера
interface RepositoryDetails {
    //url запрос, callback ответ
    fun getWeatherFromServer(lat:Double,lon:Double,callback: retrofit2.Callback<WeatherDTO>)
}