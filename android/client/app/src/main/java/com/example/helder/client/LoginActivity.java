package com.example.helder.client;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.helder.client.Adapter.MyAnimalListAdapter;
import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.DataBase.Contrato;
import com.example.helder.client.DataBase.DB;
import com.example.helder.client.DataBase.Location;
import com.example.helder.client.WebServices.Singleton;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    private String userID;

    EditText editUsername;
    EditText editPassword;

    public static final String KEY_USERNAME = "user";
    public static final String KEY_PASSWORD = "password";


    DB mDBHelper;
    SQLiteDatabase db;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = "Null";

        editUsername = (EditText)findViewById(R.id.edit_username);
        editPassword = (EditText)findViewById(R.id.edit_password);

        mDBHelper = new DB(this);
        db = mDBHelper.getReadableDatabase();


    }

    public void Enter(View v){
        //com net
        //verifyLogin();
        //sem net

        if(isNetworkAvailable()){
            verifyLogin();
            Toast.makeText(this,"entrou! e tem net",Toast.LENGTH_SHORT).show();
        }else{
            if(verifyLoginLocal()){
                Toast.makeText(this,"entrou! e nao tem net",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra(Utils.param_Userid, userID);
                startActivity(i);
            }else{
                Toast.makeText(this,"nao entrou!",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void verifyLogin(){
        final String url = "http://eurogather.net:3000/api/loginCheck";
        //final String user = editUsername.getText().toString().trim();
        //final String password = editPassword.getText().toString().trim();
        final String user = "helder";
        final String password = "b74e86682b5e0b2a3b53a8816cdfe217b74fccc1";


        Map<String,String> params = new HashMap<String, String>();
        params.put(KEY_USERNAME, user);
        params.put(KEY_PASSWORD, password);

        JsonObjectRequest getRequest = new JsonObjectRequest(url, new JSONObject(params) ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // display response
                try{

                    String result = response.getString("result");
                    if(result.equals("OK")){
                        JSONObject jobj= response.getJSONObject("user");

                        userID = jobj.getString("_id");

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(Utils.param_Userid, userID);
                        startActivity(i);

                    }else{
                        Toast.makeText(LoginActivity.this, "Login Inválido", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(this).addToRequestQeueu(getRequest);
    }


    private Boolean verifyLoginLocal(){
        String sql = "Select " +
                Contrato.User.COLUMN_USERNAME + ", " +
                Contrato.User.COLUMN_ID +
                " FROM " + Contrato.User.TABLE_NAME +
                " WHERE " + Contrato.User.TABLE_NAME + "."
                + Contrato.User.COLUMN_USERNAME + " = '" + editUsername.getText().toString() + "'" +
                " AND " + Contrato.User.TABLE_NAME + "."
                + Contrato.User.COLUMN_PASSWORD + " = '" + editPassword.getText().toString() + "'";

        c = db.rawQuery(sql, null);

        if(c.getCount() == 0){
            return false;
        }else{
            Toast.makeText(this, c.getColumnName(1) + "", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
