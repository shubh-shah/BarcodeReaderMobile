package com.google.mlkit.md;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheckPairStatusActivity extends AppCompatActivity {
    TextView statusView;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pair_status);

        statusView = findViewById(R.id.showStatusView);
        errorView = findViewById(R.id.errorView);

        SendRequestThread request = new SendRequestThread("");
        boolean response = request.start();
        if(response){
            statusView.setText(R.string.check_pair_status_success);
        }
        else{
            statusView.setText(R.string.check_pair_status_failure);
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(request.error);
        }
    }

    public void refresh(View view) {
        statusView = findViewById(R.id.showStatusView);
        errorView = findViewById(R.id.errorView);

        statusView.setText(R.string.checking_pair_status);
        errorView.setVisibility(View.INVISIBLE);

        SendRequestThread request = new SendRequestThread("");
        boolean response = request.start();
        if(response){
            statusView.setText(R.string.check_pair_status_success);
            errorView.setVisibility(View.INVISIBLE);
        }
        else{
            statusView.setText(R.string.check_pair_status_failure);
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(request.error);
        }
    }

    public void returnBack(View view) {
        onBackPressed();
    }
}