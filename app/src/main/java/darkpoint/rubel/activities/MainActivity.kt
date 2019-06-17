package darkpoint.rubel.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import darkpoint.rubel.R

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val application = requireNotNull(this).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }
}
