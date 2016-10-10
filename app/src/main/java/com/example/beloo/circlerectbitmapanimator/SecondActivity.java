package com.example.beloo.circlerectbitmapanimator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.LinearInterpolator;

import com.example.beloo.circlerectbitmapanimator.support.CircleToRectTransition;
import com.example.beloo.circlerectbitmapanimator.view.CircleRectView;

public class SecondActivity extends AppCompatActivity {

    CircleRectView view;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = new CircleToRectTransition();
            transition.setDuration(1500);
            getWindow().setSharedElementEnterTransition(transition);
            getWindow().setSharedElementExitTransition(new CircleToRectTransition().setDuration(1500));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        view = (CircleRectView) findViewById(R.id.circleView);
        root = findViewById(R.id.root);
        view.setTag("SECOND");

        view.setOnClickListener(v -> view.animator(250, 600, 150, 150).setDuration(1500).start());

//        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                view.getViewTreeObserver().removeOnPreDrawListener(this);
//                view.animator(500, root.getWidth()).start();
//                root.setVisibility(View.VISIBLE);
//                view.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
//        overridePendingTransition(0, 0);
    }
}
