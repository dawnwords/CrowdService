package service.shcomputer.ws.select.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import service.shcomputer.ws.select.impl.SelectURLServiceImpl;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class WebViewerActivity extends ServiceActivity {

    private WebView webView;
    private TextView urlView;
    private Button chooseBtn, backBtn, reloadBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initURLView();
        initButtons();
        initWebView();

        LinearLayout header = new LinearLayout(getContext());
        header.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.addView(urlView, new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.64f));
        header.addView(backBtn, new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.12f));
        header.addView(reloadBtn, new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.12f));
        header.addView(chooseBtn, new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.12f));

        LinearLayout all = new LinearLayout(getContext());
        all.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        all.setOrientation(LinearLayout.VERTICAL);
        all.addView(header, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        all.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setContentView(all);

    }

    @Override
    public void onResume() {
        webView.loadUrl(getInputParameterBundle().getString(SelectURLServiceImpl.START_URL));
    }

    private void initButtons() {
        chooseBtn = new Button(getContext());
        chooseBtn.setText("Choose");
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence url = urlView.getText();
                if (url != null) {
                    finish(url.toString());
                }
            }
        });

        backBtn = new Button(getContext());
        backBtn.setText("←");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        reloadBtn = new Button(getContext());
        reloadBtn.setText("Reload");
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
    }

    private void initURLView() {
        urlView = new TextView(getContext());
        urlView.setHint("URL");
    }

    private void initWebView() {
        webView = new WebView(getContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                urlView.setText(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                toast("Error to load%s:%d\n%s", failingUrl, errorCode, description);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
