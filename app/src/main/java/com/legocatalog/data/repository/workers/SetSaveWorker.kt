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
import com.legocatalog.extensions.toSetEntity
import retrofit2.Response
import javax.inject.Inject

class SetSaveWorker: Worker() {

    companion object {
        val NUMBER_KEY = "number_key"
        val THEME_KEY = "theme_key"
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
            val themeId = inputData.getInt(THEME_KEY, -1)
            if (numberKey != null) {
                return fetchSetInfo(numberKey, themeId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.FAILURE
    }

    private fun fetchSetInfo(number: String, themeId: Int): Result {
        val response = rebrickableService.setByNumber(number).execute()
        return when (response.isSuccessful) {
            true -> {
                val setId = saveSet(response, themeId)
                if (setId != null) {
                    val result = fetchSetParts(setId.toInt(), number)
                    if (result) {
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

    private fun fetchSetParts(setId: Int, number: String): Boolean {
        val response = rebrickableService.setParts(number).execute()
        return when (response.isSuccessful) {
            true -> {
                saveParts(setId, response)
                true
            }
            else -> false
        }
    }

    private fun saveSet(response: Response<LegoSet>, themeId: Int): Long? {
        return response.body()?.let {
            it.themeId = themeId
            repository.addSet(it.toSetEntity())
        }
    }

    private fun saveParts(setId: Int, response: Response<LegoPartsWrapper>) {
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
}