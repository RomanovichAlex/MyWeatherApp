package by.romanovich.theweatherapp.model

//реализация
class RepositoryImpl:Repository {
//было
    //override fun getWeatherFromServer(): Weather {
        //return Weather() }
    //стало:
    override fun getWeatherFromServer() = Weather()

// методы которые возращают погоду
    // было override fun getWeatherFromLocalStorageRus(): List<Weather> {
       // return getRussianCities()}
    //стало:
    override fun getWeatherFromLocalStorageRus()=getRussianCities()

//было
    //override fun getWeatherFromLocalStorageWorld(): List<Weather> {
       // return getWorldCities()}
    //Стало:
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}