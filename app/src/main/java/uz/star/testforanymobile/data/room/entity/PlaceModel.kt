package uz.star.testforanymobile.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.mapkit.geometry.Point

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

@Entity
data class PlaceModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var subtitle: String,
    var distance: String,
    var allReview: Int?,
    var score: Float?,
    var longitude: Double,
    var latitude: Double
)