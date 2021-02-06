package uz.star.testforanymobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.databinding.PlaceItemRecycleBinding
import uz.star.testforanymobile.utils.SingleBlock
import uz.star.testforanymobile.utils.gone
import uz.star.testforanymobile.utils.visible

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

class BookMarkAdapter :
    ListAdapter<PlaceModel, BookMarkAdapter.ViewHolder>(DIFF_SEARCH_CALLBACK) {

    private var listenClick: SingleBlock<PlaceModel>? = null

    companion object {
        var DIFF_SEARCH_CALLBACK = object : DiffUtil.ItemCallback<PlaceModel>() {
            override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                newItem.hashCode() == oldItem.hashCode()

            override fun areContentsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                newItem.title == oldItem.title && newItem.subtitle == oldItem.subtitle && newItem.distance == oldItem.distance
        }
    }

    inner class ViewHolder(private val binding: PlaceItemRecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val placeModel = getItem(adapterPosition)
            binding.title.text = placeModel.title
            binding.subtitle.text = placeModel.subtitle
            if (placeModel.score != null) {
                binding.ratingGroup.visible()
                binding.ratings.text = placeModel.score.toString()
                binding.ratingView.rating = placeModel.score ?: 0F
                binding.reviewCount.text = placeModel.allReview.toString() + " отзыв"
            } else {
                binding.ratingGroup.gone()
            }
            binding.root.setOnClickListener { listenClick?.invoke(placeModel) }
        }
    }

    fun setOnItemClickListener(f: SingleBlock<PlaceModel>) {
        listenClick = f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        PlaceItemRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}