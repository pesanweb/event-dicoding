package com.example.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class ListEventsItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("ownerName")
    val ownerName: String,

    @field:SerializedName("beginTime")
    val beginTime: String,

    @field:SerializedName("quota")
    val quota: Int,

    @field:SerializedName("registrant")
    val registrant: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("imageLogo")
    val imageLogo: String,

    @field:SerializedName("link")
    val link: String
)
