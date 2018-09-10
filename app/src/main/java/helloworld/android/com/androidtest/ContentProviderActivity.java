package helloworld.android.com.androidtest;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ContentProviderActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);
    }

    // 获取联系人列表
    public void getContacts(View view){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()){
            String s1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            builder.append(s1);
            builder.append("\r\n");
        }

        EditText editText = findViewById(R.id.editText);
        editText.setText(builder.toString());
    }
}
