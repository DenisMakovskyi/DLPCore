package ua.com.wl.dlp.data.api.responses.models.orders.order

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ConsumerOrder(
    @Json(name = "rs_order_id")
    val id: String,

    @Json(name = "rs_price_in_money")
    val amount: String?,

    @Json(name = "rs_price_in_bonuses")
    val bonuses: Long?
) : Parcelable
