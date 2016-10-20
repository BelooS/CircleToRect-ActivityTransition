package com.example.beloo.circlerectbitmapanimator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.View;

import com.example.beloo.circlerectbitmapanimator.support.CircleToRectTransition;
import com.example.beloo.circlerectbitmapanimator.view.CircleRectView;

public class SecondActivity extends AppCompatActivity {

    CircleRectView view;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds bounds = new ChangeBounds();
            bounds.setDuration(1500);
            getWindow().setSharedElementEnterTransition(bounds);
            getWindow().setSharedElementExitTransition(bounds);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        view = (CircleRectView) findViewById(R.id.circleView);
        root = findViewById(R.id.root);
        view.setTag("SECOND");

    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
//        overridePendingTransition(0, 0);
    }
}
