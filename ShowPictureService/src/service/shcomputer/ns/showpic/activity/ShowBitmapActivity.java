package service.shcomputer.ns.showpic.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import edu.fudan.se.crowdservice.core.ServiceActivity;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class ShowBitmapActivity extends ServiceActivity {
    public static final String BITMAP = "BITMAP";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String path = getInputParameterBundle().getString(BITMAP);
        try {
            Button back = new Button(getContext());
            back.setText("Back");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish(null);
                }
            });

            Bitmap pic = BitmapFactory.decodeStream(new FileInputStream(path));
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(pic);


            LinearLayout all = new LinearLayout(getContext());
            all.setOrientation(LinearLayout.VERTICAL);
            all.addView(back, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            all.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            setContentView(all);
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }

    }
}
