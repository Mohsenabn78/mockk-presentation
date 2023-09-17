package com.mohsen.nobka.ui.nobka


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.nobka.core.data.remote.error.Failure
import com.mohsen.nobka.core.extension.launchAndCollectIn
import com.mohsen.nobka.databinding.NobkaFragmentBinding
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import com.mohsen.nobka.ui.nobka.adapter.NobkaContentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class NobkaFragment : Fragment() {
    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: NobkaFragmentBinding? = null
    private val binding get() = _binding!!

    // inject viewModel
    private val viewModel: NobkaViewModel by viewModels()

    // inject adapter
    @Inject
    lateinit var adapter: NobkaContentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NobkaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        collectLifeCycleAwareNobkaState()
        setupButtonListener()
    }

    private fun setupButtonListener() {
        binding.fetchButton.setOnClickListener {
            viewModel.fetchDataRequest()
        }
    }

    private fun collectLifeCycleAwareNobkaState() {
        viewModel.nobkaUiState.launchAndCollectIn(this) { state ->
            uiStateHandler(state)
        }
    }

    private fun uiStateHandler(uiState: NobkaViewModel.NobkaUiState) {
        when (uiState) {
            is NobkaViewModel.NobkaUiState.Success -> {
                binding.fetchButton.visibility = View.GONE
                binding.progress.visibility = View.GONE
                setupContentRecycler(uiState.result)
            }

            is NobkaViewModel.NobkaUiState.Error -> {
                binding.progress.visibility = View.GONE
                errorHandler(uiState.failure)
            }

            is NobkaViewModel.NobkaUiState.Loading -> {
                setupPreLoadProgress()
            }
        }
    }

    private fun setupContentRecycler(moviesList: List<NobkaMoviesModel>) {
        adapter.setData(moviesList)
        binding.contentRecycler.setHasFixedSize(true)
        binding.contentRecycler.adapter = adapter
    }

    private fun errorHandler(failure: Failure) {
        val message = when (failure) {
            is Failure.ConnectionError -> "No Connection!"
            is Failure.ApiResourceBoundError<*> -> "DataBase is Malformed!"
            is Failure.FlowError -> "Application Error"
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setupPreLoadProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}