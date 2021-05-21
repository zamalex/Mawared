package app.mawared.alhayat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    AlertDialog alertDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_blank, container, false);
        webView = v.findViewById(R.id.webv);
        if (getActivity()!=null)

            ((MainActivity) getActivity()).showDialog(false);


         alertDialog = new AlertDialog.Builder(getActivity())
//set title
//set message
                 .setView(R.layout.loadind_dialog)
               // .setMessage("برجاء الانتظار وسيتم تحويلك تلقائيا")
//set positive button
                .setCancelable(false)
                .create();


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

                Log.e("console","message is "+cmsg.message());

                try {
                    JSONObject jsonObject = new JSONObject(cmsg.message());
                    System.out.println("json is " + cmsg.message());
                    int status = Integer.parseInt(jsonObject.getString("status"));
                    boolean success = (boolean) jsonObject.getBoolean("success");

                    if (status == 200 && success) {

                   //     Toast.makeText(getActivity(), "status is success " + status, Toast.LENGTH_SHORT).show();

                        ((MainActivity)getActivity()).setPaymentSuccess(true);

                        ((MainActivity)getActivity()).fragmentStack.replace(new OrderDoneFragment());



                    } else {
                     //   Toast.makeText(getActivity(), "status is failed " + status, Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).setPaymentSuccess(false);


                        ((MainActivity)getActivity()).fragmentStack.pop();

                    }

                } catch (JSONException e) {

                }


                return true;

            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

               // Log.d("webb ", "shouldOverrideUrlLoading " + request.getUrl().toString());

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               // Log.d("webb ", "shouldOverrideUrlLoading " + url);

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.contains("handle-payment-response"))
                webView.setVisibility(View.INVISIBLE);
                Log.d("webb ", "onPageStarted " + url);
                if (url.equals("http://www.mawared.badee.com.sa/api/v1/payment/return")||url.contains(".payfort.com/FortAPI/general/backToMerchant")) {
                  //  Toast.makeText(getActivity(), "please wait", Toast.LENGTH_SHORT).show();
                    showDialog();
                    //startActivity(new Intent(getActivity(),MainActivity.class));
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

               // Log.d("webb ", "onReceivedError " + error.getDescription() + error.getErrorCode());

            }


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                //Log.d("requestto", request.getMethod() + " " + request.getUrl() + " " + request.getRequestHeaders().toString());


                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //   Toast.makeText(getActivity(), "is "+url, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getActivity(), "web "+view.toString(), Toast.LENGTH_LONG).show();

                Log.d("webb ", "onPageFinished " + url);
                Log.d("webb ", "webview " + view.toString());
                if (url.equals("http://www.mawared.badee.com.sa/api/v1/payment/return")||url.contains(".payfort.com/FortAPI/general/backToMerchant")) {
                    //  Toast.makeText(getActivity(), "please wait", Toast.LENGTH_SHORT).show();
                    showDialog();
                    //startActivity(new Intent(getActivity(),MainActivity.class));
                }

                Log.e("console","url is "+url);
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

    @Override
    public void onStop() {
        super.onStop();
        dismissDialog();
    }
    void showDialog(){
        if (alertDialog!=null){
            if (!alertDialog.isShowing())
                alertDialog.show();
        }
    }
    void dismissDialog(){
        if (alertDialog!=null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
    }
}