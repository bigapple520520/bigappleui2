package com.xuan.bigappleui.demo.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.dialog.BUAlertDialog;
import com.xuan.bigappleui.lib.dialog.BUConfirmDialog;
import com.xuan.bigappleui.lib.dialog.BUPromptDialog;
import com.xuan.bigappleui.lib.utils.BUToastUtil;

/**
 * 自定义对话框测试
 * 
 * @author xuan
 * 
 */
public class DemoDialogActivity extends Activity {
	private LinearLayout content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_dialog);
		content = (LinearLayout)findViewById(R.id.content);

		addButton("alert", new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BUAlertDialog builder = new BUAlertDialog.Builder(
						DemoDialogActivity.this).setTitle("alert")
						.setMessage("ok message").setbuttonText("OK")
						.setOnButtonListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								BUToastUtil.displayTextLong("Click OK!!!");
							}
						}).create();
				builder.show();
			}
		});

		addButton("confirm", new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BUConfirmDialog confirm = new BUConfirmDialog.Builder(
						DemoDialogActivity.this)
						.setTitle("confirm")
						.setMessage("confirm message!!!")
						.setLeftBtnText("Left Btn")
						.setOnLeftBtnListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								BUToastUtil.displayTextShort(
										"Click Left Btn!!!");
							}
						}).setRightBtnText("Right Btn")
						.setOnRightBtnListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								BUToastUtil.displayTextShort(
										"Click Right Btn!!!");
							}
						}).create();
				confirm.show();
			}
		});

		addButton("prompt", new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BUPromptDialog prompt = new BUPromptDialog.Builder(
						DemoDialogActivity.this)
						.setTitle("prompt")
						.setMessage("prompt message!!!")
						.setLeftBtnText("Left Btn")
						.setOnLeftBtnListener(new BUPromptDialog.PromptDialogListener() {
							@Override
							public void onClick(View view, String inputText) {
								BUToastUtil.displayTextShort(
										"Click Left Btn!!!Say:" + inputText);
							}
						}).setRightBtnText("Right Btn")
						.setOnRightBtnListener(new BUPromptDialog.PromptDialogListener() {
							@Override
							public void onClick(View view, String inputText) {
								BUToastUtil.displayTextShort(
										"Click Right Btn!!!Say:" + inputText);
							}
						}).create();
				prompt.show();
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
