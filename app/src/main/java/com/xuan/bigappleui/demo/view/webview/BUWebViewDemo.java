package com.xuan.bigappleui.demo.view.webview;

import android.os.Bundle;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.view.webview.BUWebView;

public class BUWebViewDemo extends BPActivity {
	@InjectView(R.id.webView)
	private BUWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_anwebview);
		webView.getWebView().loadUrl("http://www.baidu.com");
	}

}
