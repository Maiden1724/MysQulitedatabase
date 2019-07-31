package com.example.mysqulitedatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText mfname,mlname,mId;
    Button msave, mview,mdelete;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfname = findViewById(R.id.edtfname);
        mlname = findViewById(R.id.edtlname);
        mId = findViewById(R.id.edtidno);
        msave =findViewById(R.id.btnsave);
        mview =findViewById(R.id.btnview);
        mdelete = findViewById(R.id.btndelete);


        // cteate database

        db = openOrCreateDatabase("huduma",MODE_PRIVATE,null);

        // create a table in your database


        db.execSQL("CREATE TABLE IF NOT EXISTS citizens(first_name VARCHAR,last_name VARCHAR,id_number INTEGER)");

        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the data from the user
                String fisrtname= mfname.getText().toString();
                String lastname= mlname.getText().toString();
                String idnumber= mId.getText().toString();

                // check if the user is attempting to submitt empty fields

                if (fisrtname.isEmpty()){
                    messages("first name error","please enter the first name");
                }else if (lastname.isEmpty()){
                    messages("last name error","please enter the last name");
                }else if (idnumber.isEmpty()){
                    messages("idnumber error","please enter the id number");
                }else {
                    //proceed to save your received data into db called huduma

                    db.execSQL("INSERT INTO citizens VALUES('"+fisrtname+"','"+lastname+"','"+idnumber+"')");
                    messages("SUCCES","user saved succesfully");
                    //clear input fields for the next entry
                    mfname.setText("");
                    mlname.setText("");
                    mId.setText("");
                }

            }
        });

       mview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // use the cursor to query and select data from your db table

               Cursor cursor = db.rawQuery("SELECT * FROM citizens ",null);
               // check if the cursor found any data in the db

               if (cursor.getCount()==0){
                   messages("empty database","sorry no data was found");


               }else {
                   //proceed to display the selected data
                   //user the string buffer to append the display the records

                   StringBuffer buffer = new StringBuffer();

                   //loop through the selected data that is in your cursor to display
                   while (cursor.moveToNext()){
                       buffer.append(cursor.getString(0)+"\t");//zero is a column fo fname
                       buffer.append(cursor.getString(1)+"\t");//one is a column for lname
                       buffer.append(cursor.getString(2)+"\n");//two is a column for idno
                   }

                   //display your data using the string buffer on the string dialog
                   messages("DATABASE RECORDS",buffer.toString());
               }
           }
       });

       mdelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String id = mId.getText().toString().trim();

               if (id.isEmpty()){
                   messages("ID ERROR","please enter the id number");
               }else {
                   Cursor cursor = db.rawQuery("SELECT * FROM citizens WHERE id_number = '"+id+"'",null);
                   if (cursor.moveToNext()){
                       db.execSQL("DELETE FROM citizens WHERE id_number='"+id+"'");
                       mId.setText("");
                       messages("SUCCES","user deleted succesfully");
                       mId.setText("");
                   }
               }
           }
       });

    }

    //messages display function

    public void messages(String title,String messages){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(messages);
        builder.create().show();
    }
}
