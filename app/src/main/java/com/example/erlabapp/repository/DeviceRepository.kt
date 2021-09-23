package com.example.erlabapp.repository

import com.example.erlabapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class DeviceRepository {

    fun getGeneratedRAuthJNI(): Flow<Resource<String>> {
        return flow {
            val getResultFromJNI = generateRAuthJNI("123456789123", "123456789123", "1618996116")
            if (getResultFromJNI != null) {
                emit(Resource.success(getResultFromJNI))
            } else {
                emit(Resource.error("empty value", null, null))
            }
        }.catch {
            emit(Resource.error("Something went wrong", null, it))
        }.flowOn(Dispatchers.IO)
    }

    private external fun generateRAuthJNI(mac: String?, serial: String?, rTime: String?): String?

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
                System.loadLibrary("native-lib")
        }
    }

}
