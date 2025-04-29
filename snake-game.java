import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 600, HEIGHT = 600;
    private final int UNIT_SIZE = 20;
    private final int TOTAL_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    private final int[] x = new int[TOTAL_UNITS];
    private final int[] y = new int[TOTAL_UNITS];

    private int bodyParts = 6;
    private int foodX, foodY;
    private int score = 0;
    private int delay = 150; // Default speed delay (Medium)

    private char direction = 'R'; // U, D, L, R
    private boolean running = false;
    private boolean paused = false;
    private Timer timer;
    private Random random;
    private long startTime; // To track the game start time
    private Color snakeColor = Color.green; // Snake color
    private Color foodColor = Color.red;   // Food color
    private String highScoreFile = "highscore.txt";
    private int highScore = 0; // Store the highest score achieved
    private String difficulty = "Medium"; // Default difficulty

    // Constructor
    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        random = new Random();
        loadHighScore(); // Load high score at the start
        startGame();
    }

    // Start the game based on difficulty
    public void startGame() {
        placeFood();
        running = true;
        paused = false;
        startTime = System.currentTimeMillis();
        setDifficultySpeed();
        timer = new Timer(delay, this);
        timer.start();
    }

    // Set the speed based on difficulty
    public void setDifficultySpeed() {
        switch (difficulty) {
            case "Easy":
                delay = 200; // Slow speed for easy
                break;
            case "Medium":
                delay = 150; // Default speed
                break;
            case "Hard":
                delay = 100; // Fast speed for hard
                break;
            default:
                delay = 150; // Default to medium if invalid
        }
    }

    // Restart the game
    public void restartGame() {
        bodyParts = 6;
        score = 0;
        direction = 'R';
        setDifficultySpeed();

        for (int i = 0; i < TOTAL_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        timer.stop();
        startGame();
    }

    // Paint the components on screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draw the game objects
    public void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(foodColor);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? snakeColor : Color.white);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25);

            // Draw Timer
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Time: " + elapsedTime + "s", WIDTH / 2 - 40, 25);

            // Draw difficulty
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Difficulty: " + difficulty, WIDTH - 150, 25);
        } else {
            gameOver(g);
        }

        if (paused) {
            drawPauseScreen(g);
        }
    }

    // Place food at random position
    public void placeFood() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    // Move the snake
    public void move() {
        if (!paused) {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {
                case 'U': y[0] -= UNIT_SIZE; break;
                case 'D': y[0] += UNIT_SIZE; break;
                case 'L': x[0] -= UNIT_SIZE; break;
                case 'R': x[0] += UNIT_SIZE; break;
            }
        }
    }

    // Check if snake eats food
    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            score++;
            playEatSound();
            placeFood();

            // Increase speed every 5 points
            if (score % 5 == 0 && delay > 50) {
                delay -= 10;
                timer.setDelay(delay);
            }
        }
    }

    // Check for collisions
    public void checkCollisions() {
        // Head with body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Wall collision
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            saveHighScore(); // Save the high score if the game ends
            timer.stop();
        }
    }

    // Display game over screen
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, WIDTH / 2 - 40, HEIGHT / 2 + 40);
        g.drawString("High Score: " + highScore, WIDTH / 2 - 60, HEIGHT / 2 + 80);
        g.drawString("Press ENTER to Restart", WIDTH / 2 - 100, HEIGHT / 2 + 120);
    }

    // Pause screen
    public void drawPauseScreen(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("PAUSED", WIDTH / 2 - 75, HEIGHT / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press P to Resume", WIDTH / 2 - 90, HEIGHT / 2 + 40);
    }

    // Play sound when snake eats food
    private void playEatSound() {
        try {
            // Load and play the eat sound
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/eat_sound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load high score from file
    private void loadHighScore() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(highScoreFile));
            highScore = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (IOException e) {
            highScore = 0;
        }
    }

    // Save high score to file
    private void saveHighScore() {
        if (score > highScore) {
            highScore = score;
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(highScoreFile));
                writer.write(String.valueOf(highScore));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Handle key press events
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R'; break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D'; break;
            case KeyEvent.VK_ENTER:
                if (!running) {
                    restartGame();
                }
                break;
            case KeyEvent.VK_P:
                paused = !paused;
                break;
            case KeyEvent.VK_1: // Easy
                difficulty = "Easy";
                setDifficultySpeed();
                break;
            case KeyEvent.VK_2: // Medium
                difficulty = "Medium";
                setDifficultySpeed();
                break;
            case KeyEvent.VK_3: // Hard
                difficulty = "Hard";
                setDifficultySpeed();
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game - Thiru");
        SnakeGame gamePanel = new SnakeGame();

        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
