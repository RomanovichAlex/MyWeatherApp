package by.romanovich.theweatherapp.lesson10

import android.graphics.Color
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.databinding.FragmentGoogleMapsMainBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding: FragmentGoogleMapsMainBinding
        get() {
            return _binding!!
        }



    //ссылка на нашу карту
    private lateinit var map: GoogleMap
    //список маркеров на карте
    private val markers = arrayListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val minsk = LatLng(53.913682, 27.676429)
        //googleMap.addMarker(MarkerOptions().position(m).title("Marker in Minsk"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(minsk))
        //по клику на карту вешаем маркеры
        googleMap.setOnMapLongClickListener {
            //по широте и долготе получаем адресс
            getAddress(it)
            addMarker(it)
            drawLine()
        }

        //googleMap.isMyLocationEnabled = true // TODO HW проверить есть ли разрешение на геолокацию
        googleMap.uiSettings.isZoomControlsEnabled = true


    }


    //рисуем линию
    private fun drawLine() {
        //получаем последний индекс массива
        val last = markers.size
        //если есть хотя бы два флажка
        if (last > 1) {
            //нарисовать поле лайн между последней и предпоследней позицией маркера
            map.addPolyline(
                PolylineOptions().add(markers[last-1].position, markers[last - 2].position)
                    //задаем цвет
                    .color(Color.RED)
                        //и тощину в пикселях
                    .width(5f)
            )
        }
    }


    //добавляем маркер по координатам
    private fun addMarker(location: LatLng) {
        //создаем маркер
        val marker = map.addMarker(
            //на позицию
            MarkerOptions().position(location)
                    //ставим иконку
                .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
        )
        markers.add(marker!!)
    }


    private fun getAddress(location: LatLng) {
        Log.d("", " $location")
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            requireActivity().runOnUiThread {
                //получаем адресс асинхронно
                binding.textAddress.text = listAddress[0].getAddressLine(0)
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            search()
        }
    }

    //по нажатию на кнопку
    private fun search(){
        Thread {
            val geocoder = Geocoder(requireContext())
            //вызываем адрес по имени
            val listAddress = geocoder.getFromLocationName(binding.searchAddress.text.toString(),1)
            requireActivity().runOnUiThread {
                //по имени получаем адрес из адреса достаем широту и долготу
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(listAddress[0].latitude,listAddress[0].longitude),10f))
                map.addMarker(MarkerOptions().position(LatLng(listAddress[0].latitude,listAddress[0].longitude)).title("") .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)))
                //addMarker(LatLng(listAddress[0].latitude,listAddress[0].longitude))
            }
        }.start()
    }
}


//SHA1: 30:67:C2:ED:30:F5:F6:4A:98:72:1F:4B:49:81:27:DD:A1:9B:53:23
//SHA-256: 02:4E:9F:00:CA:0C:BA:B7:96:A7:DC:7F:37:DC:B9:41:D1:6E:D2:34:5E:21:71:FA:18:73:C0:7D:09:13:36:6B