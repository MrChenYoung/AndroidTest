package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SurfaceViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SurfaceViewTest(this));
    }
}
