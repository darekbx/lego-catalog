package com.legocatalog.data.repository.workers

import androidx.work.Worker
import androidx.work.toWorkData
import com.google.gson.Gson
import com.legocatalog.LegoCatalogApp
import com.legocatalog.data.local.SetEntity
import com.legocatalog.data.remote.model.ErrorResponse
import com.legocatalog.data.remote.model.LegoPartsWrapper
import com.legocatalog.data.remote.model.LegoSet
import com.legocatalog.data.remote.rebrickable.RebrickableService
import com.legocatalog.data.repository.Repository
import retrofit2.Response
import javax.inject.Inject

class SetInfoWorker: Worker() {

    companion object {
        val NUMBER_KEY = "number_key"
        val ERROR_MESSAGE_KEY = "error_message"
    }

    @Inject
    lateinit var rebrickableService: RebrickableService

    @Inject
    lateinit var repository: Repository

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
                val setId = saveSet(response)
                if (setId != null) {
                    val result = fetchSetParts(setId, number)
                    if (result) {
                        handleSuccess(response)
                        return Result.SUCCESS
                    } else {
                        handleError(response)
                        return Result.FAILURE
                    }
                } else {
                    handleError("Unable to save set")
                    return Result.FAILURE
                }
            }
            else -> {
                handleError(response)
                return Result.FAILURE
            }
        }
    }

    private fun fetchSetParts(setId: Long, number: String): Boolean {
        val response = rebrickableService.setParts(number).execute()
        return when (response.isSuccessful) {
            true -> {
                saveParts(setId, response)
                true
            }
            else -> false
        }
    }

    private fun saveSet(response: Response<LegoSet>): Long? {
        return response.body()?.let {
            repository.addSet(it)
        }
    }

    private fun saveParts(setId:Long, response: Response<LegoPartsWrapper>) {
        response.body()?.let {
            repository.addParts(setId, it)
        }
    }

    private fun handleError(response: Response<LegoSet>) {
        val errorMessage = response.errorBody()?.string()
        errorMessage?.let {
            val errorModel = Gson().fromJson(it, ErrorResponse::class.java)
            outputData = mapOf(ERROR_MESSAGE_KEY to errorModel.detail).toWorkData()
        }
    }

    private fun handleError(message: String) {
        outputData = mapOf(ERROR_MESSAGE_KEY to message).toWorkData()
    }

    private fun handleSuccess(response: Response<LegoSet>) {
        response.body()?.let {
            val set = SetEntity.fromLegoSet(it)
            outputData = set.toMap().toWorkData()
        }
    }
}