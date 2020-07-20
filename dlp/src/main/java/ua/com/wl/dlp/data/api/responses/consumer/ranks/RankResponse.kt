package ua.com.wl.dlp.data.api.responses.consumer.ranks

import com.google.gson.annotations.SerializedName

import ua.com.wl.dlp.data.api.responses.models.consumer.rank.RankPermissions
import ua.com.wl.dlp.data.api.responses.models.consumer.rank.RankSelectionCriteria

/**
 * @author Denis Makovskyi
 */

open class RankResponse(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("rank_priority_number")
    var priority: Int = 0,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("thumb_icon")
    var iconUrl: String = "",

    @SerializedName("color")
    var colorHex: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("is_next_rank")
    var isNext: Boolean = false,

    @SerializedName("is_current_rank")
    var isCurrent: Boolean = false,

    @SerializedName("is_previous_rank")
    var isPrevious: Boolean = false,

    @Transient
    var wasReached: Boolean = false,

    @SerializedName("permissions")
    val permissions: RankPermissions? = null,

    @SerializedName("selection_criteria")
    var selectionCriteria: RankSelectionCriteria? = null)