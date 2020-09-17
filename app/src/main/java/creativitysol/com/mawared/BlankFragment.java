package creativitysol.com.mawared;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class BlankFragment extends Fragment {

    View v;
        WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_blank, container, false);
        webView = v.findViewById(R.id.webv);



        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = getArguments().getString("html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");



       /* webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Log.d("webb ","res "+request.getUrl().toString());

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("webb ","urlll "+url);

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("webb ","started "+url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                Log.d("webb ","error "+error.getDescription()+ error.getErrorCode());

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //   Toast.makeText(getActivity(), "is "+url, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getActivity(), "web "+view.toString(), Toast.LENGTH_LONG).show();

                Log.d("webb ","url "+url);
                Log.d("webb ","webview "+view.toString());
            }
        });*/
        return v;
    }
}