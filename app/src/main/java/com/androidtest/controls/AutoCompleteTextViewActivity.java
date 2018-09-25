package com.androidtest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import helloworld.android.com.androidtest.R;

public class AutoCompleteTextViewActivity extends Activity {
    private String[] data = {
            "hello Android",
            "hello iOS",
            "hello python",
            "hello php",
            "hello java"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocompletetextview);

        // AutoCompleteTextView
        AutoCompleteTextView textView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,data);
        textView.setAdapter(adapter);


        // MultiAutoCompleteTextView
        MultiAutoCompleteTextView multiAutoCompleteTextView = findViewById(R.id.multiAutoCompleteTextView);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        // 指定在哪个视图下弹出提示
//        multiAutoCompleteTextView.setDropDownAnchor(R.id.autoCompleteTextV iew);
        multiAutoCompleteTextView.setAdapter(adapter);

    }
}
