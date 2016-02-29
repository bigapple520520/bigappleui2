package com.xuan.bigappleui.demo.view.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.utils.BUToastUtil;
import com.xuan.bigappleui.lib.view.webview.BUWebView;
import com.xuan.bigappleui.lib.view.webview.core.BUWebChromeClient;
import com.xuan.bigappleui.lib.view.webview.jscall.JsCallback;
import com.xuan.bigappleui.lib.view.webview.progress.BUWebViewProgressBar;

import java.util.HashMap;

public class BUWebViewDemo extends Activity {
	private BUWebView webView;
	private Button button;
	private BUWebViewProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_anwebview);

		webView = (BUWebView)findViewById(R.id.webView);
		//webView.loadUrl("file:///android_asset/index.html");
		webView.loadUrl("http://www.baidu.com");

		webView.setBuJsCallback(new JsCallback() {
			@Override
			public void call(String method, HashMap<String, String> params) {
				if ("alert".equals(method)) {
					showMessage(params.get("a"));
				} else if ("init".equals(method)) {
					showMessage(params.get("a"));
				}
			}
		});
		webView.setBuWebChromeClient(new BUWebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				BUToastUtil.displayTextShort(title);
			}
		});

		progressBar = (BUWebViewProgressBar)findViewById(R.id.progressBar);
		webView.setBuProgress(progressBar);

		button = (Button)findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.callJs("我是JS被调用");
			}
		});
	}

	private void showMessage(String message){
		new AlertDialog.Builder(this).setMessage(message).show();
	}

}
