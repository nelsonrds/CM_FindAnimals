package com.example.helder.client.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nelso on 10/04/2017.
 */

public class DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = ".db";

    public DB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db){

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL(Contrato.Entidade.SQL_DROP_ENTITIES);
        //db.execSQL(Contrato.Cidade.SQL_DROP_ENTITIES);
        onCreate(db);
    }

    public void onDownGrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db ,newVersion, oldVersion);
    }
}
