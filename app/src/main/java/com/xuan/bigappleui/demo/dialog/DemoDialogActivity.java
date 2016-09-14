package com.xuan.bigappleui.demo.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.dialog.BUDialog;
import com.xuan.bigappleui.lib.dialog.core.BUPromptDialog;
import com.xuan.bigappleui.lib.dialog.core.BUSingleSelectDialog;
import com.xuan.bigappleui.lib.utils.BUToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义对话框测试
 *
 * @author xuan
 */
public class DemoDialogActivity extends Activity {
    private LinearLayout content;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_dialog);
        content = (LinearLayout) findViewById(R.id.content);

        addButton("alert", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BUDialog.getInstance().alert(DemoDialogActivity.this, "真的,你不能这样对我");
            }
        });

        addButton("confirm", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BUDialog.getInstance().confirm(DemoDialogActivity.this, "你好么?我说的你知道了么?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BUToastUtil.displayTextLong("知道了");
                    }
                });
            }
        });

        addButton("prompt", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BUDialog.getInstance().prompt(DemoDialogActivity.this, "输入点东西", new BUPromptDialog.PromptDialogListener() {
                    @Override
                    public void onClick(View view, String inputText) {
                        BUToastUtil.displayTextLong("你输入了:" + inputText);
                    }
                });
            }
        });

        addButton("select", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                List<BUSingleSelectDialog.Item> itemList = new ArrayList<BUSingleSelectDialog.Item>();

                itemList.add(new BUSingleSelectDialog.Item("我是一楼", null));
                itemList.add(new BUSingleSelectDialog.Item("顶一楼", null));
                itemList.add(new BUSingleSelectDialog.Item("二楼SB", null));

                BUDialog.getInstance().select(DemoDialogActivity.this, itemList, new BUSingleSelectDialog.ItemOnClickListener() {
                    @Override
                    public void onClick(BUSingleSelectDialog.Item item, View view) {
                        BUToastUtil.displayTextLong(item.getText());
                    }
                });
            }
        });

        addButton("loadingDialog", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ProgressDialog d = BUDialog.getInstance().getProgressDialogFull(DemoDialogActivity.this, "我在努力的转啊转...");
                d.show();
            }
        });

        addButton("loadingDialogSmall", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final ProgressDialog d = BUDialog.getInstance().getProgressDialogSmall(DemoDialogActivity.this);
                d.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        d.dismiss();
                    }
                }, 3000);
            }
        });
    }

    private void addButton(String text,
                           final Button.OnClickListener onClickListener) {
        Button button = new Button(DemoDialogActivity.this);
        button.setText(text);
        button.setOnClickListener(onClickListener);
        content.addView(button);
    }
}
