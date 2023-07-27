package ua.cn.stu.navcomponent.tabs.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.cn.stu.navcomponent.tabs.Repositories
import ua.cn.stu.navcomponent.tabs.model.Boook.Usermaill
import ua.cn.stu.navcomponent.tabs.model.accounts.AccountsRepository
import ua.cn.stu.navcomponent.tabs.utils.share
var usermaiil: String = ""

class MainActivityViewModel(
    private val accountsRepository: AccountsRepository,
) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username = _username.share()
    val mainActivity = Usermaill()
    init {
        viewModelScope.launch {
            // listening for the current account and send the username to be displayed in the toolbar
            accountsRepository.getAccount().collect {
                if (it == null) {
                    _username.value = ""
                } else {
                    _username.value = "@${it.username}"
                    usermaiil = "@${it.email}"
                }
            }
        }
    }
}