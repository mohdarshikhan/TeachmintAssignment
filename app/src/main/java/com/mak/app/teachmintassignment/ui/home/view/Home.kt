package com.mak.app.teachmintassignment.ui.home.view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.mak.app.teachmintassignment.BuildConfig
import com.mak.app.teachmintassignment.domain.repo.model.Items
import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import com.mak.app.teachmintassignment.ui.home.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    val repoResponse: State<RepoListResponse> = homeViewModel.repoResponse.collectAsState()
    val repoListResponse: State<ArrayList<Items>> = homeViewModel.repoListResponse.collectAsState()

    val error = homeViewModel.error.collectAsState()
    val showProgress = homeViewModel.showProgress.collectAsState()

    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val searchHistory = remember { mutableStateListOf("") }

    val scrollState = rememberLazyListState()
    var page = 1

    // Observe scroll position
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .collect { index ->
                if (index + 1 >= repoListResponse.value.size - 5) {
                    // Load the next page when the user is close to the end
//                    if (repoResponse.value?.totalCount != null) {
//                        if (repoListResponse.value.size < repoResponse.value?.totalCount!!) {
//                        }
//                    }

                    page += 1
                    homeViewModel.getRepoList(BuildConfig.TOKEN, text, page, repoListResponse.value)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                if (!searchHistory.contains(text)) {
                    searchHistory.add(text)
                }
                active = false
                page = 1
                homeViewModel.getRepoList(BuildConfig.TOKEN, text, page, ArrayList())
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Enter your query")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {
            searchHistory.forEach {
                if (it.isNotEmpty()) {
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it)
                    }
                }
            }

            Divider()
            Text(
                modifier = Modifier
                    .padding(all = 14.dp)
                    .fillMaxWidth()
                    .clickable {
                        searchHistory.clear()
                    },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "clear all history"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = repoListResponse.value,
                itemContent = {
                    RepoListItem(item = it, navController)
                })

            // Add a loading item at the end
            if (repoListResponse.value.isNotEmpty()) {
                item {
                    // Loading indicator or placeholder
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

        }



    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    Home(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListItem(item: Items, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
            val json = Uri.encode(Gson().toJson(item))
            navController.navigate("repoDetail/${json}")
        }

    ) {
        Row {
            item.owner?.avatarUrl?.let { RepoImage(it) }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)) {
                item.name?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
                item.description?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }

            }
        }
    }
}

@Composable
private fun RepoImage(url: String) {
    Image(
        painter = rememberAsyncImagePainter(model = url),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}