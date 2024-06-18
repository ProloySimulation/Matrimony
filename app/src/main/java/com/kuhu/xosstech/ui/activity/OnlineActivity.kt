package com.kuhu.xosstech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuhu.xosstech.R
import com.kuhu.xosstech.data.Message
import com.kuhu.xosstech.network.ApiConfig
import com.kuhu.xosstech.ui.adapter.MessageAdapter
import com.kuhu.xosstech.util.SharedPreferencesManager
import com.kuhu.xosstech.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.activity_online.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class OnlineActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClient
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var messageAdapter: MessageAdapter

    private val viewModel: ChatViewModel by lazy {
        ChatViewModel(ApiConfig.BASE_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)

        // Initialize SharedPreferencesManager
        sharedPreferences = SharedPreferencesManager(this)
        recyclerChat.layoutManager = LinearLayoutManager(this)
        val receivedData = intent.getIntExtra("USER_ID",0)

        // Initialize the adapter and set it to the RecyclerView
        messageAdapter = MessageAdapter(2)
        recyclerChat.adapter = messageAdapter

        imvSend.setOnClickListener {
            val message = etMessage.text.toString()
            val updatedFields = mutableMapOf<String, String>()

            if (message.isNotEmpty()) {
                updatedFields["message_content"] = message
                updatedFields["receiver_id"] = receivedData.toString()
            }

            viewModel.messageSend("Bearer " + sharedPreferences.getLoginToken().toString(), updatedFields)
            etMessage.text = null
        }

        // Set up WebSocket connection
        setUpWebSocketConnection()

        viewModel.messageList.observe(this, Observer { message ->
            // Update UI with the list of messages received from the server
            messageAdapter.submitList(message)
        })

        viewModel.messageList("Bearer " + sharedPreferences.getLoginToken().toString(), receivedData)
    }

    private fun setUpWebSocketConnection() {
        val serverUri = URI("wss://96b2-103-149-143-144.ngrok-free.app//api/get-message")
        webSocketClient = object : WebSocketClient(serverUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("WebSocket", "Connection established")
            }

            override fun onMessage(text: String) {
                // Handle incoming messages from the server
                // Update UI with received message
                // Assuming text contains the message received from the server
                val receivedMessage = Message(messageContent = text)

                // Add the received message to the adapter
                messageAdapter.addMessage(receivedMessage)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                // WebSocket connection is closed
            }

            override fun onError(ex: Exception?) {
                Log.e("WebSocket", "Error occurred: ${ex?.message}")
            }
        }
        webSocketClient.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close WebSocket connection when activity is destroyed
        webSocketClient.close()
    }
}