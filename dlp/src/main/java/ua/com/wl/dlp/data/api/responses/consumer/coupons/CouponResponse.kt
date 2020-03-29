package ua.com.wl.dlp.data.api.responses.consumer.coupons

import com.google.gson.annotations.SerializedName

import ua.com.wl.dlp.data.api.models.shop.SimpleShop
import ua.com.wl.dlp.data.api.responses.models.another.NoveltyDatesRange
import ua.com.wl.dlp.data.api.responses.models.consumer.coupons.CouponType
import ua.com.wl.dlp.data.api.responses.shop.offer.BaseOfferResponse

/**
 * @author Denis Makovskyi
 */

data class CouponResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: CouponType,

    @SerializedName("name")
    val name: String,

    @SerializedName("barcode")
    val barcode: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val imageUrl: String?,

    @SerializedName("used_count")
    val usedCount: Int,

    @SerializedName("money_spent")
    val moneySpent: String,

    @SerializedName("count_available")
    val countAvailable: Int?,

    @SerializedName("monetary_available")
    val monetaryAvailable: String,

    @SerializedName("offer")
    val offer: BaseOfferResponse?,

    @SerializedName("date_range")
    val dateRange: NoveltyDatesRange?,

    @SerializedName("shops")
    val shops: List<SimpleShop>)