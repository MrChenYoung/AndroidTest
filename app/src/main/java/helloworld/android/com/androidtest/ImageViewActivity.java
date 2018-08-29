package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
    int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        final float scWidth = this.getWindowManager().getDefaultDisplay().getWidth();

        final int[] images = {
                R.drawable.a1,
                R.drawable.a2,
                R.drawable.a3,
                R.drawable.a4
        };


        final ImageView imageView = findViewById(R.id.imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = event.getX();
                if (x > scWidth * 0.5) {
                    // 点击屏幕右边
                    index++;
                }else {
                    // 点击屏幕左边
                    index--;
                }

                index = index < 0 ? images.length - 1 : index;
                index = index >= images.length ? 0 : index;

                imageView.setImageResource(images[index]);

                return false;
            }
        });
    }
}
