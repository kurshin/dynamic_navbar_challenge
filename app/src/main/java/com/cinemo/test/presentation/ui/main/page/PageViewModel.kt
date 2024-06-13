package com.cinemo.test.presentation.ui.main.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinemo.test.domain.Item

class PageViewModel : ViewModel() {

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> get() = _item

    fun setItem(item: Item) {
        _item.value = item
    }
}