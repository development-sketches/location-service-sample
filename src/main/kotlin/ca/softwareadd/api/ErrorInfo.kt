package ca.softwareadd.api

/**
 * @author Eugene Ossipov
 */
data class ErrorInfo(
        val error: String,
        val exception: String,
        val message: String?,
        val status: Int = 200
)
