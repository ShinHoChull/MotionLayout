package com.example.motionlayout

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment

class TouchActivity : AppCompatActivity() , View.OnClickListener , MotionLayout.TransitionListener {

    private var lastProgress = 0f
    private var fragment : Fragment? = null
    private var last : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)

        if (savedInstanceState == null) {
            fragment = MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container,it)
                    .commit()
            }
        }

        findViewById<MotionLayout>(R.id.motionLayout).setTransitionListener(this)
        findViewById<Button>(R.id.toggle).setOnClickListener(this)
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        if ( progress - lastProgress > 0) {
            Log.d("TouchActivity","end!!")
            //from Start to end
            val atEnd = Math.abs(progress - 1f) < 0.1f
            if (atEnd && fragment is MainFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.animator.show , 0)

                fragment = SecondFragment.newInstance().also {
                    transaction.setCustomAnimations(R.animator.show ,0)
                        .replace(R.id.container , it)
                        .commit()
                }
            }
        } else {
            //from end to start
            Log.d("TouchActivity","start!!")
            val atStart = progress < 0.9f
            if (atStart && fragment is SecondFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(0 , R.animator.hide)
                fragment = MainFragment.newInstance().also {
                    transaction.replace(R.id.container , it)
                        .commitNow()
                }
            }
        }

        lastProgress = progress
    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {

    }

    override fun onClick(view: View?) {
        Log.d("TouchActivity","onClick?")
        if (view?.id == R.id.toggle) {

            val transaction = supportFragmentManager.beginTransaction()

            fragment = if ( fragment == null || fragment is MainFragment ) {
                last = 1f
                transaction.setCustomAnimations(R.animator.show,0)
                SecondFragment.newInstance()
            } else {
                transaction.setCustomAnimations(0,R.animator.hide)
                MainFragment.newInstance()
            }.also {
                transaction
                    .replace(R.id.container , it)
                    .commitNow()
            }
        }

    }


}