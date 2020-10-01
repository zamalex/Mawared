package app.mawared.alhayat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class BlankFragment extends Fragment {

    View v;
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_blank, container, false);
        webView = v.findViewById(R.id.webv);
        ((MainActivity) getActivity()).showDialog(false);


        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = getArguments().getString("html");


        //     webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        webView.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "HtmlViewer");


        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg) {

                /* process JSON */

                try {
                    JSONObject jsonObject = new JSONObject(cmsg.message());
                    System.out.println("json is " + cmsg.message());
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    boolean success = (boolean) jsonObject.getBoolean("success");

                    if (status == 200 && success) {

                        Toast.makeText(getActivity(), "status is success " + status, Toast.LENGTH_SHORT).show();

                        ((MainActivity)getActivity()).setPaymentSuccess(true);

                    } else {
                        Toast.makeText(getActivity(), "status is failed " + status, Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).setPaymentSuccess(false);



                    }
                    ((MainActivity)getActivity()).fragmentStack.pop();

                } catch (JSONException e) {

                }


                return true;

            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Log.d("webb ", "res " + request.getUrl().toString());

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("webb ", "urlll " + url);

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("webb ", "started " + url);
                if (url.equals("http://www.mawared.badee.com.sa/api/v1/payment/return")) {
                    // Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();

                    //startActivity(new Intent(getActivity(),MainActivity.class));
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                Log.d("webb ", "error " + error.getDescription() + error.getErrorCode());

            }


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.d("requestto", request.getMethod() + " " + request.getUrl() + " " + request.getRequestHeaders().toString());


                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //   Toast.makeText(getActivity(), "is "+url, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getActivity(), "web "+view.toString(), Toast.LENGTH_LONG).show();

                Log.d("webb ", "url " + url);
                Log.d("webb ", "webview " + view.toString());
                webView.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");

            }
        });
        return v;
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            System.out.println(html);
        }

    }
}