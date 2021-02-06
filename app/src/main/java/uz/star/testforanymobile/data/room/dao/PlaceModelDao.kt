package uz.star.testforanymobile.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.star.testforanymobile.data.room.entity.PlaceModel

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

@Dao
interface PlaceModelDao : BaseDao<PlaceModel> {
    @Query("SELECT * FROM placemodel")
    fun getAllPlaceModels(): List<PlaceModel>
}