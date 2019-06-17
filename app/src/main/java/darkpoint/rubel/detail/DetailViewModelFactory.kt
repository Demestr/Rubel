package darkpoint.rubel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val curId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(curId = curId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}