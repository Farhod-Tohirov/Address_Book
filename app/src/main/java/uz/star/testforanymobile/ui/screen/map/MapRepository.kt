package uz.star.testforanymobile.ui.screen.map

import androidx.lifecycle.LiveData
import uz.star.testforanymobile.data.room.entity.PlaceModel

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

interface MapRepository {

    fun insertPlaceToDB(placeModel: PlaceModel): LiveData<Boolean>

}