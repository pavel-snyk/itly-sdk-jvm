package ly.iterative.itly.test

import ly.iterative.itly.*
import ly.iterative.itly.iteratively.*
import okhttp3.mockwebserver.*
import org.junit.jupiter.api.Assertions

const val NOT_INITIALIZED_ERROR_MESSAGE = "Itly is not initialized. Call Itly.load(Options(...))"

class Asserts {
    companion object {
        fun assertValidTrackerRequest(
                request: RecordedRequest,
                trackType: TrackType,
                event: Event? = null,
                apiKey: String = "api-key",
                trackerPath: String = "/t/version/company-id",
                requestContentType: String = "application/json"
        ) {
            val trackTypeText = if (trackType == TrackType.track)
                "\"eventName\":\"${event?.name}\"" else "\"type\":\"$trackType\""

            val body = request.body.readUtf8()

            // Path & Method
            Assertions.assertEquals(
                "POST", request.method,
                "should POST data to server"
            )

            Assertions.assertEquals(
                trackerPath, request.path,
                "should make requests to correct endpoint"
            )

            Assertions.assertEquals(
                "Bearer $apiKey", request.getHeader("authorization"),
                "should have authorization"
            )

            Assertions.assertTrue(
                request.getHeader("Content-Type")!!.contains(requestContentType),
                "should have JSON 'Content-Type'"
            )

            Assertions.assertTrue(
                body.contains(trackTypeText),
                "should contain track type ($trackTypeText) in json body. $body"
            )
        }

        fun assertThrowsErrorNotInitialized(executable: () -> Unit) {
            val exception = Assertions.assertThrows(Exception::class.java, executable)
            Assertions.assertEquals(NOT_INITIALIZED_ERROR_MESSAGE, exception.message)
        }
    }
}
