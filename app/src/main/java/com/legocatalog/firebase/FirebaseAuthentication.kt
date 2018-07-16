package com.legocatalog.firebase

import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

class FirebaseAuthentication {

    companion object {
        val UNKNOWN_ERROR = 5
    }

    fun buildAuthIntent(): Intent {
        val providers = mutableListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
    }

    fun handleAuthResponse(data: Intent?,
                           resultCode: Int,
                           onSuccess: (IdpResponse) -> Unit,
                           onError: (String) -> Unit) {
        val response = IdpResponse.fromResultIntent(data)
        response?.let { response ->
            when (resultCode) {
                Activity.RESULT_OK -> onSuccess(response)
                else -> onError(toFriendlyMessage(response.error?.errorCode ?: UNKNOWN_ERROR))
            }
        }
    }

    private fun toFriendlyMessage(code: Int): String {
        when (code) {
            ErrorCodes.UNKNOWN_ERROR -> return "Unknown error"
            ErrorCodes.NO_NETWORK -> return "No internet connection"
            ErrorCodes.PLAY_SERVICES_UPDATE_CANCELLED -> return "Play Services update cancelled"
            ErrorCodes.DEVELOPER_ERROR -> return "Developer error"
            ErrorCodes.PROVIDER_ERROR -> return "Provider error"
            else -> throw IllegalArgumentException("Unknown code: $code")
        }
    }
}