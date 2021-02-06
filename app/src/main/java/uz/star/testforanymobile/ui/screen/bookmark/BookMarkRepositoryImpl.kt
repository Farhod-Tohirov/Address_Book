package uz.star.testforanymobile.ui.screen.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.model.results.local.ResultData
import uz.star.testforanymobile.data.room.dao.PlaceModelDao
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.utils.Coroutines
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

class BookMarkRepositoryImpl @Inject constructor(
    private val placeModelDao: PlaceModelDao
) : BookMarkRepository {

    override fun getAllPlaces(): LiveData<ResultData<List<PlaceModel>>> {
        val resultLiveData = MutableLiveData<ResultData<List<PlaceModel>>>()

        Coroutines.ioThenMain(
            { placeModelDao.getAllPlaceModels() },
            { list ->
                if (list != null)
                    if (list.isNotEmpty())
                        resultLiveData.value = ResultData.data(list)
                    else {
                        resultLiveData.value = ResultData.data(listOf())
                    }
                else
                    resultLiveData.value = ResultData.message("непредвиденная ошибка")
            }
        )
        return resultLiveData
    }

    override fun deletePlace(placeModel: PlaceModel): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { placeModelDao.delete(placeModel) },
            { status ->
                if (status != null)
                    if (status > 0)
                        resultLiveData.value = ResultData.data(true)
                    else
                        resultLiveData.value = ResultData.data(false)
                else
                    resultLiveData.value = ResultData.message("непредвиденная ошибка")
            }
        )
        return resultLiveData
    }
}