package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

        public void openHelloScene(View view){
        Intent intent = new Intent(this, activity_instruction_set.class);

        if (view == findViewById(R.id.btnUser)){
            intent.putExtra("checkUser", true);
        }
        else{
            intent.putExtra("checkUser", false);
        }
        startActivity(intent);
    }
}
