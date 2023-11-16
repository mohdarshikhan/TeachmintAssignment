package com.mak.app.teachmintassignment.data.remote

import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AppApis {

    // https://api.github.com/search/repositories?q=Q
    @POST("/search/repositories")
    @Headers(
        "X-GitHub-Api-Version: 2022-11-28",
        "Accept: application/vnd.github+json",
        "Authorization: Bearer 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"
    )
    suspend fun getRepoList(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): RepoListResponse
}