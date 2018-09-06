package helloworld.android.com.androidtest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SqliteActivity extends Activity {

    private SqliteMyDataBase db;
    private EditText nameEditText;
    private EditText ageEditText;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        nameEditText = findViewById(R.id.editText1);
        ageEditText = findViewById(R.id.editText2);
        listView = findViewById(R.id.listView);
        adapter = new SimpleCursorAdapter(this,R.layout.activity_sqlitelistitem,cursor,new String[]{"name","age"},new int[]{R.id.nameTextView,R.id.ageTextView});
        listView.setAdapter(adapter);

        db = new SqliteMyDataBase(this);

        readData(null);
    }

    // 存数据到数据库
    public void saveData(View view){
        SQLiteDatabase dataBase = db.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",nameEditText.getText().toString());
        contentValues.put("age",ageEditText.getText().toString());
        dataBase.insert("person",null,contentValues);
        dataBase.close();

        nameEditText.setText("");
        ageEditText.setText("");
    }

    // 从数据库读取数据
    public void readData(View view){
        StringBuilder builder = new StringBuilder();
        SQLiteDatabase database = db.getReadableDatabase();

        cursor = database.query("person",null,null,null,null,null,null);
        adapter.changeCursor(cursor);

//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String age = cursor.getString(cursor.getColumnIndex("age"));
//
//            builder.append(name + "\n");
//            builder.append(age + "\n");
//        }
    }

}
