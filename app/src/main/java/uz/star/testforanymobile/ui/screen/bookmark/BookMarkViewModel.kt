package uz.star.testforanymobile.ui.screen.bookmark

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.utils.addSourceDisposable

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

class BookMarkViewModel @ViewModelInject constructor(
    private val repository: BookMarkRepository
) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _places = MediatorLiveData<List<PlaceModel>>()
    val places: LiveData<List<PlaceModel>> get() = _places

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> get() = _delete

    fun getSavePlaces() {
        _places.addSourceDisposable(repository.getAllPlaces()) { resultData ->
            resultData.onData { list ->
                if (list.isEmpty()) _message.value = "Список сейчас пуст"
                _places.value = list
            }.onMessage { message ->
                _message.value = message
            }
        }
    }

    fun deletePlaceMark(placeModel: PlaceModel) {
        _places.addSourceDisposable(repository.deletePlace(placeModel)) { resultData ->
            resultData.onData { status ->
                _delete.value = status
                getSavePlaces()
            }.onMessage { message ->
                _message.value = message
            }

        }
    }
}