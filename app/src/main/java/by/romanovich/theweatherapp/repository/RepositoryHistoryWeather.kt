package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.model.Weather


interface RepositoryHistoryWeather {
    //получение погоды, возращает список веазер
    fun getAllHistoryWeather():List<Weather>
    //сохраняем погоду только входящие
    fun saveWeather(weather: Weather)
}