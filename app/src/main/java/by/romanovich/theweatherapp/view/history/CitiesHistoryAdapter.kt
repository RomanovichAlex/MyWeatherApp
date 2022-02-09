package by.romanovich.theweatherapp.view.history
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.romanovich.theweatherapp.databinding.FragmentHistoryRecyclerCityItemBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.view.main.OnMyItemClickListener
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest


//MainFragmentAdapter это адаптер для RecyclerView, умеющий принимать клики
class CitiesHistoryAdapter(val listener: OnMyItemClickListener) :
    RecyclerView.Adapter<CitiesHistoryAdapter.HistoryCityViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    //когда меняется погода
    fun setWeather(data: List<Weather>) {
        this.weatherData = data
        //присвоить изменения
        notifyDataSetChanged()
    }

    //создаем список на весь экран
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesHistoryAdapter.HistoryCityViewHolder {
        val binding: FragmentHistoryRecyclerCityItemBinding =
            FragmentHistoryRecyclerCityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        //возращаем новый MainViewHolder
        return HistoryCityViewHolder(binding.root)
    }

    //если список не влазит на экран переписываем список новыми данными, поэтому используем RecyclerView а не ЛистВью
    override fun onBindViewHolder(holder: CitiesHistoryAdapter.HistoryCityViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    //item будет в размер нашего списка
    override fun getItemCount(): Int {
        return weatherData.size
    }


    inner class HistoryCityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            with(FragmentHistoryRecyclerCityItemBinding.bind(itemView)){
                cityName.text = weather.city.name
                temperature.text = "${weather.temperature}"
                feelsLike.text = "${weather.feelsLike}"
                icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                root.setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
        }

        private fun ImageView.loadUrl(url: String) {

            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
                .build()

            val request = ImageRequest.Builder(this.context).data(url).target(this).build()

            imageLoader.enqueue(request)
        }
    }
}