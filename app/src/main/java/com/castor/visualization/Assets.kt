package com.castor.visualization

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.util.Log
import java.io.InputStream

@SuppressLint("StaticFieldLeak")
object Assets {
    val TAG = "GameAssets"

    val lightColor = floatArrayOf(0.9f, 0.9f, 0.9267f, 1.0f)
    val lightPos = floatArrayOf(15.0f, 15.0f, 15.0f)
    val resources = mutableMapOf<Int, InputStream>()

    private lateinit var context: Context
    private val assets=arrayOf(
        R.raw.box_frag,
        R.raw.box_vert,
    )

    fun init(context: Context) {
        this.context = context
    }

    fun loadResource() {
        for (s in assets) {
            resources[s] = context.resources.openRawResource(s)
            Log.d(TAG, "$s")
        }
    }

    fun getResource(id: Int): InputStream {
        if (resources[id] == null) {
            resources[id] = context.resources.openRawResource(id)
        }
        return resources[id]!!
    }

    fun getImage(id: Int): Bitmap = getImage(getResource(id))
    fun getImage(imageInputStream: InputStream): Bitmap {
        val bitmapLoadingOptions = BitmapFactory.Options()
        bitmapLoadingOptions.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeStream(imageInputStream, null, bitmapLoadingOptions)!!
    }

    /**
     * v1 vertexShader
     * v2 fragmentSharder
     */
    fun createProgram(v1: Int, v2: Int): Int {
        val vertex = String(getResource(v1).readBytes())
        val fragment = String(getResource(v2).readBytes())

        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertex)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragment)

        // create empty OpenGL ES Program
        var mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }
        val linkingSucceeded = IntArray(1)
        GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linkingSucceeded, 0)
        if (linkingSucceeded[0] == GLES30.GL_FALSE) {
            Log.e(TAG, "Unable to link program :");
            Log.e(TAG, GLES30.glGetProgramInfoLog(mProgram));
            GLES30.glDeleteProgram(mProgram)
            mProgram = 0
        }
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)
        return mProgram
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        var shader = GLES30.glCreateShader(type)

        // add the source code to the shader and compile it
        if (shader != 0) {
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                Log.e(TAG, "glCompileShader error")
                Log.e(TAG, GLES30.glGetShaderInfoLog(shader))
                GLES30.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }
}