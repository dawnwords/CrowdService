package edu.fudan.se.crowdservice.core.dui;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.util.IOUtil;

import java.io.File;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class ImageInputView extends KeyValueView<byte[]> {

    private LayoutParams previewLayout;

    private String imagePath;
    private TextView description;
    private TakePicView preview;
    private ImageView imageView;

    public ImageInputView(Context context, KeyValueHolder<byte[]> holder) {
        super(context, holder);
    }

    @Override
    protected void render(String key, byte[] value) {
        setOrientation(VERTICAL);

        Button takePic = new Button(getContext());
        takePic.setText("Take Picture");
        takePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                takePic();
            }
        });

        previewLayout = new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(500));

        description = new TextView(getContext());
        description.setText(key);

        LinearLayout wrapper = new LinearLayout(getContext());
        wrapper.addView(description, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        wrapper.addView(takePic, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7));

        addView(wrapper, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private int dp2px(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().densityDpi;
        return (int) (dp * (scale / 160));
    }

    private void takePic() {
        if (preview == null) {
            preview = new TakePicView(getContext());
            if (imageView != null) {
                removeView(imageView);
                imageView = null;
            }
            addView(preview, previewLayout);
        } else {
            preview.takePicture(new OnPictureTakenListener() {
                @Override
                public void onPictureTaken(String imgPath) {
                    ImageInputView.this.imagePath = imgPath;
                    if (preview != null) {
                        removeView(preview);
                        preview = null;
                    }
                    if (imageView == null) {
                        imageView = new ImageView(getContext());
                    }
                    imageView.setImageURI(Uri.fromFile(new File(imgPath)));
                    addView(imageView, previewLayout);
                }
            });
        }
    }

    @Override
    public boolean needSubmit() {
        return true;
    }

    @Override
    public boolean isReady() {
        return imagePath != null;
    }

    @Override
    public KeyValueHolder<byte[]> submit() {
        return new ImageDisplay(description.getText().toString(), imagePath);
    }

    interface OnPictureTakenListener {
        void onPictureTaken(String imgPath);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    class TakePicView extends View implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {
        private Camera camera;
        private SurfaceView preview;
        private OnPictureTakenListener listener;

        public TakePicView(Context context) {
            super(context);
            preview = new SurfaceView(context);
        }

        public void startPreview() {
            camera = Camera.open();
        }

        public void takePicture(OnPictureTakenListener listener) {
            camera.takePicture(this, null, null, this);
            this.listener = listener;
        }

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            listener.onPictureTaken(IOUtil.saveByteArray(bytes));
            camera.stopPreview();
        }

        @Override
        public void onShutter() {
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                camera.setPreviewDisplay(preview.getHolder());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Camera.Parameters params = camera.getParameters();
            Camera.Size selected = params.getSupportedPreviewSizes().get(0);
            params.setPreviewSize(selected.width, selected.height);
            camera.setParameters(params);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }
}

