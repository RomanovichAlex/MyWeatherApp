package by.romanovich.theweatherapp.model

//реализация
class RepositoryImpl:Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
// методы которые возращают погоду
    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }


}