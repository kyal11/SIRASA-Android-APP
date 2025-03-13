package com.dev.sirasa.data.remote

import io.socket.client.Socket
import android.util.Log
import com.dev.sirasa.data.local.UserModel
import io.socket.client.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URISyntaxException

object WebSocketManager {
    private var socket: Socket? = null
    val bookingUpdateFlow = MutableSharedFlow<Unit>()

    suspend fun connect(getSession: () -> Flow<UserModel>) {
        try {
            val user = getSession().first()
            val token = user.token ?: return

            val options = IO.Options()
            options.extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))

            socket = IO.socket("https://sirasa.teamteaguard.com", options)
            socket?.connect()

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("WebSocket", "Connected to WebSocket Server")
            }

            socket?.on("bookingUpdated") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    Log.d("WebSocket", "Received booking update: $data")
                    CoroutineScope(Dispatchers.IO).launch {
                        bookingUpdateFlow.emit(Unit)
                    }
                }
            }
        } catch (e: URISyntaxException) {
            Log.e("WebSocket", "Error: ${e.localizedMessage}")
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }
}
