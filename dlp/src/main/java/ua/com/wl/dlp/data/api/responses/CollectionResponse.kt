package ua.com.wl.dlp.data.api.responses

import com.google.gson.annotations.SerializedName

/**
 * @author Denis Makovskyi
 */

data class CollectionResponse<T>(
    @SerializedName(value = "data")
    val items: List<T>)