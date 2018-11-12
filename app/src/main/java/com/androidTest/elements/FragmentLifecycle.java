package com.androidTest.elements;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;

import java.util.ArrayList;

public class FragmentLifecycle extends Fragment {

    private LinearLayout mainView;
    private ArrayList<String> lifeList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        String message = "调用了onAttach方法";
        lifeList.add(message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String message = "调用了onCreate方法";
        lifeList.add(message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_lifecycle,null);
        mainView = view.findViewById(R.id.mainView);

        String message = "调用了onCreateView方法";
        lifeList.add(message);
        updateUI();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String message = "调用了onActivityCreated方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onStart() {
        super.onStart();

        String message = "调用了onStart方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onResume() {
        super.onResume();

        String message = "调用了onResume方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onPause() {
        super.onPause();

        String message = "调用了onPause方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onStop() {
        super.onStop();

        String message = "调用了onStop方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        String message = "调用了onDestroyView方法";
        addSubView(message);
        showToast(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String message = "调用了onDestroy方法";
        addSubView(message);
        showToast(message);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        String message = "调用了onDetach方法";
        addSubView(message);
        showToast(message);
    }

    // 刷新界面，显示fragment的生命周期
    private void updateUI(){
        for (String str : lifeList){
            addSubView(str);
            showToast(str);
        }
    }

    // 调用一个生命周期的方法就添加一个指示视图
    public void addSubView(String text){
        View view = getLayoutInflater().inflate(R.layout.activity_activity_sub,null);
        TextView textView = view.findViewById(R.id.tv_name);
        textView.setText(text);
        mainView.addView(view);
    }

    // 显示Toast
    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
