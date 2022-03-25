package vsee.ndt.news.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import vsee.ndt.news.databinding.NewsFragmentBinding
import vsee.ndt.news.models.News
import vsee.ndt.news.viewmodels.MainViewModel


class NewsFragment : Fragment() {
    private lateinit var binding: NewsFragmentBinding
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsFragmentBinding.inflate(inflater, container, false)

        webView = binding.wbContent
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewModel = activity?.run {
            ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.selectedItem.observe(viewLifecycleOwner, object : Observer<News> {
            override fun onChanged(news: News) {
                webView.loadUrl(news.url)
            }
        })
    }
}