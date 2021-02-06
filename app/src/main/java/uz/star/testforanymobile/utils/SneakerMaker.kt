package uz.star.testforanymobile.utils

import android.app.Activity
import android.view.LayoutInflater
import android.widget.TextView
import com.irozon.sneaker.Sneaker
import uz.star.testforanymobile.R

/**
 * Created by Farhod Tohirov on 06-Feb-21
 **/

object SneakerMaker {
    fun showSneaker(activity: Activity, text: String) {
        val sneaker = Sneaker.with(activity)
            .setDuration(3000)
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.custom_snackbar, sneaker.getView(), false)
        view.findViewById<TextView>(R.id.sneakerErrorMessage).text = text
        sneaker.sneakCustom(view)
    }
}