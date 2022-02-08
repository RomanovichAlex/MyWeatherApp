package by.romanovich.theweatherapp.repository

import by.romanovich.theweatherapp.model.City
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.getRussianCities
import by.romanovich.theweatherapp.model.getWorldCities
import by.romanovich.theweatherapp.room.App
import by.romanovich.theweatherapp.room.HistoryWeatherEntity


//реализация
class RepositoryLocalImpl : RepositoryCitiesList, RepositoryHistoryWeather {

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()


    override fun getAllHistoryWeather(): List<Weather> {
        //переводим таблицы в везер
        return convertHistoryWeatherEntityToWeather(
            //репозиторий запрашивает из БД либо список всех таблиц, либо вставляет новую
            App.getHistoryWeatherDao().getAllHistoryWeather()
        )
    }

    override fun saveWeather(weather: Weather) {
        Thread {
            App.getHistoryWeatherDao().insert(
                convertWeatherToHistoryWeatherEntity(weather)
            )
        }.start()
    }


    //конвертер на вход из таблицы на выход список погоды
    private fun convertHistoryWeatherEntityToWeather(entityList: List<HistoryWeatherEntity>): List<Weather> {
        /*val newListWeather: MutableList<Weather> = mutableListOf()//как делали бы в джава по старинке
        entityList.forEach {
            newListWeather.add(
                Weather(
                    City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon
                )
            )
        }
        return newListWeather*/


//заменили в entityList списки на Weather и вернули entityList
        return entityList.map {
            Weather(
                City(it.name, 0.0, 0.0), it.temperature, it.feelsLike, it.icon
            )
        }
    }


    //для рботы роом и везер
    private fun convertWeatherToHistoryWeatherEntity(weather: Weather) =
        HistoryWeatherEntity(
            0,
            weather.city.name,
            weather.temperature,
            weather.feelsLike,
            weather.icon
        )

    /*fun delete(weather: Weather) {
        App.getHistoryWeatherDao().delete(
            convertWeatherToHistoryWeatherEntity(weather)
        )
    }*/
}