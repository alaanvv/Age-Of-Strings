package Interface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Interface extends Canvas implements Runnable, KeyListener {

    private boolean running = false;
    private Thread thread;

    public static final int WIDTH = 1920, HEIGHT = 1080;

    //iniciação de objetos


    public Interface() {
        JFrame frame = new JFrame("Age Of Strings");
        this.addKeyListener(this);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

  
    private void tick() {
        
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        requestFocus();

        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000.0 / 60.0;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }

        stop();
    }

    public static void main(String[] args) {
        new Interface().start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        /*   
        if (e.getKeyCode() == KeyEvent.VK_W) player.up = true;
        if (e.getKeyCode() == KeyEvent.VK_S) player.down = true;
        if (e.getKeyCode() == KeyEvent.VK_A) player.left = true;
        if (e.getKeyCode() == KeyEvent.VK_D) player.right = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.ydir = -1;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.ydir = 1;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.xdir = -1;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.xdir = 1;
        */

    }

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

    @Override
    public void keyTyped(KeyEvent e) { }
}
