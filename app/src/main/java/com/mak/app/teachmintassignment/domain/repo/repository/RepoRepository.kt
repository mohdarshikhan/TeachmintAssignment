package com.mak.app.teachmintassignment.domain.repo.repository

import com.mak.app.teachmintassignment.data.ApiResult
import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun getRepoList(q: String, page: Int): Flow<ApiResult<RepoListResponse>>

}