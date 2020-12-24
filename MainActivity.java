package com.example.xui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * @programName: DBOpenHelper.java
 * @programFunction: database helper class
 * @createDate: 2018/09/29
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/29   1.00   AnneHan   New Create
 */
class DBOpenHelper extends SQLiteOpenHelper {
    public static final String Student = "create table user(" +
            "userID varchar(20) not null primary key," +
            "pwd varchar(30) not null)";
    public DBOpenHelper(Context context,String name, CursorFactory factory,
                        int version){
        super(context, name, factory, version);
    }

    @Override
    //首次创建数据库的时候调用，一般可以执行建库，建表的操作
    //Sqlite没有单独的布尔存储类型，它使用INTEGER作为存储类型，0为false，1为true
    public void onCreate(SQLiteDatabase db){
        //user table
        db.execSQL(Student);
        ContentValues values= new ContentValues();
        values.put("userID","594859751");
        values.put("pwd","nwg666");
        long rowid = db.insert("user",null,values);
    }

    @Override//当数据库的版本发生变化时，会自动执行
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
public class MainActivity extends AppCompatActivity {
    private EditText ed1,ed2;
    private Button confirBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick_Event(View view){
        EditText editText=(EditText)findViewById(R.id.et_id);
        String userid=editText.getText().toString();
        EditText editText2=(EditText)findViewById(R.id.et_password);
        String password=editText2.getText().toString();
        DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        //根据画面上输入的账号/密码去数据库中进行查询（user_tb是表名）
        Cursor c = db.query("user",null,"userID=? and pwd=?",new String[]{userid,password},null,null,null);
        //如果有查询到数据
        if(c!=null && c.getCount() >= 1){
            //可以把查询出来的值打印出来在后台显示/查看
        /*String[] cols = c.getColumnNames();
        while(c.moveToNext()){
            for(String ColumnName:cols){
                Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
            }
        }*/
            c.close();
            db.close();
            Toast.makeText(MainActivity.this, "登录成功！", LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Person.class);
            MainActivity.this.startActivity(intent);
            this.finish();
        }
        //如果没有查询到数据
        else{
            Toast.makeText(MainActivity.this, "用户名或密码错误！", LENGTH_SHORT).show();
        }
    }
    public void OnMyRegistClick(View v){
        //对用户输入的值的格式进行判断的处理...
        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        EditText editText=(EditText)findViewById(R.id.et_id);
        String userid=editText.getText().toString();
        EditText editText2=(EditText)findViewById(R.id.et_password);
        String password=editText2.getText().toString();
        //根据画面上输入的账号去数据库中进行查询
        Cursor c = db.query("user_tb",null,"userID=?",new String[]{userid},null,null,null);
        //如果有查询到数据，则说明账号已存在
        if(c!=null && c.getCount() >= 1){
            Toast.makeText(this, "该用户已存在", LENGTH_SHORT).show();
            c.close();
        }
        //如果没有查询到数据，则往数据库中insert一笔数据
        else{
            //insert data
            ContentValues values= new ContentValues();
            values.put("userID","594859751");
            values.put("pwd","nwg666");
            long rowid = db.insert("user_tb",null,values);
            Toast.makeText(this, "注册成功", LENGTH_SHORT).show();//提示信息
            this.finish();
        }
        db.close();
    }
}
