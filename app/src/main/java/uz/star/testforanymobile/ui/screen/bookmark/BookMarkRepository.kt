package uz.star.testforanymobile.ui.screen.bookmark

import androidx.lifecycle.LiveData
import uz.star.mardex.model.results.local.ResultData
import uz.star.testforanymobile.data.room.entity.PlaceModel

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

interface BookMarkRepository {
    fun getAllPlaces(): LiveData<ResultData<List<PlaceModel>>>
    fun deletePlace(placeModel: PlaceModel): LiveData<ResultData<Boolean>>
}