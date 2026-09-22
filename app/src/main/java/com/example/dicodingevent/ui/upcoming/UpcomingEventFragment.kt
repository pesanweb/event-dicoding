package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.EventAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter()
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        fetchUpcomingEvents()
    }

    private fun fetchUpcomingEvents() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getEvents(active = 1)
                }
                binding.loadingProgressBar.visibility = View.GONE
                if (!response.error) {
                    eventAdapter.submitList(response.listEvents)
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.loadingProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Gagal memuat data: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
