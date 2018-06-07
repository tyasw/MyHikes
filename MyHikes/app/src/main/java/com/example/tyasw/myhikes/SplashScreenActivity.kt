package com.example.tyasw.myhikes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Scene
import android.view.View
import android.transition.TransitionManager

import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    var scene1: Scene? = null
    var scene2: Scene? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        scene1 = Scene.getSceneForLayout(rootContainer, R.layout.splash_layout, this)
        scene2 = Scene.getSceneForLayout(rootContainer, R.layout.splash_layout_2, this)

        scene1?.enter()
    }

    fun goToScene2(view: View) {
        TransitionManager.go(scene2)
    }

    fun goToScene1(view: View) {
        TransitionManager.go(scene1)
    }
}
