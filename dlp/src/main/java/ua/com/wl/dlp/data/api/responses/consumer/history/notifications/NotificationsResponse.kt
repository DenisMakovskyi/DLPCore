package ua.com.wl.dlp.data.api.responses.consumer.history.notifications

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.com.wl.dlp.data.api.responses.PagedResponse

@JsonClass(generateAdapter = true)
class NotificationsResponse(
    @Json(name = "notification_count")
    val notificationsCount: Int,

    @Json(name = "notification_read_count")
    val notificationsReadCount: Int,

    @Json(name = "notification_unread_count")
    val notificationsUnreadCount: Int,

    @Json(name = "page_number")
    override val page: Int? = null,
    @Json(name = "total_pages_count")
    override val pagesCount: Int? = null,
    @Json(name = "total_items_count")
    override val itemsCount: Int? = null,
    @Json(name = "next")
    override val nextPage: String? = null,
    @Json(name = "previous")
    override val previousPage: String? = null,
    /**
     * merged fields for items.
     */
    @Json(name = "data")
    override val data: List<NotificationResponse> = listOf(),
    @Json(name = "results")
    override val results: List<NotificationResponse> = listOf(),
    /**
     * merged fields for count.
     */
    @Json(name = "count")
    override val countNumber: Int? = null,
    @Json(name = "per_page")
    override val perPage: Int? = null,
    @Json(name = "page_size")
    override val pageSize: Int? = null
) : PagedResponse<NotificationResponse>(
    page = page,
    pagesCount = pagesCount,
    itemsCount = itemsCount,
    nextPage = nextPage,
    previousPage = previousPage,
    data = data,
    results = results,
    countNumber = countNumber,
    perPage = perPage,
    pageSize = pageSize
)