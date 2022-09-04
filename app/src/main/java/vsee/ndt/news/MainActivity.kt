package vsee.ndt.news

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import vsee.ndt.news.databinding.MainActivityBinding
import vsee.ndt.news.ui.main.MainFragment
import vsee.ndt.news.ui.main.OnDataPass


class MainActivity : AppCompatActivity(), OnDataPass {

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

    override fun onDataPass(data: String) {
        Log.d("NDT", "Data passed: $data")
        for(i in 1..10) {
            Log.d("NDT", "aaaa $i")
        }
    }
}