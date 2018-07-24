package com.legocatalog.repository.remote.rebrickable

import androidx.work.Data
import androidx.work.Worker
import androidx.work.toWorkData
import com.google.gson.Gson
import com.legocatalog.LegoCatalogApp
import com.legocatalog.model.ErrorResponse
import com.legocatalog.model.LegoPartsWrapper
import retrofit2.Response
import javax.inject.Inject

class PartsWorker: Worker() {

    companion object {
        val NUMBER_KEY = "number_key"
        val ERROR_MESSAGE_KEY = "error_message"
    }

    @Inject
    lateinit var rebrickableService: RebrickableService

    override fun doWork(): Result {
        (applicationContext as LegoCatalogApp).appComponent.inject(this)

        try {
            val numberKey = inputData.getString(NUMBER_KEY, null)
            if (numberKey != null) {
                val response = rebrickableService.setParts(numberKey).execute()
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.FAILURE
    }

    private fun handleError(response: Response<LegoPartsWrapper>) {
        val errorMessage = response.errorBody()?.string()
        errorMessage?.let {
            val errorModel = Gson().fromJson(it, ErrorResponse::class.java)
            outputData = mapOf(ERROR_MESSAGE_KEY to errorModel.detail).toWorkData()
        }
    }

    private fun handleSuccess(response: Response<LegoPartsWrapper>) {
        Data.Builder().
        response.body()?.let {
            outputData = it.results?
        }
    }
}