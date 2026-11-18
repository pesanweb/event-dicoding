package com.example.dicodingevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.local.room.FavoriteEvent
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.databinding.ActivityDetailEventBinding
import com.example.dicodingevent.ui.DetailEventViewModel
import com.example.dicodingevent.ui.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val detailViewModel: DetailEventViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private var currentEvent: ListEventsItem? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Event"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId != -1) {
            fetchDetailEvent(eventId)
            
            detailViewModel.getFavoriteEventById(eventId.toString()).observe(this) { favoriteEvent ->
                isFavorite = (favoriteEvent != null)
                if (isFavorite) {
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            currentEvent?.let { event ->
                val favoriteEvent = FavoriteEvent(
                    id = event.id.toString(),
                    name = event.name,
                    mediaCover = event.imageLogo
                )
                if (isFavorite) {
                    detailViewModel.delete(favoriteEvent)
                    Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insert(favoriteEvent)
                    Toast.makeText(this, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchDetailEvent(id: Int) {
        binding.progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getDetailEvent(id)
                }
                binding.progressBar.visibility = View.GONE
                if (!response.error) {
                    val event = response.event
                    currentEvent = event
                    binding.tvEventName.text = event.name
                    binding.tvEventOwner.text = "Penyelenggara: ${event.ownerName}"
                    binding.tvEventTime.text = "Waktu: ${event.beginTime}"

                    val sisaKuota = event.quota - event.registrants
                    binding.tvEventQuota.text = "Sisa Kuota: $sisaKuota"

                    binding.tvEventDescription.text = HtmlCompat.fromHtml(
                        event.description,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    Glide.with(this@DetailEventActivity)
                        .load(event.imageLogo)
                        .into(binding.imgEventLogo)

                    binding.btnRegister.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@DetailEventActivity, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@DetailEventActivity, "Gagal memuat detail: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
