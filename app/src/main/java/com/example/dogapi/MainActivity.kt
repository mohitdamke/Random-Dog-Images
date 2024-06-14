package com.example.dogapi

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.dogapi.ui.theme.DogApiTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class MainActivity : ComponentActivity() {
    lateinit var api: DogApi
    private val dogImage = mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogApiTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),

                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "DOG IMAGES",
                            modifier = Modifier.padding(bottom = 25.dp),
                            fontSize = 30.sp,
                            fontWeight = W600
                        )
                        Text(
                            text = "Click to get a new image",
                            modifier = Modifier.padding(bottom = 30.dp),
                            fontSize = 26.sp,
                            fontWeight = W600
                        )
                        AsyncImage(
                            model = dogImage.value,
                            contentDescription = "",
                            modifier = Modifier
                                .clickable { sendResponse(application) }
                                .size(300.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun sendResponse(
        app: Application
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitModel.api.getDogResult()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        app,
                        "HTTP error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        app,
                        "App error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    dogImage.value = response.body()!!.message
                }
            }
        }
    }
}
