package com.laioffer.spotify.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.laioffer.spotify.R
import com.laioffer.spotify.datamodel.Album
import com.laioffer.spotify.datamodel.Section
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(uiState = uiState)
}

@Composable
fun HomeScreenContent(uiState: HomeUiState) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            HomeScreenHeader()
        }

        when {
            uiState.isLoading -> {
                item {
                    LoadingSection(stringResource(id = R.string.screen_loading))
                }

            }
            else -> {
                items(uiState.feed) { item ->
                    AlbumSection(section = item)
                }
            }
        }
    }
}
@Composable
private fun AlbumSection(section: Section) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(section.albums) { item ->
                AlbumCover(item)
            }
        }

    }
}

@Composable
private fun AlbumCover(album: Album) {
    val context = LocalContext.current

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(UserAgentInterceptor())
        .build()

    val imageLoader = ImageLoader.Builder(context)
        .okHttpClient(okHttpClient)
        .build()

    Column {
        Box(modifier = Modifier.size(160.dp)) {
            AsyncImage(
                model = album.cover,
                imageLoader = imageLoader,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = album.name,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 4.dp, start = 2.dp)
                    .align(Alignment.BottomStart),
            )
        }

        Text(
            text = album.artists,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            color = Color.LightGray,
        )
    }
}


@Composable
private fun LoadingSection(text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
    }
}


@Composable
fun HomeScreenHeader() {
    Column {
        Text(
            stringResource(id = R.string.menu_home),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Text(
    //       modifier = Modifier.padding(bottom = 16.dp),
    //     stringResource(id = R.string.menu_home),
    //   style = MaterialTheme.typography.h4,
    // color = Color.White
    // )
}

class UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
            .build()
        return chain.proceed(request)
    }
}