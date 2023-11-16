package com.mak.app.teachmintassignment.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel: ViewModel() {
    protected val errorFlow = MutableStateFlow<String>("")
    val error = errorFlow.asStateFlow()

    protected val showProgressFlow = MutableStateFlow<Boolean>(false)
    val showProgress = showProgressFlow.asStateFlow()

    protected val hideProgressFlow = MutableStateFlow<Boolean>(false)
    val hideProgress = hideProgressFlow.asStateFlow()
}