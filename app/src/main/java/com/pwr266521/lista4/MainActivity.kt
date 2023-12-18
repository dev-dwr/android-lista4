package com.pwr266521.lista4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.gson.GsonBuilder
import com.pwr266521.lista4.config.Const.API_BASE
import com.pwr266521.lista4.model.FlickrResponse
import com.pwr266521.lista4.model.Item
import com.pwr266521.lista4.repository.FlickrRepository
import com.pwr266521.lista4.service.FlickrApiService
import com.pwr266521.lista4.ui.theme.lista4Theme
import com.pwr266521.lista4.viewmodel.FlickrViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val apiService = retrofit.create(FlickrApiService::class.java)
        val repository = FlickrRepository(apiService)
        val viewModel = FlickrViewModel(repository)

        setContent {
            lista4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlickrGallery(viewModel)
                }
            }
        }
    }
}

@Composable
fun FlickrGallery(viewModel: FlickrViewModel) {
    val photos: FlickrResponse? = viewModel.photos.value
    val isLoading = viewModel.isLoading.value

    Column {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(photos!!.items.size) { index ->
                    ImageCard(photos.items[index])
                }
            }
        }
    }
}


@Composable
fun ImageCard(image: Item) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(image.link))
                context.startActivity(intent)
            },
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(image.media.m),
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = image.title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "By: ${image.author}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Taken: ${formatDate(image.date_taken)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val output = parser.parse(dateString)
        formatter.format(output)
    } catch (e: Exception) {
        dateString
    }
}
