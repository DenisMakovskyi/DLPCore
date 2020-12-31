package ua.com.wl.notificator.data

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import androidx.annotation.RestrictTo
import ua.com.wl.notificator.dsl.NotificationMarker
import ua.com.wl.notificator.dsl.PendingIntentMarker
import ua.com.wl.notificator.dsl.SemanticMarker
import ua.com.wl.notificator.dsl.TaskStackMarker
import ua.com.wl.notificator.utils.buildMessage
import ua.com.wl.notificator.utils.fromFirst
import ua.com.wl.notificator.utils.isSingle
import ua.com.wl.notificator.utils.safe

/**
 * @author Denis Makovskyi
 */

enum class From {
    SERVICE,
    ACTIVITY,
    BROADCAST
}

@SemanticMarker
@TaskStackMarker
@PendingIntentMarker
class PendingIntentBuilder {

    private var requestCode: Int = 100
    private var pendingFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT
    private var targetIntent: From = From.ACTIVITY
    private var packageContext: Context? = null
    private var taskStackElements: MutableList<TaskStackElement> = mutableListOf()

    operator fun TaskStackElement.unaryPlus() {
        taskStackElements.add(this)
    }

    fun requestCode(init: () -> Int) {
        requestCode = init()
    }

    fun pendingFlag(init: () -> Int) {
        pendingFlags = pendingFlags or init()
    }

    fun pendingFlags(vararg flags: Int) {
        pendingFlags = flags.toList()
            .reduce { acc, flag ->
                acc or flag
            }
    }

    fun targetIntent(init: () -> From) {
        targetIntent = init()
    }

    fun packageContext(init: () -> Context?) {
        packageContext = init()
    }

    fun taskStackElement(init: TaskStackElement.Builder.() -> Unit) {
        +TaskStackElement.Builder().build(init)
    }

    @Deprecated(
        "Use DSL-style function instead",
        ReplaceWith(
            "taskStackElement(init: TaskStackElement.Builder.() -> Unit)",
            "ua.makovskyi.notificator.data"
        ),
        DeprecationLevel.WARNING
    )
    fun taskStackElements(vararg elements: TaskStackElement) {
        taskStackElements = elements.toMutableList()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun build(): PendingIntent {
        require(taskStackElements.isNotEmpty()) {
            buildMessage(
                PendingIntentBuilder::class,
                "Can not create pending intent from empty tasks list"
            )
        }
        val context = requireNotNull(packageContext) {
            buildMessage(
                PendingIntentBuilder::class,
                "To create pending intent, please pass your package context"
            )
        }
        return when (targetIntent) {
            From.SERVICE -> {
                when (val intent = taskStackElements.fromFirst { it?.intent }) {
                    null ->
                        throw IllegalArgumentException(
                            buildMessage(
                                PendingIntentBuilder::class,
                                "Can not create pending intent from empty intent"
                            )
                        )
                    else -> PendingIntent.getService(context, requestCode, intent, pendingFlags)
                }
            }
            From.ACTIVITY -> {
                if (taskStackElements.isSingle()) {
                    taskStackElements.fromFirst { e -> e?.intent }.let { intent ->
                        PendingIntent.getActivity(context, requestCode, intent, pendingFlags)
                    }
                } else {
                    requireNotNull(TaskStackBuilder.create(context).run {
                        for (element in taskStackElements) {
                            element.intent.safe { intent ->
                                when (element.howPut) {
                                    HowPut.ONLY_NEXT_INTENT -> addNextIntent(intent)
                                    HowPut.ONLY_EXTRACT_PARENT -> addParentStack(intent.component)
                                    HowPut.NEXT_INTENT_WITH_PARENT -> addNextIntentWithParentStack(
                                        intent
                                    )
                                }
                            }
                        }
                        getPendingIntent(requestCode, pendingFlags)
                    })
                }
            }
            From.BROADCAST -> {
                when (val intent = taskStackElements.fromFirst { e -> e?.intent }) {
                    null ->
                        throw IllegalArgumentException(
                            buildMessage(
                                PendingIntentBuilder::class,
                                "Can not create pending intent from empty intent"
                            )
                        )
                    else -> PendingIntent.getBroadcast(context, requestCode, intent, pendingFlags)
                }
            }
        }
    }

    internal fun build(init: PendingIntentBuilder.() -> Unit): PendingIntent {
        init()
        return build()
    }
}

data class Intention(
    val autoCancel: Boolean,
    val deleteIntent: PendingIntent?,
    val contentIntent: PendingIntent?
) {

    @NotificationMarker
    @PendingIntentMarker
    open class Builder(
        protected var autoCancel: Boolean = true,
        protected var deleteIntent: PendingIntent? = null,
        protected var contentIntent: PendingIntent? = null
    ) {

        constructor(intention: Intention) : this(
            intention.autoCancel,
            intention.deleteIntent,
            intention.contentIntent
        )

        fun autoCancel(init: () -> Boolean) {
            autoCancel = init()
        }

        fun deleteIntent(builder: PendingIntentBuilder) {
            deleteIntent = builder.build()
        }

        fun deleteIntent(init: PendingIntentBuilder.() -> Unit) {
            deleteIntent = PendingIntentBuilder().build(init)
        }

        fun contentIntent(builder: PendingIntentBuilder) {
            contentIntent = builder.build()
        }

        fun contentIntent(init: PendingIntentBuilder.() -> Unit) {
            contentIntent = PendingIntentBuilder().build(init)
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun build(): Intention = Intention(autoCancel, deleteIntent, contentIntent)

        internal open fun build(init: Builder.() -> Unit): Intention {
            init()
            return build()
        }
    }
}

fun notificationIntention(init: Intention.Builder.() -> Unit): Intention =
    Intention.Builder().build(init)