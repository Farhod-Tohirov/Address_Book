package uz.star.testforanymobile.ui.screen.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.utils.addSourceDisposable

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

class MapViewModel @ViewModelInject constructor(private val repository: MapRepository) :
    ViewModel() {

    private val _resultLiveData = MediatorLiveData<Boolean>()
    val resultLiveData: LiveData<Boolean> get() = _resultLiveData

    fun addPlaceModelToDB(placeModel: PlaceModel) {
        _resultLiveData.addSourceDisposable(repository.insertPlaceToDB(placeModel)) { isAdded ->
            _resultLiveData.value = isAdded
        }
    }

}