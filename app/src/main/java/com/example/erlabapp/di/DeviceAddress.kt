package com.example.erlabapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.net.NetworkInterface
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceAddress {

    @Singleton
    @Provides
    @Named("mac")
    fun getMacAddress(): String {
        return try {
            val network = NetworkInterface.getByName("wlan0")
            val mac = network.hardwareAddress
            val sb = StringBuilder()
            for (i in mac.indices) {
                sb.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) "-" else ""))
            }
            sb.toString()
        } catch (E: Exception) {
            System.err.println("MAC Exception: " + E.message)
            return "02:00:00:00:00:00"
        }
    }
}
