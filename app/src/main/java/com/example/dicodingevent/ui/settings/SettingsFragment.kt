package com.example.dicodingevent.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.databinding.FragmentSettingsBinding
import com.example.dicodingevent.ui.ViewModelFactory
import com.example.dicodingevent.utils.NotificationHelper
import com.example.dicodingevent.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                binding.switchReminder.isChecked = false
                settingsViewModel.saveNotificationSetting(false)
                setDailyReminder(false)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NotificationHelper.createNotificationChannel(requireContext())

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.switchTheme.setOnCheckedChangeListener(null)
            binding.switchTheme.isChecked = isDarkModeActive
            binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.saveThemeSetting(isChecked)
            }
        }

        settingsViewModel.getNotificationSetting().observe(viewLifecycleOwner) { isActive ->
            binding.switchReminder.setOnCheckedChangeListener(null)
            binding.switchReminder.isChecked = isActive
            binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    settingsViewModel.saveNotificationSetting(true)
                } else {
                    settingsViewModel.saveNotificationSetting(isChecked)
                }
                setDailyReminder(isChecked)
            }
        }
    }

    private fun setDailyReminder(isActive: Boolean) {
        val workManager = WorkManager.getInstance(requireContext())
        if (isActive) {
            val dailyReminderRequest =
                PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS).build()
            workManager.enqueueUniquePeriodicWork(
                DailyReminderWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                dailyReminderRequest
            )
        } else {
            workManager.cancelUniqueWork(DailyReminderWorker.WORK_NAME)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
