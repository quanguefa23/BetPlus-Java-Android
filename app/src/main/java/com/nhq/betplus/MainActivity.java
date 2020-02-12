package com.nhq.betplus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    MainPlayer mainPlayer;

    TextView usernameTV;
    TextView levelTV;
    TextView coinTV;
    TextView gemTV;

    CardView cv1;
    CardView cv2;
    CardView cv3;
    CardView cv4;
    CardView cv5;
    CardView cv6;

    int REQUEST_CODE_ADMOB = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Statee", "0 onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapWidget();
        initialMainPlayer();
        setOnClickForEachGame();

        contactFunction();
        dailyMissionFunction();
        earnCoinFunction();
    }

    private void contactFunction() {
        ImageView iv = findViewById(R.id.main_contact);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*              Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"nhqnhq1@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback ứng dụng Bet Plus");
                email.putExtra(Intent.EXTRA_TEXT, "Your feedback...");
                email.setType("message/rfc822");*/
                confirm();
            }

            private void confirm() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Feedback");
                alertDialog.setMessage("Gửi feedback của bạn tới email nhqnhq1@gmail.com?");

                alertDialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nhqnhq1@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback ứng dụng Bet Plus");
                        intent.putExtra(Intent.EXTRA_TEXT, "Your feedback...");

                        startActivity(Intent.createChooser(intent, "Chọn ứng dụng mail"));
                    }
                });
                alertDialog.setNegativeButton("Hủy", null);

                alertDialog.show();
            }
        });
    }

    private void dailyMissionFunction() {
        ImageView iv = findViewById(R.id.main_daily_mission);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "NHIỆM VỤ HÀNG NGÀY. Chức năng đang phát triển.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void earnCoinFunction() {
        //sau này sẽ thêm activity mới cho mục earn coin -> bao gồm xem video, lấy ngọc mua coin, mua coin bằng tiền
        ImageView addCoin = findViewById(R.id.main_earn_coin);
        addCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainPlayer.getCoin() > 200) {
                    Toast.makeText(MainActivity.this,
                            "Không thể nhận coin miễn phí khi đang có hơn 200 coin",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, AdmobActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADMOB);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ADMOB && resultCode == RESULT_OK) {
            mainPlayer.addCoin(200);
            commitData();
            Toast.makeText(MainActivity.this,
                    "Chúc mừng! Bạn nhận được 200 coin miễn phí",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this,
                    "Bạn chưa xem quảng cáo!", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void commitData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("level", mainPlayer.getLevel());
        editor.putFloat("exp", mainPlayer.getExp());
        editor.putInt("coin", mainPlayer.getCoin());
        editor.putInt("gem", mainPlayer.getGem());
        editor.apply();
    }

    private void initialMainPlayer() {
        mainPlayer = new MainPlayer();
    }

    private void setOnClickForEachGame() {
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game1Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Code chưa xong", Toast.LENGTH_SHORT).show();
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game3Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Code chưa xong", Toast.LENGTH_SHORT).show();
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Code chưa xong", Toast.LENGTH_SHORT).show();
            }
        });

        cv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        "Code chưa xong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateView() {
        usernameTV.setText("Player: " + mainPlayer.getUsername());
        String levelString = "Level : " + mainPlayer.getLevel() + " | " + mainPlayer.getExp() + "%";
        levelTV.setText(levelString);
        coinTV.setText(mainPlayer.getCoin() + " ");
        gemTV.setText(mainPlayer.getGem() + " ");
    }

    private void updateMainPlayer() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        int level = sharedPreferences.getInt("level", 0);
        float exp = sharedPreferences.getFloat("exp", 0);
        int coin = sharedPreferences.getInt("coin", 1000);
        int gem = sharedPreferences.getInt("gem", 0);
        mainPlayer.update(username, level, exp, coin, gem);
    }

    private void mapWidget() {
        usernameTV = findViewById(R.id.main_username);
        levelTV = findViewById(R.id.main_level);
        coinTV = findViewById(R.id.main_sumcoin);
        gemTV = findViewById(R.id.main_sumgem);

        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);
        cv5 = findViewById(R.id.cv5);
        cv6 = findViewById(R.id.cv6);
    }

    @Override
    protected void onResume() {
        updateMainPlayer();
        updateView();
        Log.d("Statee", "0 onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        commitData();
        super.onPause();
        Log.d("Statee", "0 onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Statee", "0 onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Statee", "0 onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Statee", "0 onStop");
    }
}
