package com.example.dicodingevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        // TODO: Implement detail event view
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
