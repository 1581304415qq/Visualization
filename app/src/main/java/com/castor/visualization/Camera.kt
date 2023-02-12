package com.castor.visualization

import android.opengl.Matrix

class Camera(var x: Float=0f, var y: Float=0f, var z: Float=0f) {
    val angle = FloatArray(3)
    val position = FloatArray(3)
    val front = FloatArray(3)
    val up = FloatArray(3)
    var projection = FloatArray(16)
    val view = FloatArray(16)

    init {
        position[0] = x
        position[1] = y
        position[2] = z
        Matrix.frustumM(projection, 0, -0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 1000f)
        Matrix.setLookAtM(
            view, 0,
            position[0], position[1], position[2],
            front[0], front[1], front[2],
            up[0], up[1], up[2],
        )
    }
}