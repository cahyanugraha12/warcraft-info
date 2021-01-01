package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.credits.ui

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.AttributeSet
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView(context: Context, attributes: AttributeSet? = null)
    : GLSurfaceView(context, attributes) {
    private val renderer: MyGLRenderer
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        setRenderer(renderer)
    }

    inner class MyGLRenderer : GLSurfaceView.Renderer {
        private lateinit var wShape: WShape
        private var wShapeCoords = floatArrayOf(
            -0.6f, 0.622008459f, 0.0f,
            -0.3f, -0.311004243f, 0.0f,
            0.0f, 0.622008459f, 0.0f,
            0.3f, -0.311004243f, 0.0f,
            0.6f, 0.622008459f, 0.0f
        )

        override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            GLES20.glViewport(0, 0, width, height)
            val ratio: Float = width.toFloat() / height.toFloat()
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

            wShape = WShape()
        }

        override fun onDrawFrame(unused: GL10) {
            val scratch = FloatArray(16)

            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            val time = SystemClock.uptimeMillis() % 4000L
            val angle = 0.090f * time.toInt()
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

            wShape.draw(scratch)
        }

        override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(type).also { shader ->
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        inner class WShape {
            private var mProgram: Int
            private val color = floatArrayOf(0.4315f, 0.3795f, 0.1889f, 1.0f)
            private var positionHandle: Int = 0
            private var mColorHandle: Int = 0
            private val vertexCount: Int = wShapeCoords.size / 3
            private val vertexStride: Int = 3 * 4
            private var vPMatrixHandle: Int = 0

            private val vertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}"

            private val fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}"

            init {
                val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
                val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

                mProgram = GLES20.glCreateProgram().also {
                    GLES20.glAttachShader(it, vertexShader)
                    GLES20.glAttachShader(it, fragmentShader)
                    GLES20.glLinkProgram(it)
                }
            }

            private var vertexBuffer: FloatBuffer =
                ByteBuffer.allocateDirect(wShapeCoords.size * 4).run {
                    order(ByteOrder.nativeOrder())

                    asFloatBuffer().apply {
                        put(wShapeCoords)
                        position(0)
                    }
                }

            fun draw(mvpMatrix: FloatArray) {
                GLES20.glUseProgram(mProgram)
                positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
                    GLES20.glEnableVertexAttribArray(it)
                    GLES20.glVertexAttribPointer(
                        it,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        vertexStride,
                        vertexBuffer
                    )
                    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                        GLES20.glUniform4fv(colorHandle, 1, color, 0)
                    }
                    vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
                    GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
                    GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexCount)
                    GLES20.glDisableVertexAttribArray(it)
                }
            }
        }
    }
}