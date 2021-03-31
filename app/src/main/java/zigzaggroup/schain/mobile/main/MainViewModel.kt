package zigzaggroup.schain.mobile.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import zigzaggroup.schain.mobile.data.models.Item
import zigzaggroup.schain.mobile.data.models.ItemHistory
import zigzaggroup.schain.mobile.ui.NoConnectivityException
import zigzaggroup.schain.mobile.utils.DispatcherProvider
import zigzaggroup.schain.mobile.utils.Resource
import zigzaggroup.schain.mobile.utils.toast
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : AndroidViewModel(application) {

    sealed class Event<out T> {
        class Success<out T>(val item: T) : Event<T>()
        class Failure<out T>(val errorText: String) : Event<T>()
        object Loading : Event<Nothing>()
        object Empty : Event<Nothing>()
    }

    private val _item = MutableStateFlow<Event<Item>>(Event.Empty)
    val item: StateFlow<Event<Item>> = _item

    private val _itemHistory = MutableStateFlow<Event<ItemHistory>>(Event.Empty)
    val history: StateFlow<Event<ItemHistory>> = _itemHistory

    private fun <T> callApi(
        toReturn: MutableStateFlow<Event<T>>,
        id: String,
        func: suspend (String) -> Resource<T>
    ) {
        viewModelScope.launch(dispatchers.io) {
            try {
                toReturn.value = Event.Loading
                when (val response = func(id)) {
                    is Resource.Error<*> -> {
                        toReturn.value = Event.Failure(response.message!!)
                    }
                    is Resource.Success<*> -> {
                        val item = response.data
                        toReturn.value = Event.Success(item!!)
                    }
                }
            } catch (e: Exception) {
                if (e is NoConnectivityException) {
                    getApplication<Application>().applicationContext.toast(e.message)
                }
                toReturn.value = Event.Failure("Error occurred: " + e.message)

            }
        }
    }

    fun getItem(id: String) {
        callApi(_item, id, repository::getItem)
    }

    fun getHistory(id: String) {
        callApi(_itemHistory, id, repository::getItemHistory)
    }
}