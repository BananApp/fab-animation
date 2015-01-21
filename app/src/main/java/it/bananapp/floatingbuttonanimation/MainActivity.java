package it.bananapp.floatingbuttonanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends ActionBarActivity {

    private FloatingActionButton mFloatingActionButton;

    private View mTargetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTargetView = findViewById(R.id.target_view);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                startAnimation();
            }
        });
    }

    private void startAnimation() {

        AnimatorSet animatorSet = new AnimatorSet();

        final int fabHeight = mFloatingActionButton.getHeight();
        final int fabWidth = mFloatingActionButton.getWidth();

        int heightOffset = fabHeight / 2;
        int widthOffset = fabWidth / 2;

        int cx = (mTargetView.getLeft() + mTargetView.getRight()) / 2 - widthOffset;
        int cy = (mTargetView.getTop() + mTargetView.getBottom()) / 2 - heightOffset;

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mFloatingActionButton, View.X, cx);
        animatorX.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mFloatingActionButton, View.Y, cy);
        animatorY.setInterpolator(new AccelerateInterpolator());

        int finalRadius = Math.max(mTargetView.getWidth(), mTargetView.getHeight());

        Animator animatorReveal = ViewAnimationUtils.createCircularReveal(mTargetView, cx, cy, 0, finalRadius);
        animatorReveal.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(final Animator animation) {

                mFloatingActionButton.setElevation(0);
                mTargetView.setVisibility(View.VISIBLE);
            }
        });

        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.play(animatorReveal).after(animatorX);

        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

        animatorSet.start();
    }
}
