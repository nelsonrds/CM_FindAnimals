package com.example.helder.client.DataBase;

import android.provider.BaseColumns;

/**
 * Created by nelso on 10/04/2017.
 */

public class Contrato {
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String CREATE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String REFERENCES = " REFERENCES ";
    private static final String DROP = "DROP TABLE ";

    public Contrato() {
    }

    public static abstract class User implements BaseColumns{
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_ID = "id_mongo";
        public static final String COLUMN_ID_FENCE = "id_fence";

        public static final String SQL_CREATES_USER =
                CREATE + User.TABLE_NAME + "(" +
                        User._ID + INT_TYPE + PRIMARY_KEY + "," +
                        User.COLUMN_NAME + TEXT_TYPE + "," +
                        User.COLUMN_USERNAME + TEXT_TYPE + "," +
                        User.COLUMN_PASSWORD + TEXT_TYPE + "," +
                        User.COLUMN_ID + TEXT_TYPE + "," +
                        User.COLUMN_ID_FENCE + INT_TYPE + REFERENCES +
                        Fence.TABLE_NAME + "(" + Fence._ID + "));";

        public static final String SQL_DROP_USER = DROP + User.TABLE_NAME + ";";
    }

    public static abstract class Coordenadas implements BaseColumns {
        public static final String TABLE_NAME = "Coordenadas";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        public static final String[] PROJECTION = {Coordenadas._ID, Coordenadas.COLUMN_LATITUDE, Coordenadas.COLUMN_LONGITUDE};

        public static final String SQL_CREATES_COORDENADAS =
                CREATE + Coordenadas.TABLE_NAME + "(" +
                        Coordenadas._ID + INT_TYPE + PRIMARY_KEY + "," +
                        Coordenadas.COLUMN_LATITUDE + TEXT_TYPE + "," +
                        Coordenadas.COLUMN_LONGITUDE + TEXT_TYPE + ");";


        public static final String SQL_DROP_COORDENADAS = DROP + Coordenadas.TABLE_NAME + ";";
    }

    public static abstract class Fence implements BaseColumns{
        public static final String TABLE_NAME = "Fence";

        public static final String SQL_CREATES_FENCE =
                CREATE + Fence.TABLE_NAME + "(" +
                        Coordenadas._ID + INT_TYPE + PRIMARY_KEY  + ");";

        public static final String SQL_DROP_FENCE = DROP + Fence.TABLE_NAME + ";";
    }

    public static abstract class FenceCoordenadas implements BaseColumns{
        public static final String TABLE_NAME = "FenceCoordenadas";
        public static final String COLUMN_IDFENCE = "id_fence";
        public static final String COLUMN_IDCOORDENADA = "id_coordenada";

        public static final String SQL_CREATES_FENCECOORDENADAS =
                CREATE + FenceCoordenadas.TABLE_NAME + "(" +
                        FenceCoordenadas._ID + INT_TYPE + PRIMARY_KEY + "," +
                        FenceCoordenadas.COLUMN_IDFENCE + INT_TYPE + REFERENCES +
                        Fence.TABLE_NAME + "(" + Fence._ID + "));" +
                        FenceCoordenadas.COLUMN_IDCOORDENADA + INT_TYPE + REFERENCES +
                        Coordenadas._ID + "(" + Coordenadas._ID + "));";

        public static final String SQL_DROP_FENCECOORDENADAS = DROP + FenceCoordenadas.TABLE_NAME + ";";
    }
}