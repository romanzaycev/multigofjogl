package ru.romanzaycev.multigoljogl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.IntBuffer;
import java.time.Clock;
import java.util.Random;

public class Game extends JFrame implements GLEventListener, KeyListener {
    // Settings
    private final int BOARD_WIDTH = 1920;
    private final int BOARD_HEIGHT = 1080;

    private static final int WND_WIDTH = 1920;
    private static final int WND_HEIGHT = 1080;

    private static final int TARGET_FPS = 60;

    private static final int BOARD_GENERATION_THREADS = 16;

    private static final int GEN_DELAY_MILLS = 50;

    //
    private static final long serialVersionUID = 1L;
    private final FPSAnimator animator;
    private BoardHandler boardHandler;
    private UpdateWorldThread updateBoardThread;

    private static class BoardHandler {
        private Board b;

        public Board getBoard() {
            return b;
        }

        public synchronized void setBoard(Board b) {
            this.b = b;
        }
    }

    private static class UpdateWorldThread implements Runnable {
        private GenerationBuilder builder;
        private BoardHandler boardHandler;
        private int delay;
        private volatile boolean running = true;

        public UpdateWorldThread(GenerationBuilder builder, BoardHandler boardHandler, int genDelayMills) {
            this.builder = builder;
            this.boardHandler = boardHandler;
            this.delay = genDelayMills;
        }

        public void terminate() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                long ms = Clock.systemUTC().millis();
                boardHandler.setBoard(builder.getNextGeneration(boardHandler.getBoard()));

                if (delay > 0) {
                    long md = Clock.systemUTC().millis() - ms;

                    if (md < delay) {
                        try {
                            Thread.sleep(delay - md);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Game() throws HeadlessException {
        super("Multithreaded GoL");

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        this.add(canvas);

        this.setName(this.getTitle());
        this.setSize(WND_WIDTH, WND_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        canvas.requestFocusInWindow();

        boardHandler = new BoardHandler();
        animator = new FPSAnimator(canvas, TARGET_FPS, true);
    }

    public void play() {
        Random random = new Random();

        Board currentGeneration = new BoardImpl(BOARD_WIDTH, BOARD_HEIGHT);
        GenerationBuilder generationBuilder = new PooledGenerationBuilder(BOARD_GENERATION_THREADS);

        for (int i = 0; i < BOARD_WIDTH * BOARD_HEIGHT; i++) {
            if (random.nextBoolean()) {
                currentGeneration.setAliveLinearized(i);
            } else {
                currentGeneration.setDeadLinearized(i);
            }
        }

        boardHandler.setBoard(currentGeneration);
        updateBoardThread = new UpdateWorldThread(generationBuilder, boardHandler, GEN_DELAY_MILLS);
        Thread t = new Thread(updateBoardThread);
        t.start();

        animator.start();
    }

    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        int size = BOARD_WIDTH * BOARD_HEIGHT;
        int[] src = new int[size];
        int black = 0xff110000;
        int white = 0xffffffd3;

        if (boardHandler.getBoard() != null) {
            for (int i = 0; i < size; i++) {
                src[i] = boardHandler.getBoard().isAliveLinearized(i) ? white : black;
            }
        }

        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        gl.glPixelStorei(GL2.GL_UNPACK_SKIP_PIXELS, 0);
        gl.glPixelStorei(GL2.GL_UNPACK_SKIP_ROWS, 0);

        // Scale
        gl.glPixelZoom((float) WND_WIDTH / BOARD_WIDTH, (float) WND_HEIGHT / BOARD_HEIGHT);

        gl.glDrawPixels(
                BOARD_WIDTH,
                BOARD_HEIGHT,
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
        updateBoardThread.terminate();
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
