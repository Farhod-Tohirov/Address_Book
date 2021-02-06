package uz.star.testforanymobile.ui.screen.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import dagger.hilt.android.AndroidEntryPoint
import uz.star.testforanymobile.data.local.LocalStorage
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.databinding.FragmentMapBinding
import uz.star.testforanymobile.databinding.SaveDialogBinding
import uz.star.testforanymobile.ui.adapters.SearchResultAdapter
import uz.star.testforanymobile.ui.screen.MainActivity
import uz.star.testforanymobile.utils.*
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val searchResultAdapter = SearchResultAdapter()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: MapViewModel by viewModels()
    private val args: MapFragmentArgs by navArgs()

    @Inject
    lateinit var localStorage: LocalStorage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) {
                /**
                 * Below checks from where this fragment is called
                 * If It is called from Bookmark fragment args.placeModel can not be null
                 * If it is loading first time args.placeModel will be null
                 */
                if (args.placeModel != null) {
                    navigateToPoint(Point(args.placeModel!!.latitude, args.placeModel!!.longitude))
                    binding.searchSection.gone()
                    setPlaceData(args.placeModel!!, true)
                    loadViews(true)
                } else {
                    loadViews(false)
                }
            }
        }
    }

    private fun loadViews(isItFromSaved: Boolean = false) {
        /**
         * If this fragment called from bookmarkFragment it is not necessary to loadBelow
         * So, below used if statement.
         */
        if (!isItFromSaved) {
            loadObservers()
            loadUserLocation()
            loadBottomSheetBehavior()
            loadSuggestListener()
            loadButtons()
            loadMapTapListeners()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.resultLiveData.observe(this, addToDBResult)
    }

    private val addToDBResult = Observer<Boolean> {
        showBottomMenu()
        showMessage("Добавлено успешно")
    }

    private fun loadButtons() {

        binding.map.map.move(
            CameraPosition(Point(41.311223, 69.279298), 9f, 0f, 0f)
        ) // moves to center of tashkent

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.navigateToCurrent.setOnClickListener {
            if (localStorage.currentLat != 0.0)
                navigateToPoint(Point(localStorage.currentLat, localStorage.currentLong))
            else showMessage("Пожалуйста подождите. Ваше местоположение определяется.")
        }

        searchResultAdapter.setOnItemClickListener {
            if (it.longitude != 0.0) {
                hideKeyboard()
                val point = Point(it.latitude, it.longitude)
                navigateToPoint(point)
                binding.bottomSheet.gone()
                setPlaceData(it)
            }
        }

        binding.searchResult.itemAnimator = null

    }

    private fun setPlaceData(placeModel: PlaceModel, isItFromSaved: Boolean = false) {
        binding.placeItem.root.visible()

        (activity as MainActivity).hideBottomMenu()

        if (isItFromSaved) {
            binding.placeItem.closeButton.gone()
            binding.placeItem.saveButton.gone()
        } else {
            binding.placeItem.closeButton.visible()
            binding.placeItem.saveButton.visible()
        }

        binding.placeItem.closeButton.setOnClickListener {
            showBottomMenu()
        }

        binding.placeItem.saveButton.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogBinding = SaveDialogBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialogBinding.title.setText(placeModel.title)
            dialogBinding.saveButton.setOnClickListener {
                placeModel.title = dialogBinding.title.text.toString()
                viewModel.addPlaceModelToDB(placeModel)
                dialog.dismiss()
            }

            dialogBinding.editIcon.setOnClickListener {
                dialogBinding.title.requestFocus()
                dialogBinding.title.requestFocusFromTouch()
            }

            dialogBinding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        if (placeModel.score != null) {
            binding.placeItem.ratingGroup.visible()
            binding.placeItem.ratings.text = placeModel.score.toString()
            binding.placeItem.ratingView.rating = placeModel.score ?: 0F
            binding.placeItem.reviewCount.text = placeModel.allReview.toString() + " отзыв"
        } else {
            binding.placeItem.ratingGroup.gone()
        }

        binding.placeItem.title.text = placeModel.title
        if (placeModel.subtitle == "")
            binding.placeItem.subTitle.gone()
        else {
            binding.placeItem.subTitle.visible()
            binding.placeItem.subTitle.text = placeModel.subtitle
        }
    }

    private fun showBottomMenu() {
        binding.placeItem.root.gone()
        (activity as MainActivity).showBottomMenu()
    }

    private fun showMessage(text: String) {
        SneakerMaker.showSneaker(requireActivity(), text)
    }

    private fun loadBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
    }

    private fun hideKeyboard() {
        try {
            val inputManager: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        } catch (e: Exception) {

        }
    }

    private fun navigateToPoint(point: Point) {
        binding.map.map.move(
            CameraPosition(point, 17.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun loadMapTapListeners() {
        binding.map.map.addTapListener(geoObjectTapListener)
    }

    private val geoObjectTapListener = GeoObjectTapListener { geoObjectTapEvent ->
        val name = geoObjectTapEvent.geoObject.name
        val selectionMetadata = geoObjectTapEvent.geoObject.metadataContainer.getItem(
            GeoObjectSelectionMetadata::class.java
        )
        val description = geoObjectTapEvent.geoObject.descriptionText
        val lat = geoObjectTapEvent.geoObject.geometry[0].point?.latitude ?: 0.0
        val long = geoObjectTapEvent.geoObject.geometry[0].point?.longitude ?: 0.0

        val currentLocation = Location("current")
        currentLocation.latitude = localStorage.currentLat
        currentLocation.longitude = localStorage.currentLong

        val foundLocation = Location("found")
        var distance = 0F
        if (lat != 0.0) {
            foundLocation.latitude = lat
            foundLocation.longitude = long
            distance = currentLocation.distanceTo(foundLocation)
        }

        val rating =
            geoObjectTapEvent.geoObject.metadataContainer.getItem(BusinessRating1xObjectMetadata::class.java)

        val placeModel = PlaceModel(
            0,
            name ?: "Not found",
            description ?: "",
            if (distance == 0F) "NaN" else distance.metrToKM(),
            rating?.ratings,
            rating?.score,
            long,
            lat,
        )
        val point = geoObjectTapEvent.geoObject.geometry[0].point
        if (point != null)
            navigateToPoint(point)
        setPlaceData(placeModel)

        selectionMetadata != null
    }


    private fun loadUserLocation() {
        binding.map.map.isRotateGesturesEnabled = true
        val userLocationLayer =
            MapKitFactory.getInstance().createUserLocationLayer(binding.map.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(userLocationDrawer)
    }

    private val userLocationDrawer = object : UserLocationObjectListener {

        override fun onObjectAdded(userLocationView: UserLocationView) {
            val latLng = userLocationView.accuracyCircle.geometry.center
            if (latLng.latitude != 0.0) {
                setPointToLocalStorage(latLng)
            }

            userLocationView.accuracyCircle.fillColor = Color.TRANSPARENT
        }

        override fun onObjectRemoved(userLocationView: UserLocationView) {
        }

        override fun onObjectUpdated(userLocationView: UserLocationView, p1: ObjectEvent) {
            val latLng = userLocationView.accuracyCircle.geometry.center
            if (latLng.latitude != 0.0) {
                setPointToLocalStorage(latLng)
            }
        }
    }

    private fun setPointToLocalStorage(point: Point) {
        localStorage.currentLat = point.latitude
        localStorage.currentLong = point.longitude
    }

    private fun loadSuggestListener() {
        val searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        searchManager.createSuggestSession()
        binding.searchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResult.adapter = searchResultAdapter

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.placeItem.root.gone()
                if (text?.isNotEmpty() == true) {
                    binding.bottomSheet.visible()
                    binding.record.gone()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    (activity as MainActivity).hideBottomMenu()
                } else {
                    binding.bottomSheet.gone()
                    binding.record.visible()
                    (activity as MainActivity).showBottomMenu()
                }

                val searchOption = SearchOptions()
                searchOption.snippets = Snippet.BUSINESS_RATING1X.value

                searchManager.submit(
                    text.toString(),
                    VisibleRegionUtils.toPolygon(binding.map.map.visibleRegion),
                    searchOption,
                    searchListener
                )
            }

            override fun afterTextChanged(text: Editable?) {}
        })
    }

    private val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val resultList = ArrayList<PlaceModel>()
            for (suggest in response.collection.children) {
                val currentLocation = Location("current")
                currentLocation.latitude = localStorage.currentLat
                currentLocation.longitude = localStorage.currentLong

                val foundPoint = suggest.obj?.geometry?.get(0)?.point
                val foundLocation = Location("found")
                var distance = 0F
                if (foundPoint != null) {
                    foundLocation.latitude = foundPoint.latitude
                    foundLocation.longitude = foundPoint.longitude
                    distance = currentLocation.distanceTo(foundLocation)
                }

                val rating =
                    suggest.obj?.metadataContainer?.getItem(BusinessRating1xObjectMetadata::class.java)

                val placeModel = PlaceModel(
                    0,
                    suggest.obj?.name.toString(),
                    suggest.obj?.descriptionText ?: "",
                    if (distance == 0F) "NaN" else distance.metrToKM(),
                    rating?.ratings,
                    rating?.score,
                    suggest.obj?.geometry?.get(0)?.point?.longitude ?: 0.0,
                    suggest.obj?.geometry?.get(0)?.point?.latitude ?: 0.0,
                )
                resultList.add(placeModel)
            }
            searchResultAdapter.submitResultsList(resultList, binding.search.text?.toString() ?: "")

        }

        override fun onSearchError(p0: Error) {

        }
    }

    override fun onStart() {
        binding.map.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onStop() {
        binding.map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}