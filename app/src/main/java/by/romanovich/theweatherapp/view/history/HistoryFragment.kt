package by.romanovich.theweatherapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.romanovich.theweatherapp.databinding.FragmentHistoryBinding
import by.romanovich.theweatherapp.model.Weather
import by.romanovich.theweatherapp.view.main.OnMyItemClickListener
import by.romanovich.theweatherapp.viewmodel.AppState
import by.romanovich.theweatherapp.viewmodel.HistoryViewModel
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment(), OnMyItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() {
            return _binding!!
        }

    private val adapter: CitiesHistoryAdapter by lazy {
        CitiesHistoryAdapter(this)
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // обращаем внимание
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { renderData(it) })
        //вернуть всю историю
        viewModel.getAllHistory()
        binding.historyFragmentRecyclerview.adapter = adapter
    }



    private fun renderData(appState: AppState) {
        with(binding) {
            when (appState) {
                is AppState.Error -> {
                    // TODO HW
                }
                is AppState.Loading -> {}
                is AppState.Success -> {
                    adapter.setWeather(appState.weatherData)
                }
            }
        }
    }

    fun View.showSnackBarWithoutAction(text:String, length:Int){
        Snackbar.make(this,text,length).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onItemClick(weather: Weather) {
        // можете что-то свое добавить
    }
}