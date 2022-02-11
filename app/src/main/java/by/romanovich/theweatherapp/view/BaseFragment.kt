package by.romanovich.theweatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


//на вход приходит джинерик ограниченный ViewBinding
abstract class BaseFragment<T : ViewBinding>(
    private val inflateBinding: (inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> T
) : Fragment() {


    //что бы бороться с утечкой памяти
    private var _binding: T? = null
    //ловим какой то фрагмент
    val binding: T
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding.invoke(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}