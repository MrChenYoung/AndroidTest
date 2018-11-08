package com.androidTest.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidTest.R;

public class ViewPagerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
    }

    // 普通ViewPager
    public void nomalClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择样式");
        String[] types = {"none","ScalTransformer", "RotateTransformer"};
        builder.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ViewPagerActivity.this,ViewPagerNomalActivity.class);
                if (which == 0){
                    intent.putExtra("type","none");
                }else if(which == 1) {
                    intent.putExtra("type","scal");
                }else {
                    intent.putExtra("type","rotate");
                }
                startActivity(intent);
            }
        });
        builder.show();
    }

    // 3D画廊样式
    public void gallery(View view){
        Intent intent = new Intent(ViewPagerActivity.this,ViewPagerGalleryActivity.class);
        startActivity(intent);
    }

    // Fragment page
    public void fragmentPage(View view){
        Intent intent = new Intent(ViewPagerActivity.this,ViewPagerFragmentActivity.class);
        startActivity(intent);
    }
}
