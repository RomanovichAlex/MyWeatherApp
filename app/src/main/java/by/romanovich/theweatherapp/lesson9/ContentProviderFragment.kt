package by.romanovich.theweatherapp.lesson9

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.romanovich.theweatherapp.databinding.FragmentContentProviderBinding


class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }


    //спрашиваем есть ли у нас разрешение к контактам
    private fun checkPermission() {
        context?.let {
            when {
                //на вход идет контекст есть ли  разрешение
                ContextCompat.checkSelfPermission(it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                // нужна ли рацианализация(первый раз метод не сработает)
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                //если отказали
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    //запрашиваем пермишонс
    val REQUEST_CODE = 999
    private fun myRequestPermission(){
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {

            when {
                    //если результат для нашего первого пемишиона выдан
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    //отрисовываем контакты
                    getContacts()
                }
                //если не выдано запрашиваем рационализацию
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                //если отказали
                else -> {
                    Log.d("", "КОНЕЦ")
                }
            }
        }
    }


    private fun getContacts() {
        context?.let { it ->
            //contentResolver - это содержит в себе перечень контент провайдеров, обращается к базам данных
            val contentResolver = it.contentResolver
            //курсор на таблицу, запрос в базу данных к query таблице контактов
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                //сортировка по имени, по возрастанию. DESK по убыванию
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let { cursor->
                //от 0 до всего массива контактов
                for (i in 0 until cursor.count) {
                    //двигаем курсор с 0 и дальше
                    cursor.moveToPosition(i)
                    //получаем имя
                    //мы у курсора спрашиваем индекс для имени
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    addView(name)
                }
            }
            cursor?.close()
        }
    }

    private fun addView(name:String) {
        binding.containerForContacts.addView(TextView(requireContext()).apply {
            text = name
            textSize = 30f
        })
    }


    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Объяснение")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
    }
}