package com.example.helder.client.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nelso on 10/04/2017.
 */

public class DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Animals.db";

    public DB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(Contrato.Fence.SQL_CREATES_FENCE);
        db.execSQL(Contrato.Coordenadas.SQL_CREATES_COORDENADAS);
        db.execSQL(Contrato.FenceCoordenadas.SQL_CREATES_FENCECOORDENADAS);
        db.execSQL(Contrato.User.SQL_CREATES_USER);

        db.execSQL("insert into " + Contrato.Fence.TABLE_NAME + " values (1);");
        db.execSQL("insert into " + Contrato.User.TABLE_NAME + " values (1, 'Nelson Rodrigues', 'nelson', 'nelson', '123', 1);");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(Contrato.User.SQL_DROP_USER);
        db.execSQL(Contrato.FenceCoordenadas.SQL_DROP_FENCECOORDENADAS);
        db.execSQL(Contrato.Fence.SQL_DROP_FENCE);
        db.execSQL(Contrato.Coordenadas.SQL_DROP_COORDENADAS);
        onCreate(db);
    }

    public void onDownGrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db ,newVersion, oldVersion);
    }
}
