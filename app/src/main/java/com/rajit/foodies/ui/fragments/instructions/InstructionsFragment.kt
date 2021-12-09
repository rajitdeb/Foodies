package com.rajit.foodies.ui.fragments.instructions

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.snackbar.Snackbar
import com.rajit.foodies.R
import com.rajit.foodies.databinding.FragmentInstructionsBinding
import com.rajit.foodies.models.Result
import com.rajit.foodies.utils.Constants.Companion.RECIPE_RESULT

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val myBundle: Result? = arguments?.getParcelable(RECIPE_RESULT)
        val recipeMainUrl: String? = myBundle?.sourceUrl
        if (recipeMainUrl != null) {
            binding.instructionsWebView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.webViewProgressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.webViewProgressBar.visibility = View.GONE
                }
            }
            binding.instructionsWebView.loadUrl(recipeMainUrl)
        } else {
            Snackbar.make(binding.root, "URL Error: Empty Url", Snackbar.LENGTH_INDEFINITE)
                .setAction("OKAY") {}
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}