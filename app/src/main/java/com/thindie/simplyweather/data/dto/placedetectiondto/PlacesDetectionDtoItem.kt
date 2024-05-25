package com.thindie.simplyweather.data.dto.placedetectiondto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesDetectionDtoItem(
    @SerialName("addresstype")
    val addressType: String,
    @SerialName("boundingbox")
    val boundingBox: List<String>,
    @SerialName("class")
    val classX: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("importance")
    val importance: Double,
    @SerialName("lat")
    val latitude: String,
    @SerialName("licence")
    val licence: String,
    @SerialName("lon")
    val longitude: String,
    @SerialName("name")
    val name: String,
    @SerialName("osm_id")
    val osmId: Long,
    @SerialName("osm_type")
    val osmType: String,
    @SerialName("place_id")
    val placeId: Int,
    @SerialName("place_rank")
    val placeRank: Int,
    @SerialName("type")
    val type: String,
)