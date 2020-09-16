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
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "\t<title>Payment redirect page</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\t<form method=\"post\" action=\"https://checkout.payfort.com/FortAPI/paymentPage\" id=\"frm\" name=\"frm\">\n" +
                "\t\t<input type=\"hidden\" name=\"command\" value=\"PURCHASE\">\n" +
                "\t\t<input type=\"hidden\" name=\"access_code\" value=\"RKu6Ec8Qj9rPvw4RHZ7n\">\n" +
                "\t\t<input type=\"hidden\" name=\"merchant_identifier\" value=\"hMLmqaLh\">\n" +
                "\t\t<input type=\"hidden\" name=\"merchant_reference\" value=\"ORDR.1599729812\">\n" +
                "\t\t<input type=\"hidden\" name=\"amount\" value=\"13800\">\n" +
                "\t\t<input type=\"hidden\" name=\"currency\" value=\"SAR\">\n" +
                "\t\t<input type=\"hidden\" name=\"language\" value=\"ar\">\n" +
                "\t\t<input type=\"hidden\" name=\"customer_email\" value=\"Moody@gmil.com\">\n" +
                "\t\t<input type=\"hidden\" name=\"return_url\" value=\"http://mawared.badee.com.sa/api/v1/payment/return\">\n" +
                "\t\t<input type=\"hidden\" name=\"signature\" value=\"8840fa581ec15221d1519effc46c478bbf05890a236ce73b80fa3510f8f063a7\">\n" +
                "\t\t<button type=\"submit\">Click here to proceed to payment if you are not automatically redirected</button>\n" +
                "\t</form>\n" +
                "\t<script>\n" +
                "\t\twindow.onload = function () {\n" +
                "        document.frm.submit();\n" +
                "    };\n" +
                "\t</script>\n" +
                "</body>\n" +
                "\n" +
                "</html>";


        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.setWebViewClient(new WebViewClient() {
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
        });
        return v;
    }
}