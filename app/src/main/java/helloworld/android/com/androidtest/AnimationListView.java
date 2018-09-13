package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AnimationListView extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);

        List<String> lisData = new ArrayList<>();
        lisData.add("10010");
        lisData.add("10086");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lisData);
        listView.setAdapter(adapter);

        // 添加动画
        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
        animation.setDuration(1000);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation,0);
        listView.setLayoutAnimation(layoutAnimationController);
    }
}
