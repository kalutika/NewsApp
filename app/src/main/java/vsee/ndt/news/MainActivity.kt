package vsee.ndt.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vsee.ndt.news.databinding.MainActivityBinding
import vsee.ndt.news.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}