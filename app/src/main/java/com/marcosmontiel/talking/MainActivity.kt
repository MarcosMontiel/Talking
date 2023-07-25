package com.marcosmontiel.talking

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.marcosmontiel.talking.ui.theme.TalkingTheme
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var _tts: TextToSpeech

    private var _text: String by mutableStateOf("")
    private var _message: String by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            TalkingTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Greeting(
                        message = _message,
                        text = _text,
                        onTextChange = {
                            _text = it
                        },
                        onClick = {
                            speak()
                        }
                    )
                }

            }

        }

        _tts = TextToSpeech(this, this)

    }

    override fun onDestroy() {
        if (::_tts.isInitialized) {
            _tts.stop()
            _tts.shutdown()
        }

        super.onDestroy()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            _message = "Device is ready to speak"
            _tts.language = Locale("ES")

        } else {

            _message = "Device is not ready to speak, please try again later"

        }

    }

    private fun speak() {
        _tts.speak(_text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}

@Composable
fun Greeting(message: String, text: String, onTextChange: (String) -> Unit, onClick: () -> Unit) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = message)

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {

                    if (text.isEmpty()) {

                        Toast.makeText(context, "Insert a message to speak", Toast.LENGTH_LONG)
                            .show()

                    } else {

                        onClick()

                    }

                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Speak")
            }

        }
    }
}
