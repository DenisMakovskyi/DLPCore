package ua.com.wl.dlp.domain.exeptions

import android.content.Context

import ua.com.wl.dlp.common.R

/**
 * @author Denis Makovskyi
 */

open class CoreRuntimeException(
    message: String? = null,
    cause: Throwable? = null
) : CoreException(message, cause) {

    override fun getLocalizedMessage(context: Context): String {
        return context.getString(R.string.dlp_error_runtime)
    }
}