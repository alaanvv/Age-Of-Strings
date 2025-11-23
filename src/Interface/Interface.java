package Interface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The main class for the game, responsible for creating the window,
 * managing the game loop, rendering graphics, and handling keyboard input.
 * It extends {@link Canvas} to be a drawable component, implements
 * {@link Runnable} to be executed on a separate thread, and implements
 * {@link KeyListener} to process user input.
 */
public class Interface extends Canvas implements Runnable, KeyListener {

    /**
     * A volatile flag to control the game loop's execution.
     * When set to true, the run() method will continue to loop.
     * When set to false, the loop will terminate, and the thread will stop.
     */
    private boolean running = false;
    
    /**
     * The dedicated thread that runs the game loop (the run() method).
     * Using a separate thread prevents the game logic from freezing the
     * application's main Event Dispatch Thread (EDT).
     */
    private Thread thread;

    public static final int WIDTH = 1920, HEIGHT = 1080;

    //iniciação de objetos


    public Interface() {
        JFrame frame = new JFrame("Age Of Strings");
        this.addKeyListener(this);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this); // Adds this canvas to the frame
        frame.pack(); // Resizes the frame to fit its components (the canvas)
        frame.setLocationRelativeTo(null); // Centers the window on the screen
        frame.setVisible(true);
    }

    /**
     * Starts the game thread safely.
     * The {@code synchronized} keyword prevents race conditions if this method
     * were to be called from multiple threads at once. It ensures that the
     * game thread is only created and started if it's not already running.
     */
    public synchronized void start() {
        if (running) return; // Don't start a new thread if one is already active
        running = true;
        thread = new Thread(this);
        thread.start(); // This will invoke the run() method
    }

    /**
     * Stops the game thread safely.
     * The {@code synchronized} keyword works with start() to prevent conflicts.
     * It waits for the game thread to finish its execution completely before
     * the program continues, ensuring a clean shutdown.
     */
    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            // Waits for the 'thread' to die (finish its run() method).
            // This is crucial for ensuring all resources are released properly.
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

  
    /**
     * Updates the game state. All game logic, such as character movement,
     * physics calculations, and AI, should be handled here. This method is
     * called at a fixed rate (typically 60 times per second) by the game loop.
     */
    private void tick() {
        
    }

    /**
     * Renders all graphics to the screen. This method uses a BufferStrategy
     * to prevent flickering and screen tearing by drawing to a hidden "buffer"
     * and then showing it on the screen all at once.
     */
    private void render() {
        // A mechanism to manage multiple screens (buffers) for drawing.
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            // If it's the first time, create a triple buffer strategy.
            // Triple buffering provides smoother rendering than double buffering.
            this.createBufferStrategy(3);
            return;
        }

        // Get the graphics context from the buffer to draw on.
        Graphics g = bs.getDrawGraphics();

        // --- Start Drawing ---
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // --- End Drawing ---

        g.dispose(); // Releases system resources used by the graphics object.
        bs.show(); // Makes the next available buffer visible (swaps buffers).
    }

    /**
     * The core game loop. This method is executed by the game {@link #thread}.
     * It's designed to run the {@link #tick()} method at a consistent rate,
     * regardless of how fast the {@link #render()} method runs, ensuring
     * predictable game behavior across different hardware.
     */
    @Override
    public void run() {
        // Requests that this component gets the input focus to receive key events.
        requestFocus();

        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000.0 / 60.0; // 60 ticks per second
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            // 'delta' accumulates the time passed in terms of ticks.
            // When delta >= 1, it means enough time has passed for one tick.
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            // This inner loop ensures game logic catches up if rendering lags.
            while (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }

        stop(); // Clean up and stop the thread when the loop finishes.
    }

    public static void main(String[] args) {
        new Interface().start();
    }

    /**
     * Invoked when a key has been pressed.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        /* if (e.getKeyCode() == KeyEvent.VK_W) player.up = true;
        if (e.getKeyCode() == KeyEvent.VK_S) player.down = true;
        if (e.getKeyCode() == KeyEvent.VK_A) player.left = true;
        if (e.getKeyCode() == KeyEvent.VK_D) player.right = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.ydir = -1;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.ydir = 1;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.xdir = -1;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.xdir = 1;
        */

    }
    
    /**
     * Invoked when a key has been released.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        /*
        if (e.getKeyCode() == KeyEvent.VK_W) player.up = false;
        if (e.getKeyCode() == KeyEvent.VK_S) player.down = false;
        if (e.getKeyCode() == KeyEvent.VK_A) player.left = false;
        if (e.getKeyCode() == KeyEvent.VK_D) player.right = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.ydir = 0;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.ydir = 0;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.xdir = 0;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.xdir = 0;
        */

    }

    /**
     * Invoked when a key has been typed. This event is not typically used
     * for game controls that require continuous input (like movement).
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) { }
}