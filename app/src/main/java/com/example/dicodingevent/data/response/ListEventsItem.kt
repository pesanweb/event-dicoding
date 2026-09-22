package com.example.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class ListEventsItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("ownerName")
    val ownerName: String,

    @field:SerializedName("beginTime")
    val beginTime: String,

    @field:SerializedName("endTime")
    val endTime: String? = null,

    @field:SerializedName("quota")
    val quota: Int,

    @field:SerializedName("registrants")
    val registrants: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("imageLogo")
    val imageLogo: String,

    @field:SerializedName("mediaCover")
    val mediaCover: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("cityName")
    val cityName: String? = null,

    @field:SerializedName("link")
    val link: String
)
