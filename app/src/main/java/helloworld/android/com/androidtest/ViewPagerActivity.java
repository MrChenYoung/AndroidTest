package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
}
