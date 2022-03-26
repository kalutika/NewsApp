package vsee.ndt.news.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import vsee.ndt.news.models.News
import vsee.ndt.news.repositories.MainRepository


class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val articleList = MutableLiveData<List<News>>()
    var job: Job? = null

    val loading = MutableLiveData<Boolean>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        onError("Exception: $exception")
    }

    fun getArticles() {
        job = CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            val response = repository.getResponse()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    articleList.postValue(response.body()?.articleList)
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    private val mutableSelectedItem = MutableLiveData<News>()
    val selectedItem: LiveData<News> get() = mutableSelectedItem

    fun selectItem(news: News) {
        mutableSelectedItem.value = news
    }
}