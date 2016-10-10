package com.example.beloo.circlerectbitmapanimator;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.beloo.circlerectbitmapanimator.support.CircleToRectTransition;
import com.example.beloo.circlerectbitmapanimator.view.CircleRectView;

import static android.R.attr.transitionName;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MainActivity extends AppCompatActivity {

    CircleRectView view;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (CircleRectView) findViewById(R.id.circleView);

        view.setOnClickListener(v -> {
//            Intent intent = new Intent(this, SecondActivity.class)
//                    .addFlags(FLAG_ACTIVITY_NO_ANIMATION);
            Intent intent = new Intent(this, SecondActivity.class);
//                    .addFlags(FLAG_ACTIVITY_NO_ANIMATION);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, getString(R.string.circle));

            ActivityCompat.startActivity(MainActivity.this, intent , transitionActivityOptions.toBundle());
        });

        view.setTag("FIRST");

        root = findViewById(R.id.root);
    }
}
