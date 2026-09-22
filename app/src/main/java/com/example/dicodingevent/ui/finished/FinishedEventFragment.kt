package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.EventAdapter

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private lateinit var viewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter()
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        viewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
