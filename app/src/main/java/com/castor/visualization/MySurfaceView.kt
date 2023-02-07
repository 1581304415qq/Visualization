package com.castor.visualization

import android.content.Context
import android.opengl.GLSurfaceView


class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRender

    init {
        Assets.init(context)

        // Create an OpenGL ES 3.1 context
        setEGLContextClientVersion(3)

        renderer = MyGLRender()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }

}
