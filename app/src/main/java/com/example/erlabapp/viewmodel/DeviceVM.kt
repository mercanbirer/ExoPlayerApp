package com.example.erlabapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.erlabapp.repository.DeviceRepository
import com.example.erlabapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceVM @Inject constructor(
    deviceRepository: DeviceRepository
) : ViewModel() {

    val getResultFromJNIForRAuth: MutableLiveData<Resource<String>> =
        deviceRepository.getGeneratedRAuthJNI().asLiveData() as MutableLiveData<Resource<String>>

}
