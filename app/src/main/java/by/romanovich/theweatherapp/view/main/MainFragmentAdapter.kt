package by.romanovich.theweatherapp.view.main
//посмотреть ещё лекцию 3 на 3.31мин
//посмотреть ещё лекцию 3 на 4.07мин
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.romanovich.theweatherapp.databinding.FragmentMainRecyclerCityItemBinding
import by.romanovich.theweatherapp.model.Weather


//MainFragmentAdapter это адаптер для RecyclerView, умеющий принимать клики
class MainFragmentAdapter(val listener: OnMyItemClickListener) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

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
    ): MainFragmentAdapter.MainViewHolder {
        val binding: FragmentMainRecyclerCityItemBinding =
            FragmentMainRecyclerCityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        //возращаем новый MainViewHolder
        return MainViewHolder(binding.root)
    }

    //если список не влазит на экран переписываем список новыми данными, поэтому используем RecyclerView а не ЛистВью
    override fun onBindViewHolder(holder: MainFragmentAdapter.MainViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    //item будет в размер нашего списка
    override fun getItemCount(): Int {
        return weatherData.size
    }


    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            with(FragmentMainRecyclerCityItemBinding.bind(itemView)) {
                mainFragmentRecyclerItemTextView.text = weather.city.name
                root.setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
            FragmentMainRecyclerCityItemBinding.bind(itemView).run {
                mainFragmentRecyclerItemTextView.text = weather.city.name
                root.setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
        }
    }
}