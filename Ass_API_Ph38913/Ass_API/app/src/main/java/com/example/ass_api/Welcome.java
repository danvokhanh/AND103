package com.example.ass_api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển màn hình sang hoạt động mới
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
                finish(); // Tùy chọn: Đóng hoạt động hiện tại nếu bạn không muốn nó tiếp tục tồn tại sau khi chuyển màn hình
            }
        }, 3000); // 3000 milliseconds = 3 giây

    }
}