package com.xuan.bigappleui.demo.view.webview;

import android.app.Activity;
import android.os.Bundle;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.view.webview.BUWebView;

public class BUWebViewDemo extends Activity {
	private BUWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_anwebview);

		webView = (BUWebView)findViewById(R.id.webView);
		webView.getWebView().loadUrl("http://www.baidu.com");
	}

}
