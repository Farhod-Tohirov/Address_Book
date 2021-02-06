package uz.star.testforanymobile.ui.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.search.SearchFactory
import dagger.hilt.android.AndroidEntryPoint
import uz.star.testforanymobile.R
import uz.star.testforanymobile.databinding.ActivityMainBinding
import uz.star.testforanymobile.utils.gone
import uz.star.testforanymobile.utils.visible
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        MapKitFactory.setLocale("ru_RU")
        MapKitFactory.initialize(this)
        SearchFactory.initialize(this)

        val radius = resources.getDimension(R.dimen.radius_small)
        val bottomNavigationViewBackground =
            binding.bottomMenu.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
        setContentView(binding.root)

        loadViews()

    }

    private fun loadViews() {
        val bottomNavigationView = binding.bottomMenu
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    fun showBottomMenu(){
        binding.bottomMenu.visible()
    }

    fun hideBottomMenu(){
        binding.bottomMenu.gone()
    }

}