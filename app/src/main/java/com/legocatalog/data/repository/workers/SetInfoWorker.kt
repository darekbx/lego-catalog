package com.legocatalog.data.repository.workers

import androidx.work.Worker
import androidx.work.toWorkData
import com.google.gson.Gson
import com.legocatalog.LegoCatalogApp
import com.legocatalog.data.remote.model.ErrorResponse
import com.legocatalog.data.remote.model.LegoSet
import com.legocatalog.data.remote.rebrickable.RebrickableService
import com.legocatalog.extensions.toMap
import com.legocatalog.extensions.toSetInfo
import retrofit2.Response
import javax.inject.Inject

class SetInfoWorker: Worker() {

    companion object {
        val NUMBER_KEY = "number_key"
        val ERROR_MESSAGE_KEY = "error_message"
    }

    @Inject
    lateinit var rebrickableService: RebrickableService

    override fun doWork(): Result {
        (applicationContext as LegoCatalogApp).appComponent.inject(this)

        try {
            val numberKey = inputData.getString(NUMBER_KEY)
            if (numberKey != null) {
                return fetchSetInfo(numberKey)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.FAILURE
    }

    private fun fetchSetInfo(number: String): Result {
        val response = rebrickableService.setByNumber(number).execute()
        return when (response.isSuccessful) {
            true -> {
                handleSuccess(response)
                return Result.SUCCESS
            }
            else -> {
                handleError(response)
                return Result.FAILURE
            }
        }
    }

    private fun handleError(response: Response<LegoSet>) {
        val errorMessage = response.errorBody()?.string()
        errorMessage?.let {
            val errorModel = Gson().fromJson(it, ErrorResponse::class.java)
            outputData = mapOf(ERROR_MESSAGE_KEY to errorModel.detail).toWorkData()
        }
    }

    private fun handleSuccess(response: Response<LegoSet>) {
        response.body()?.run {
            val set = toSetInfo().toMap()
            outputData = set.toMap().toWorkData()
        }
    }
}