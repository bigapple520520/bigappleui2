package com.xuan.bigappleui.demo.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xuan.bigappleui.R;
import com.xuan.bigappleui.lib.camera.CameraUtils;
import com.xuan.bigappleui.lib.utils.BUToastUtil;

/**
 * 相机使用DEMO
 * 
 * @author xuan
 * 
 */
public class CameraDemo extends Activity {
	public static final int CAMERA_CODE = 1;

	private Button button;
	private ImageView imageView;

	private final String dir = Environment.getExternalStorageDirectory()
			.getPath() + "/bigappleuidemo/camera/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_camera);

		button = (Button)findViewById(R.id.button);
		imageView = (ImageView)findViewById(R.id.imageView);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CameraUtils.openCamera(CameraDemo.this, CAMERA_CODE, dir
						+ "temp.jpg");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			BUToastUtil.displayTextShort("拍照取消");
			return;
		}

		switch (requestCode) {
		case CAMERA_CODE:
			imageView
					.setImageBitmap(BitmapFactory.decodeFile(dir + "temp.jpg"));
			break;
		}

	}
}
