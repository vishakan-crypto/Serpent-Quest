# 🐍 Snake Game in Java

A classic Snake Game implemented using **Java** and **Swing GUI**. This project demonstrates basic game mechanics, event handling, file operations, and sound integration in Java.

## 🎮 Features

- **Snake Movement** using arrow keys.
- **Three Difficulty Levels**: Easy, Medium, and Hard.
- **Pause/Resume** functionality with the 'P' key.
- **Score tracking** and **High Score** saved in a file.
- **Sound effect** played when food is eaten.
- **Real-time timer** to track survival time.
- **Simple 2D graphics** using Java AWT and Swing.

## 🛠 Requirements

- **Java JDK 8 or higher**
- A Java-compatible IDE (e.g., IntelliJ, Eclipse, or NetBeans)

## ▶️ How to Run

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/snake-game.git
    ```
2. Compile and run the `SnakeGame.java` file.
3. Use the arrow keys to move the snake.
4. Press **P** to pause and resume the game.
5. Press **ENTER** to restart after a game over.

## 📁 Project Structure

- `SnakeGame.java`: Main game logic and GUI.
- `highscore.txt`: Stores the highest score achieved.
- `eat_sound.wav`: Sound played when the snake eats food.

## 📈 Controls

- **Arrow Keys**: Move the snake (Up, Down, Left, Right).
- **P**: Pause and Resume the game.
- **ENTER**: Restart the game after Game Over.
- **1**: Select Easy difficulty.
- **2**: Select Medium difficulty.
- **3**: Select Hard difficulty.

## 💾 How the Game Works

- The game initializes with a snake of length 6.
- Food is placed randomly on the grid, and the snake grows each time it eats the food.
- The game ends if the snake collides with itself or the walls.
- The score is displayed along with a timer that counts the survival time.
- High scores are saved to a local file, `highscore.txt`.

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).

## 📬 Contact

If you have any questions or suggestions, feel free to reach out to me via email or open an issue on this repository.

---

Feel free to modify or expand the README as needed. This template provides an overview, instructions for running the project, and an explanation of its key features.

