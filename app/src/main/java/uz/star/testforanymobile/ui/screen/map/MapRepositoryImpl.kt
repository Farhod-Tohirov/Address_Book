package uz.star.testforanymobile.ui.screen.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.testforanymobile.data.room.dao.PlaceModelDao
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.utils.Coroutines
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

class MapRepositoryImpl @Inject constructor(
    private val placeModelDao: PlaceModelDao
) : MapRepository {

    override fun insertPlaceToDB(placeModel: PlaceModel): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()
        Coroutines.ioThenMain(
            { placeModelDao.insert(placeModel) },
            { id ->
                if (id != null)
                    resultLiveData.value = id > 0
            }
        )
        return resultLiveData
    }
}