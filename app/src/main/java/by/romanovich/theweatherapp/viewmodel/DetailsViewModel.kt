package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.model.getDefaultCity
import by.romanovich.theweatherapp.repository.RepositoryLocalImpl
import by.romanovich.theweatherapp.repository.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    //создали контейнер liveData с состоянием приложения AppState и на этот контейнер подписалась фрагмент(Вьюшка)
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),

    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl()
    ) : ViewModel() {



    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    fun getLiveData() = liveData


    //спасли базу данных от работы в главном потоке
    fun saveWeather(weather: Weather){
        Thread {
            repositoryLocalImpl.saveWeather(weather)
        }.start()
    }


    fun getWeatherFromRemoteServer(lat:Double,lon:Double) {
        liveData.postValue(AppState.Loading(0))
        //ретрофит работает по принципу дай мне поля запроса
        repositoryRemoteImpl.getWeatherFromServer(lat,lon, callback)
    }


    //конверттер с везердто в везер
    fun converterDTOtoModel(weatherDTO: WeatherDTO):List<Weather>{
        return listOf(Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt(),weatherDTO.fact.icon))
    }

    private val callback = object : Callback<WeatherDTO> {
       //когда не достучался до сервера
       override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
           liveData.postValue(AppState.Error(R.string.errorOnServer.toString(),0))
       }
       //ответ сервера
       override fun onResponse(call:Call<WeatherDTO>, response: Response<WeatherDTO>) {
           if (response.isSuccessful) {
               //если ответ бади не нулл то мы его переводим в строку и парсим в везер дто
               response.body()?.let {
                   liveData.postValue(
                       AppState.Success(
                           converterDTOtoModel(it)))
               }
           } else {
               liveData.postValue(AppState.Error(R.string.ServerError.toString(),response.code()))
           }
       }
    }
}
