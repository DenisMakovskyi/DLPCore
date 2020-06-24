package ua.com.wl.dlp.data.api.responses.models.shop.offer.promo

import com.google.gson.annotations.SerializedName

/**
 * @author Denis Makovskyi
 */

@Deprecated(
    message = "This class will be removed in further revisions. Changes are caused by offer promo structure refactoring.")
enum class PromoType(val type: String) {
    @SerializedName("GIFT")
    GIFT("GIFT"),
    @SerializedName("SALE")
    SALE("SALE"),
    @SerializedName("EVENT")
    EVENT("EVENT"),
    @SerializedName("DISCOUNT")
    DISCOUNT("DISCOUNT"),
    @SerializedName("BY_BONUS")
    BY_BONUS("BY_BONUS"),
    @SerializedName("EXCLUSIVE_CASH_BACK")
    EXCLUSIVE_CASH_BACK("EXCLUSIVE_CASH_BACK"),
}