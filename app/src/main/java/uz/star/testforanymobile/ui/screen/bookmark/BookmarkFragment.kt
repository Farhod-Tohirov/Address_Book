package uz.star.testforanymobile.ui.screen.bookmark

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.star.testforanymobile.data.room.entity.PlaceModel
import uz.star.testforanymobile.databinding.FragmentBookmarkBinding
import uz.star.testforanymobile.ui.adapters.BookMarkAdapter
import uz.star.testforanymobile.utils.SneakerMaker

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: BookMarkViewModel by viewModels()
    private val adapter = BookMarkAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadObservers()
        loadViews()
        viewModel.getSavePlaces()
    }

    private fun loadViews() {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener { placeModel ->
            val dialog = AlertDialog.Builder(requireContext()).create()
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Удалить") { d, v ->
                viewModel.deletePlaceMark(placeModel)
            }

            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "На карте") { d, v ->
                val action = BookmarkFragmentDirections.actionBookmarkFragmentToMapFragment(placeModel)
                findNavController().navigate(action)
            }

            dialog.setTitle("Вы хотите удалить?")
            dialog.show()
        }

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.places.observe(this, placesObserver)
        viewModel.message.observe(this, messageObserver)
        viewModel.delete.observe(this, deleteObserver)
    }

    private val deleteObserver = Observer<Boolean> {
        showMessage("успешно удален")
    }

    private val placesObserver = Observer<List<PlaceModel>> {
        adapter.submitList(it.toMutableList())
    }

    private val messageObserver = Observer<String> {
        showMessage(it)
    }

    private fun showMessage(text: String) {
        SneakerMaker.showSneaker(requireActivity(), text)
    }
}