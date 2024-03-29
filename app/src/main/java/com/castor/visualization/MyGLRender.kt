package com.castor.visualization

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGLRender() : GLSurfaceView.Renderer {
    val TAG = "MyGLRenderer"

    /** Timeout to wait for the surface to be created and the version queried.  */
    private val TIMEOUT_SECONDS = 10L

    /** Version string reported by glGetString.  */
    private var mVersionString: String? = null

    /** Extensions string reported by glGetString.  */
    private var mExtensionsString: String? = null

    /** Latch that is unlocked when the activity is done finding the version.  */
    private val mSurfaceCreatedLatch: CountDownLatch = CountDownLatch(1)

    private val projectionMatrix = FloatArray(16) { 1f }
    private val viewMatrix = FloatArray(16) { 1f }

    // 摄像机的位置坐标
    private val cameraPos = floatArrayOf(0.0f, 0f, 6f)

    // 摄像机目标的坐标
    private val cameraFront = floatArrayOf(0.0f, 0.0f, 0.0f)

    // 摄像机的up方向
    private val cameraUp = floatArrayOf(0.0f, 1.0f, 0.0f)
    private var rad = floatArrayOf(0.0f, 0.0f)

    private lateinit var cube: Cube

    @Throws(InterruptedException::class)
    fun getVersionString(): String? {
        mSurfaceCreatedLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        synchronized(this) { return mVersionString }
    }

    @Throws(InterruptedException::class)
    fun getExtensionsString(): String? {
        mSurfaceCreatedLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        synchronized(this) { return mExtensionsString }
    }

    fun hasExtension(extensions: String, name: String): Boolean {
        var start = extensions.indexOf(name)
        while (start >= 0) {
            // check that we didn't find a prefix of a longer extension name
            val end = start + name.length
            if (end == extensions.length || extensions[end] == ' ') {
                return true
            }
            start = extensions.indexOf(name, end)
        }
        return false
    }

    private fun setOpenGl(gl: GL10, config: EGLConfig) {
        // 检查opengl版本
        try {
            mVersionString = gl.glGetString(GL10.GL_VERSION)
            mExtensionsString = gl.glGetString(GL10.GL_EXTENSIONS)
            Log.d(TAG, "$mVersionString $mExtensionsString")
        } finally {
            mSurfaceCreatedLatch.countDown()
        }
        // 获取屏幕尺寸

        //设置opengl
        GLES30.glViewport(0, 0, 800, 600)
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        setOpenGl(gl, config)
        Assets.loadResource()
        cube = Cube(width = 0.8f, depth = 1f, height = 0.6f, x = 0f, y = 0f, z = 0f)
        cube.angles = floatArrayOf(20f, 30f, 45f)
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        cube.draw(projectionMatrix, viewMatrix)
        cube.angleX += 1
//        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
//        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

//        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
//         this projection matrix is applied to object coordinates
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 700f)
        Matrix.setLookAtM(
            viewMatrix,
            0,
            cameraPos[0], cameraPos[1], cameraPos[2],
            cameraFront[0], cameraFront[1], cameraFront[2],
            cameraUp[0], cameraUp[1], cameraUp[2],
        )
    }
}
