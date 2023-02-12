package com.castor.visualization

import android.opengl.GLES30
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val COORDS_PER_VERTEX = 3

class Cube(
    width: Float = 1f,
    depth: Float = 1f,
    height: Float = 1f,
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f
) {
    private val color = floatArrayOf(0.55f, 0.6224f, 0.7322f, 1.0f)
    private val w2 = width / 2
    private val d2 = depth / 2
    private val h2 = height / 2
    private val triangleCoordinates = floatArrayOf(// back
        -w2, h2, -d2,
        -w2, -h2, -d2,
        w2, -h2, -d2,
        w2, -h2, -d2,
        w2, h2, -d2,
        -w2, h2, -d2,

        -w2, -h2, d2,
        -w2, -h2, -d2,
        -w2, h2, -d2,
        -w2, h2, -d2,
        -w2, h2, d2,
        -w2, -h2, d2,

        w2, -h2, -d2,
        w2, -h2, d2,
        w2, h2, d2,
        w2, h2, d2,
        w2, h2, -d2,
        w2, -h2, -d2,

        -w2, -h2, d2,
        -w2, h2, d2,
        w2, h2, d2,
        w2, h2, d2,
        w2, -h2, d2,
        -w2, -h2, d2,

        -w2, h2, -d2,
        w2, h2, -d2,
        w2, h2, d2,
        w2, h2, d2,
        -w2, h2, d2,
        -w2, h2, -d2,

        -w2, -h2, -d2,
        -w2, -h2, d2,
        w2, -h2, -d2,
        w2, -h2, -d2,
        -w2, -h2, d2,
        w2, -h2, d2
    )

    private val vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoordinates.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())
            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(triangleCoordinates)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    private val vertexCount: Int = triangleCoordinates.size / COORDS_PER_VERTEX

    private var program: Int = -1

    var angles = FloatArray(3) { 0f }

    var angleX: Float
        set(value) = angles.set(0, value)
        get() = angles[0]

    init {
        program = Assets.createProgram(R.raw.box_vert, R.raw.box_frag)
    }

    fun draw(projection: FloatArray, viewPos: FloatArray) {
        val model = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f,
        )
        // 生成模型矩阵 旋转 平移 缩放*
        Matrix.rotateM(model, 0, angles[0], 1.0f, 0.0f, 0f);
        Matrix.rotateM(model, 0, angles[1], 0.0f, 1.0f, 0f);
        Matrix.rotateM(model, 0, angles[2], 0.0f, 0.0f, 1f);

        GLES30.glUseProgram(program)

        GLES30.glVertexAttribPointer(
            0,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(0)

        // get handle to fragment shader's vColor member
        GLES30.glGetUniformLocation(program, "vColor").also {
            GLES30.glUniform4fv(it, 1, color, 0)
        }

        // 物体变换矩阵
        GLES30.glGetUniformLocation(program, "model").also {
            GLES30.glUniformMatrix4fv(it, 1, false, model, 0)
        }

        // 视图矩阵
        // get handle to fragment viewPos member
        GLES30.glGetUniformLocation(program, "viewPos").also {
            GLES30.glUniformMatrix4fv(it, 1, false, viewPos, 0)
        }
        // 投影矩阵
        // get handle to shape's transformation matrix
        GLES30.glGetUniformLocation(program, "projection").also {
            GLES30.glUniformMatrix4fv(it, 1, false, projection, 0)
        }

        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

        GLES30.glDisableVertexAttribArray(0)
    }

}