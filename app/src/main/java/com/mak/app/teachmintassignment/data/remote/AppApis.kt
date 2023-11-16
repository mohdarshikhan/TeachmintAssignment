package com.mak.app.teachmintassignment.data.remote

import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AppApis {

    // https://api.github.com/search/repositories?q=Q
    @GET("/search/repositories")
    suspend fun getRepoList(
        @Header("Authorization") token: String,
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): RepoListResponse
}