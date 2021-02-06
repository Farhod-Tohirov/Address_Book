package uz.star.testforanymobile.ui.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.databinding.SearchResultViewItemBinding
import uz.star.testforanymobile.utils.SingleBlock
import java.util.*

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

class SearchResultAdapter :
    ListAdapter<PlaceModel, SearchResultAdapter.ViewHolder>(DIFF_SEARCH_CALLBACK) {

    private var listenClick: SingleBlock<PlaceModel>? = null
    private var currentText = ""

    companion object {
        var DIFF_SEARCH_CALLBACK = object : DiffUtil.ItemCallback<PlaceModel>() {
            override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                newItem.hashCode() == oldItem.hashCode()

            override fun areContentsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                newItem.title == oldItem.title && newItem.subtitle == oldItem.subtitle && newItem.distance == oldItem.distance
        }
    }

    inner class ViewHolder(private val binding: SearchResultViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {

            val title = getItem(adapterPosition).title
            val spannable = SpannableString(title)

            val spanLower = SpannableString(title.toLowerCase(Locale.ROOT))

            val index = spanLower.indexOf(currentText)
            if (index > 0)
                spannable.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    index,
                    index + currentText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            binding.title.text = spannable
            binding.subtitle.text = getItem(adapterPosition).subtitle
            binding.distance.text = getItem(adapterPosition).distance
            binding.root.setOnClickListener { listenClick?.invoke(getItem(adapterPosition)) }
        }
    }

    fun setOnItemClickListener(f: SingleBlock<PlaceModel>) {
        listenClick = f
    }

    fun submitResultsList(list: List<PlaceModel>, text: String) {
        submitList(list)
        currentText = text.toLowerCase(Locale.ROOT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SearchResultViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}