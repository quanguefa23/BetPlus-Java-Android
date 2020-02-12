package com.nhq.betplus;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Statee", "-1 onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get username
        String username = getUsername();

        if (username.equals("")) {
            //if can not get username, show dialog to input username
            dialogInputUserName();
        }
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

/*    private String getUsername() {
        //folder name
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("RacingBetLocal", Context.MODE_PRIVATE);

        String filename = "username";
        File myInternalFile = new File(directory, filename);

        try {
            //read file
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String new_username = br.readLine();
            in.close();
            return new_username;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }*/

    private String getUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    private void dialogInputUserName() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button bt = dialog.findViewById(R.id.confirm);
        final EditText ed = dialog.findViewById(R.id.editText);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_username = ed.getText().toString();
                int len = new_username.length();
                if (len >= 8 && len <= 16) {
                    dialog.cancel();

                    saveUsername(new_username);
                    pushDefaultData();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Nickname có độ dài từ 8-16 kí tự", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void pushDefaultData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("level", 0);
        editor.putFloat("exp", 0);
        editor.putInt("coin", 1000);
        editor.putInt("gem", 0);
        editor.apply();
    }

/*    private void saveUsername(String username) {
        //folder name
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("RacingBetLocal", Context.MODE_PRIVATE);

        String filename = "username";
        File myInternalFile = new File(directory, filename);

        try {
            //Mở file
            FileOutputStream fos = new FileOutputStream(myInternalFile);
            //Ghi dữ liệu vào file
            fos.write(username.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Statee", "-1 onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Statee", "-1 onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Statee", "-1 onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Statee", "-1 onResume");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Statee", "-1 onStop");
    }
}
