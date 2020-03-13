package ru.romanzaycev.multigofjogl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.IntBuffer;
import java.util.Random;

public class Game extends JFrame implements GLEventListener, KeyListener {
    private static final long serialVersionUID = 1L;

    private final int width = 1920;
    private final int height = 1080;

    private final String name = "Multithreaded GoF";

    private final FPSAnimator animator;

    private Board currentGeneration;
    private GenerationBuilder generationBuilder;

    public Game() throws HeadlessException {
        super("Multithreaded GoF");

        currentGeneration = new BoardImpl(width, height);
        //generationBuilder = new GenerationBuilderImpl();
        generationBuilder = new PooledGenerationBuilder(8);

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        this.add(canvas);

        this.setName(this.name);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        canvas.requestFocusInWindow();

        final FPSAnimator animator = new FPSAnimator(canvas, 60, true);
        this.animator = animator;
        animator.start();
    }

    public void play() {
        Random random = new Random();

        for (int i = 0; i < width * height; i++) {
            if (random.nextBoolean()) {
                currentGeneration.setAliveLinearized(i);
            } else {
                currentGeneration.setDeadLinearized(i);
            }
        }
    }

    private void updateWorld() {
        synchronized (this) {
            currentGeneration = generationBuilder.getNextGeneration(currentGeneration);
        }
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        int size = width * height;
        int[] src = new int[size];
        int black = 0xff110000;
        int white = 0xffffffd3;

        updateWorld();

        for (int i = 0; i < size; i++) {
            src[i] = currentGeneration.isAliveLinearized(i) ? white : black;
        }

        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        gl.glPixelStorei(GL2.GL_UNPACK_SKIP_PIXELS, 0);
        gl.glPixelStorei(GL2.GL_UNPACK_SKIP_ROWS, 0);

        gl.glDrawPixels(
                width,
                height,
                GL.GL_RGBA,
                GL.GL_UNSIGNED_BYTE,
                IntBuffer.wrap(src)
        );

        gl.glFlush();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1.0f);
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        animator.stop();
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public void keyTyped(KeyEvent keyEvent) {

    }

    public void keyPressed(KeyEvent keyEvent) {

    }
}
