package com.mak.app.teachmintassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.mak.app.teachmintassignment.domain.repo.model.Items
import com.mak.app.teachmintassignment.ui.detail.view.RepoDetail
import com.mak.app.teachmintassignment.ui.home.view.Home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavGraph(navController, "home")

        }

    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = "home") {
    NavHost(navController, startDestination) {
        composable(route = "home") { Home(navController) }

        composable(
            route = "repoDetail/{repoJson}",
            arguments = listOf(
                navArgument("repoJson") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val repoJson = backStackEntry.arguments?.getString("repoJson")

            val item = Gson().fromJson(repoJson, Items::class.java)

            RepoDetail(navController, item)
//            RepoDetail(navController, item.owner?.avatarUrl, item.name, item.description, item.url)
        }
    }
}