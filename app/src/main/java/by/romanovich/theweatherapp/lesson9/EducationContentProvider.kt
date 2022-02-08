package by.romanovich.theweatherapp.lesson9

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import by.romanovich.theweatherapp.R
import by.romanovich.theweatherapp.room.App.Companion.getHistoryWeatherDao
import by.romanovich.theweatherapp.room.HistoryWeatherEntity
import by.romanovich.theweatherapp.room.ID
import by.romanovich.theweatherapp.room.NAME
import by.romanovich.theweatherapp.room.TEMPERATURE


private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH =
    "HistoryWeatherEntity" // Часть пути (будем определять путь до HistoryEntity


class EducationContentProvider : ContentProvider() {

    private var authorities: String? = null
    //инструмент позволяющий работать с адресом
    private lateinit var uriMatcher: UriMatcher

    private var entityContentType: String? = null
    private var entityContentItemType: String? = null

    private lateinit var contentUri: Uri

    //наш контент провайдер
    override fun onCreate(): Boolean {
        authorities = context?.resources?.getString(R.string.authorities)
        //инструмент который выдает куски нашей строки и анализирует
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        //возращаем всю таблицу
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        //строку по ид
        uriMatcher.addURI(authorities, "${ENTITY_PATH}/#", URI_ID)
        //курсор работает с таблицей
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        //курсор работает с элементом
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        //по какому адресу все находится
        contentUri = Uri.parse("content://${authorities}/${ENTITY_PATH}")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }


    //Запрос нашего контент провайдера на вставку вызывает функция инсёрт, передавая в нее набор
    // неких параметров, готовых на вставку
    override fun insert(uri: Uri, values: ContentValues?): Uri {
//ссылка на работу с нашими базами данных, пытаемся добавить нашу новую запись
        val historyWeatherDao = getHistoryWeatherDao()
        //делаем запрос на вставку валуес
        val entity = mapper(values)
        historyWeatherDao.insert(entity)
        // создали запрос, и эту запись
        val resultUri = ContentUris.withAppendedId(contentUri, entity.id)
        //вставка, обновляем
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri

    }

    //если валуес не пустое, то мы создаём новую хистори
    fun mapper(values: ContentValues?): HistoryWeatherEntity {
        values?.let {
            val id = values[ID] as Long
            val name = values[NAME] as String
            val temperature = values[TEMPERATURE] as Int
            return HistoryWeatherEntity(id, name, temperature)
        }
        return HistoryWeatherEntity()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}