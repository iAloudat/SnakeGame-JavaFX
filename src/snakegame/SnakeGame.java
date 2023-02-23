package snakegame;
/**
 * %%%%%%%%%%%%% Snake Game - Iyad Aloudat %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SnakeGame extends Application {

    /**
     * %%%%%%%%%%%%% Game Variables  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    /** Game board dimensions **/
    private static final int BOARD_WIDTH = 1200; // Width of the game board
    private static final int BOARD_HEIGHT = 800; // Height of the game board
    private static final int COLUMNS = BOARD_WIDTH / 50; // Number of columns in the game board
    private static final int ROWS = BOARD_HEIGHT / 50; // Number of rows in the game board
    private static final int BLOCK_SIZE = BOARD_HEIGHT / ROWS; // Size of each block on the game board

    /** Colors **/
    private static final String BLOCK_COLOR_1 = "#22223b"; // Color Block 1
    private static final String BLOCK_COLOR_2 = "#22223b"; // Color Block 2
    private static final String SNAKE_COLOR = "#69F0AE"; // Snake Color
    private static final String TEXT_COLOR = "#69F0AE"; // Text Color

    /** Food Objects **/
    private int foodX; // Food X-coordinate
    private int foodY; // Food Y-coordinate
    private Image foodImage; // Food Image
    private int foodImageIndex = 0;  // Initialize a counter for the current index in the FOODS_IMAGE array
    private static final String[] FOODS_IMAGE = new String[]{ // Array of food images
            "file:src/img/icon_chicken.png",
            "file:src/img/icon_turtle.png"
    };

    /** Snake Objects **/
    private Point snakeHead; // The head of the snake
    private final List<Point> snakeBody = new ArrayList<>(); // The body of the snake

    /** Directions **/
    private int currentDirection; // The current direction of the snake
    private static final int RIGHT = 0; // Direction right
    private static final int LEFT = 1; // Direction left
    private static final int UP = 2; // Direction up
    private static final int DOWN = 3; // Direction down

    /** Game objects **/
    private GraphicsContext gc; // Graphics context of the game board
    private Scene scene; // Scene of the game
    private int score = 0; // The score of the player
    private boolean start = true; // If the game has started
    private boolean gameOver = false; // If the game is over
    private Timeline timeline = new Timeline(); // Timeline for the game
    private static final int SPEED = 300; // Speed of the game



    @Override

    /**
     * %%%%%%%%%%%%% Initializes the game and sets up the game board  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */
    public void start(Stage primaryStage) {
        // Set up the game window
        setUp(primaryStage);

        // Set up the initial game state
        setUpGame();

        // Set up the game loop
        setUpGameLoop();

        // Set up the key handler for player input
        setUpKeyHandler();

        System.out.println(" BOARD_WIDTH " + BOARD_WIDTH + " COLUMNS " + COLUMNS);
        System.out.println(" BOARD_HEIGHT " + BOARD_HEIGHT  + " ROWS " + ROWS);

    }


    /**
     * %%%%%%%%%%%%% sets up the game window and creates a canvas to draw on %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    private void setUp(Stage primaryStage) {
        // Set the title of the game window
        primaryStage.setTitle("Snake Game");

        // Create a root group to hold the canvas
        Group root = new Group();

        // Create a canvas to draw the game on
        Canvas canvas = new Canvas(BOARD_WIDTH, BOARD_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Add the canvas to the root group
        root.getChildren().add(canvas);

        // Create a new scene with the root group and set it as the primary stage's scene
        scene = new Scene(root);
        primaryStage.setScene(scene);

        // Display the game window
        primaryStage.show();
    }


    /**
     * %%%%%%%%%%%%% sets up the game Object (Snake , Food)  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Sets up the initial state of the game, including the snake's starting position and the food's location.
    private void setUpGame() {
        // Clear the snake body list
        snakeBody.clear();

        // Add the starting points of the snake body
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(3, ROWS / 2));

            // #########  Debugging line  ##############
            System.out.println("Start Body element: " + i + " - Coordinate : " + snakeBody.get(i));
        }

        // Set the snake head to the first element of the snake body list
            snakeHead = snakeBody.get(0);

        // #########  Debugging line  ##############
            System.out.println("Start Head element: 0 - Coordinate : " + snakeBody.get(0));

        // Generate the first food
        generateFood();
    }

    /**
     * %%%%%%%%%%%%% Set up the game loop to repeatedly call the run method  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    private void setUpGameLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(SPEED), e -> run(gc))); // Create a timeline For Graphics Context and defined the speed
        timeline.setCycleCount(Animation.INDEFINITE); // Set cycle count to indefinite
        timeline.play(); // Start the timeline
    }

    /**
     * %%%%%%%%%%%%% Set up the Function key to Restart, Pause, Resume %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Restart the game
    private void restart() {
        start = true; // Set start to true
        gameOver = false; // Set gameOver to false
        score = 0; // Reset the score

        setUpGame(); // Set up the game again
    }

    // Pause the game
    private void pause() {
        timeline.stop(); // Stop the timeline
        start = false; // Set start to false
    }

    // Resume the game
    private void resume() {
        timeline.play(); // Start the timeline
        start = true; // Set start to true
    }



    /**
     * %%%%%%%%%%%%% Updates the current direction of the snake  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

     // Updates the current direction of the snake to the new direction,
     // if the new direction is not opposite of the current direction.
     // @param newDirection the new direction to set the snake to
     // @param oppositeDirection the opposite direction of the new direction

    private void updateDirectionIfNotOpposite(int newDirection, int oppositeDirection) {
        // Check if the current direction is not opposite to the new direction
        if (currentDirection != oppositeDirection) {
            // If it's not opposite, update the current direction to the new direction
            currentDirection = newDirection;
        }
    }

    /**
     * %%%%%%%%%%%%% Sets the direction of the snake based on the user's input %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // @param code the key code corresponding to the user's input
    private void setDirection(KeyCode code) {
        // Determine the direction based on the user's input
        switch (code) {
            case RIGHT:
            case D:
                // If the user input is right, set the direction to right
                updateDirectionIfNotOpposite(RIGHT, LEFT);
                break;
            case LEFT:
            case A:
                // If the user input is left, set the direction to left
                updateDirectionIfNotOpposite(LEFT, RIGHT);
                break;
            case UP:
            case W:
                // If the user input is up, set the direction to up
                updateDirectionIfNotOpposite(UP, DOWN);
                break;
            case DOWN:
            case S:
                // If the user input is down, set the direction to down
                updateDirectionIfNotOpposite(DOWN, UP);
                break;
            default:
                // If the user input is not recognized, do nothing
                break;
        }
    }


    /**
     * %%%%%%%%%%%%% Set up the key handler to handle key events %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    private void setUpKeyHandler() {
        scene.setOnKeyPressed(event -> handleKeyEvent(event.getCode())); // Set the scene's on key pressed event handler
    }

    // Handle key events
    private void handleKeyEvent(KeyCode code) {
        if (gameOver && code == KeyCode.R) { // If game is over and R key is pressed
            restart(); // Restart the game
        }

        if (start && code == KeyCode.SPACE) { // If game has started and SPACE key is pressed
            pause(); // Pause the game
        } else if (!start && code == KeyCode.SPACE) { // If game is paused and SPACE key is pressed
            resume(); // Resume the game
        }

        if (code == KeyCode.ESCAPE) { // If ESCAPE key is pressed
            System.exit(0); // Exit the game
        }

        setDirection(code); // Set the direction based on the key pressed
    }


    /**
     * %%%%%%%%%%%%% Draw Background %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */
    private void drawBackground(GraphicsContext gc) {
        // loop through each row and column to draw the background
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                // check if the sum of row and column is even or odd to create a checkerboard pattern
                if ((i + j) % 2 == 0) {
                    // set color to BLOCK_COLOR_1 for even sum
                    gc.setFill(Color.web(BLOCK_COLOR_1));
                } else {
                    // set color to BLOCK_COLOR_2 for odd sum
                    gc.setFill(Color.web(BLOCK_COLOR_2));
                }
                // draw a rectangle for each block with BLOCK_SIZE
                gc.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }


    /**
     * %%%%%%%%%%%%% Draw Snake %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    private void drawSnake(GraphicsContext gc) {
        // Set the fill color to the color of the snake
        gc.setFill(Color.web(SNAKE_COLOR));

        // Draw the head of the snake
        gc.fillRoundRect(snakeHead.getX() * BLOCK_SIZE, snakeHead.getY() * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1, 50, 50);

        // #########  Debugging line  ##############
        System.out.println("Draw Head element: 0 - Coordinate : " + snakeHead);


        // Draw the body of the snake
        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * BLOCK_SIZE, snakeBody.get(i).getY() * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1, 25, 25);

            // #########  Debugging line  ##############
            System.out.println("Draw Body element: " + i + " - Coordinate : " + snakeBody.get(i));
        }

        // #########  Debugging line  ##############
        // System.out.println(snakeBody);

    }


    /**
     * %%%%%%%%%%%%% Move Snake  Head %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Move the snake to the right by incrementing its x-coordinate
    private void moveRight() {
        snakeHead.x++;
    }

    // Move the snake to the left by decrementing its x-coordinate
    private void moveLeft() {
        snakeHead.x--;
    }

    // Move the snake up by decrementing its y-coordinate
    private void moveUp() {
        snakeHead.y--;
    }

    // Move the snake down by incrementing its y-coordinate
    private void moveDown() {
        snakeHead.y++;
    }

    /**
     * %%%%%%%%%%%%% Move Snake through the walls %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */


    // Allow the snake to move through the walls and appear on the opposite side of the board
    private void moveThroughWall() {
        // If the snake goes off the left edge of the board, wrap around to the right edge
        if (snakeHead.x < 0) {
            snakeHead.x = BOARD_WIDTH / BLOCK_SIZE - 1;
        }
        // If the snake goes off the right edge of the board, wrap around to the left edge
        if (snakeHead.x >= BOARD_WIDTH / BLOCK_SIZE) {
            snakeHead.x = 0;
        }
        // If the snake goes off the top edge of the board, wrap around to the bottom edge
        if (snakeHead.y < 0) {
            snakeHead.y = BOARD_HEIGHT / BLOCK_SIZE - 1;
        }
        // If the snake goes off the bottom edge of the board, wrap around to the top edge
        if (snakeHead.y >= BOARD_HEIGHT / BLOCK_SIZE) {
            snakeHead.y = 0;
        }
    }



    /**
     * %%%%%%%%%%%%% If snake eat Food %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */
    // Check if the snake's head is at the same position as the food and eat it
    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            // Add a new body part to the snake and generate new food
            snakeBody.add(new Point(-1, -1));
            generateFood();
            // Increase score by 1
            score += 1;
        }
    }

    /**
     * %%%%%%%%%%%%% Game Over - When had touches body %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Check if the snake has collided with its own body and set game over flag if it has
    public void gameOver() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    /**
     * %%%%%%%%%%%%% Draw food image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */
    // Draw the food image at the current food position
    private void drawFood(GraphicsContext gc) {
        gc.drawImage(foodImage, foodX * BLOCK_SIZE, foodY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    /**
     * %%%%%%%%%%%%% Generate new random food  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Generate new food position until it doesn't overlap with the snake's body
    private void generateFood() {
        // Use a label to break out of nested loops
        newFoodPosition:
        while (true) {
            // Randomly choose new food position
            foodX = (int) (Math.random() * (COLUMNS - 1));
            foodY = (int) (Math.random() * (ROWS - 1));

            // If either foodX or foodY equals 1, generate a new position
            if (foodX == 0 || foodY == 0 || foodX == 1 || foodY == 1) {
                continue newFoodPosition;
            }

            System.out.println("%%%%%%  Food position X %%%%%%%%%% " + foodX);
            System.out.println("%%%%%%  Food position Y %%%%%%%%%% " + foodY);

            // Check if food position overlaps with any part of the snake's body
            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    // If the food position overlaps, start generating a new position
                    continue newFoodPosition;
                }
            }


            // Choose the next food image in the FOODS_IMAGE array
            foodImage = new Image(FOODS_IMAGE[foodImageIndex % FOODS_IMAGE.length]);

            // Increment the foodImageIndex for the next iteration
            foodImageIndex++;

            break;
        }
    }






    /**
     * %%%%%%%%%%%%% Header %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Method to draw the player's score
    private void drawScore() {
        // Set the fill color to a semi-transparent black color
        gc.setFill(Color.web("#000000", 0.5));
        // Fill a rectangle with black color starting at (0,0) with the width of the scene and the height of 50 pixels
        gc.fillRect(0, 0, scene.getWidth(), 50);
        // Set the fill color to the text color
        gc.setFill(Color.web(TEXT_COLOR));
        // Set the font to bold with size 20
        gc.setFont(Font.font("default", FontWeight.BOLD, 20));
        // Draw the player's score starting at (10, 35)
        gc.fillText("SCORE: " + score, 10, 35);
    }

    /**
     * %%%%%%%%%%%%% Footer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    // Method to draw key information for the player
    private void drawKeyInfo() {
        // Set the fill color to a semi-transparent black color
        gc.setFill(Color.web("#000000", 0.5));
        // Fill a rectangle with black color starting at (0, scene height - 35) with the width of the scene and the height of 50 pixels
        gc.fillRect(0, scene.getHeight() - 35, scene.getWidth(), 50);
        // Set the fill color to the text color
        gc.setFill(Color.web(TEXT_COLOR));
        // Set the font to size 12
        gc.setFont(Font.font("default", 12));
        // Draw the information about game controls starting at (10, scene height - 15)
        gc.fillText("〚 **** IYAD ALOUDAT **** 〛〚 Restart : R 〛 〚 Exit : ESC 〛〚 Paus : SPACE 〛", 15, scene.getHeight() - 15);
    }






    /**
     * %%%%%%%%%%%%% Play The Game %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */

    /**
    This method is the main loop of the game, it updates the state of the game, moves the snake,
     checks for collisions, and draws the game elements on the canvas.
     It takes a GraphicsContext object as a parameter to draw the game elements on the canvas.
    **/

    // ########## Play Game  ##########
    private void run(GraphicsContext gc) {
        // Move the body of the snake
        // Move each point in the snake's body to the position of the previous point
        // Update the snakeBody array starting from the tail and ending at the head
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            // Update the x-coordinate of the current element with the x-coordinate of the previous element
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            // Update the y-coordinate of the current element with the y-coordinate of the previous element
            snakeBody.get(i).y = snakeBody.get(i - 1).y;

            // #########  Debugging line  ##############
            System.out.println("index: " + i + " Point : " + snakeBody.get(i) + " New x : " + snakeBody.get(i - 1).x);
        }

        // Switch statement that checks the value of the currentDirection variable.

        switch (currentDirection) {
            // If the value of currentDirection is RIGHT, call the moveRight method.
            case RIGHT:
                moveRight();
                break;
            // If the value of currentDirection is LEFT, call the moveLeft method.
            case LEFT:
                moveLeft();
                break;
            // If the value of currentDirection is UP, call the moveUp method.
            case UP:
                moveUp();
                break;
            // If the value of currentDirection is DOWN, call the moveDown method.
            case DOWN:
                moveDown();
                break;
            // If the value of currentDirection is none of the above, throw an IllegalStateException.
            default:
                throw new IllegalStateException("Unexpected value: " + currentDirection);
        }


        // If the game is over, show the "Game Over" message and return
        if (gameOver) {
            gc.setFill(Color.web(TEXT_COLOR));
            gc.setFont(Font.font("default", FontWeight.BOLD, 80));
            gc.fillText("Game Over", BOARD_WIDTH / 4, BOARD_HEIGHT / 2);
            return;
        }

        // Render the game
        drawBackground(gc);  // Draw the background
        drawFood(gc);  // Draw the food
        eatFood();  // Check if the snake has eaten the food
        drawSnake(gc);  // Draw the snake
        drawScore();  // Draw the score
        drawKeyInfo();  // Draw the key information
        moveThroughWall();  // Move the snake through the wall
        gameOver();  // Check if the game is over
    }

    public static void main(String[] args) {
        launch(args);
    }
}