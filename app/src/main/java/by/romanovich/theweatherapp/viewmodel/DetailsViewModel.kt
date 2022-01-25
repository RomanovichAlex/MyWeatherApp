package by.romanovich.theweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.model.WeatherDTO
import by.romanovich.theweatherapp.model.getDefaultCity
import by.romanovich.theweatherapp.repository.RepositoryImpl
import by.romanovich.theweatherapp.utils.YANDEX_API_URL
import by.romanovich.theweatherapp.utils.YANDEX_API_URL_END_POINT
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

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
        repositoryImpl.getWeatherFromServer(YANDEX_API_URL + YANDEX_API_URL_END_POINT +"?lat=${
           lat}&lon=${lon}",callback)
    }


    //конверттер с везердто в везер
    fun converterDTOtoModel(weatherDTO: WeatherDTO):List<Weather>{
        return listOf(Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt()))
    }

    private val callback = object : Callback {
       //когда не достучался до сервера
       override fun onFailure(call: Call, e: IOException) {
           TODO("Not yet implemented")
       }
       //ответ сервера
       override fun onResponse(call: Call, response: Response) {
           if (response.isSuccessful) {
               //если ответ бади не нулл то мы его переводим в строку и парсим в везер дто
               response.body()?.let {
                   //выполняем сет везер в главном потоке
                   val json = it.string()
                   liveData.postValue(
                       AppState.Success(
                           converterDTOtoModel(
                               Gson().fromJson(
                                   json,
                                   WeatherDTO::class.java
                               )
                           )
                       )
                   )

               }
           } else {
               // TODO HW
           }
       }
   }
}
