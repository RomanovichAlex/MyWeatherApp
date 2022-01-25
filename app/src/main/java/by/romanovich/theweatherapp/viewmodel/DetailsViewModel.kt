package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.model.getDefaultCity
import by.romanovich.theweatherapp.repository.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    //создали контейнер liveData с состоянием приложения AppState и на этот контейнер подписалась фрагмент(Вьюшка)
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    ) : ViewModel() {
    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }

    fun getLiveData() = liveData


    fun getWeatherFromRemoteServer(lat:Double,lon:Double) {
        liveData.postValue(AppState.Loading(0))
        //ретрофит работает по принципу дай мне поля запроса
        repositoryImpl.getWeatherFromServer(lat,lon, callback)
    }


    //конверттер с везердто в везер
    fun converterDTOtoModel(weatherDTO: WeatherDTO):List<Weather>{
        return listOf(Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt(),weatherDTO.fact.icon))
    }

    private val callback = object : Callback<WeatherDTO> {
       //когда не достучался до сервера
       override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
           //TODO hw ("Not yet implemented")
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
               // TODO HW
           }
       }
    }
}
