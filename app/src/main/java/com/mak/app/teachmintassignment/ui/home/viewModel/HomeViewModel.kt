package com.mak.app.teachmintassignment.ui.home.viewModel

import androidx.lifecycle.viewModelScope
import com.mak.app.teachmintassignment.base.BaseViewModel
import com.mak.app.teachmintassignment.data.ApiResult
import com.mak.app.teachmintassignment.domain.repo.model.Items
import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import com.mak.app.teachmintassignment.domain.repo.usecase.RepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repoUseCase: RepoUseCase
) : BaseViewModel() {

    private val _RepoResponse = MutableStateFlow(RepoListResponse())
    val repoResponse = _RepoResponse.asStateFlow()

    private val _RepoListResponse = MutableStateFlow<ArrayList<Items>>(ArrayList())
    val repoListResponse = _RepoListResponse.asStateFlow()

    fun getRepoList(token: String, q: String, page: Int, oldData: ArrayList<Items>) {
        repoUseCase(token, q, page).onEach { result ->
            when (result) {
                is ApiResult.Loading -> {
                    showProgressFlow.emit(true)
                }

                is ApiResult.Success -> {
                    hideProgressFlow.emit(true)
                    _RepoResponse.emit(result.data)
//                    _RepoListResponse.emit(result.data.items)

                    oldData.addAll(result.data.items)

                    _RepoListResponse.emit(oldData)

                }

                is ApiResult.Error -> {
                    hideProgressFlow.emit(true)

                    errorFlow.emit(result.error.message)
                }
            }
        }.launchIn(viewModelScope)
    }
}