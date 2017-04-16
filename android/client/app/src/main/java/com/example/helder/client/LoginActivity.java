package com.example.helder.client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    private String userID;

    EditText editUsername;
    EditText editPassword;
    Boolean state;

    public static final String KEY_USERNAME = "user";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "fbToken";



    DB mDBHelper;
    SQLiteDatabase db;
    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = "Null";
        state = false;

        editUsername = (EditText)findViewById(R.id.edit_username);
        editPassword = (EditText)findViewById(R.id.edit_password);

        mDBHelper = new DB(this);
        db = mDBHelper.getReadableDatabase();

        SharedPreferences shared = getSharedPreferences(Utils.param_sharedPreferences, Context.MODE_PRIVATE);
        String user = shared.getString(Utils.param_username, "default");
        String pass = shared.getString(Utils.param_password, "default");
        editUsername.setText(user);
        editPassword.setText(pass);
        state = shared.getBoolean(Utils.param_dontShow, false);

        Utils.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("fbLogin",Utils.firebaseToken);

        Boolean retorno = getIntent().getBooleanExtra(Utils.logout, false);

        if(!retorno){
            verifyLogin();
        }


    }

    public void Enter(View v){
        //com net
        //verifyLogin();
        //sem net

        if(isNetworkAvailable()){
            verifyLogin();

            saveLogin();
        }else{
            if(verifyLoginLocal()) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra(Utils.param_Userid, userID);
                startActivity(i);
            }

        }
    }


    private void verifyLogin(){
        final String url = Utils.URL_PRINCIPAL + "/api/loginCheck";
        final String user = editUsername.getText().toString().trim();
        final String password = getSha1Hex(editPassword.getText().toString().trim());
        //final String user = "helder";
        //final String password = "b74e86682b5e0b2a3b53a8816cdfe217b74fccc1";


        Map<String,String> params = new HashMap<String, String>();
        params.put(KEY_USERNAME, user);
        params.put(KEY_PASSWORD, password);
        params.put(KEY_TOKEN, Utils.firebaseToken);

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
                        finish();
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

    private void saveLogin(){
        /*new AlertDialog.Builder(this)
                .setTitle("Save Login")
                .setMessage("Are you sure you want to save this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

        SharedPreferences shared = getSharedPreferences(Utils.param_sharedPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(Utils.param_username, editUsername.getText().toString());
        editor.putString(Utils.param_password, editPassword.getText().toString());
        editor.putBoolean(Utils.param_dontShow, true);
        editor.commit();
    }

    public static String getSha1Hex(String clearString)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes)
            {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
            return null;
        }
    }
}
