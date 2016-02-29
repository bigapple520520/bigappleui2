package com.xuan.bigappleui.demo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.demo.view.webview.BUWebViewDemo;

/**
 * Created by xuan on 16/2/29.
 */
public class ViewDemoMain extends Activity {
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_view_main);
        content = (LinearLayout) findViewById(R.id.content);

        addButton("Webview", BUWebViewDemo.class);
    }

    private void addButton(String text, final Class<?> clazz) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ViewDemoMain.this, clazz);
                startActivity(intent);
            }
        });
        content.addView(button);
    }

}
