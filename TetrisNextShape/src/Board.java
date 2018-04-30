
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vmalonso
 */
public class Board extends javax.swing.JPanel {

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (tryToMove(currentShape,
                            currentColumn - 1, currentRow)
                            && timer.isRunning()) {
                        currentColumn--;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (tryToMove(currentShape,
                            currentColumn + 1, currentRow)
                            && timer.isRunning()) {
                        currentColumn++;
                    }
                    break;
                case KeyEvent.VK_UP:
                    Shape piece = currentShape.rotateRight();
                    if (tryToMove(piece,
                            currentColumn, currentRow)
                            && timer.isRunning()) {
                        currentShape = piece;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    while (tryToMove(currentShape, currentColumn, currentRow + 1)
                            && timer.isRunning()) {
                        currentRow++;
                    }
                    break;
                case KeyEvent.VK_P:
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                default:
                    break;
            }
            repaint();
        }
    }

    private static final int COLUMNS = 10;
    private static final int ROWS = 22;
    private Tetrominoes[][] matrixBoard;
    private int currentRow;
    private int currentColumn;
    private Shape currentShape;
    private ScoreBoard scoreBoard;
    private Timer timer;
    private boolean gameOver;
    MyKeyAdapter keyAdepter;
    private int speed;
    private int lines;
    private ControllerBestScores controller;
    private BestScores score;
    private Config config;
    private Shape nextShape;
    private NextShape nextShapeController;

    /**
     * Creates new form Board
     */
    public Board(ScoreBoard scoreBoard, NextShape nextShapeController) throws IOException, FileNotFoundException,
            ClassNotFoundException {
        initComponents();
        config = config.getInstance();
        this.scoreBoard = scoreBoard;
        this.speed = config.getSpeed();
        matrixBoard = new Tetrominoes[ROWS][COLUMNS];
        setFocusable(true);
     //   controller = new ControllerBestScores();
        this.nextShapeController = nextShapeController;

    }

    private void createCurrentShape() {
        currentRow = 0;
        currentColumn = COLUMNS / 2;
        if (currentShape == null) {
            currentShape = new Shape();
            nextShape = new Shape();
            currentShape.setRandomShape();
            nextShape.setRandomShape();
            nextShapeController.setShape(nextShape);
        } else {
            currentShape.setShape(nextShape.getShape());
            nextShape.setRandomShape();
            nextShapeController.setShape(nextShape);
        }
    }

    private void initMatrixBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                matrixBoard[i][j] = Tetrominoes.NoShape;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (currentShape != null) {
            drawCurrentShape(g);
        }
        nextShapeController.repaint();
    }

    private void drawBoard(Graphics g) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                drawSquare(g, i, j, matrixBoard[i][j]);
            }
        }
    }

    private void drawCurrentShape(Graphics g) {
        for (int i = 0; i < 4; i++) {
            drawSquare(g, currentRow + currentShape.getY(i),
                    currentColumn + currentShape.getX(i),
                    currentShape.getShape());
        }
    }

    public void initGame() {

        initMatrixBoard();
        createCurrentShape();
        scoreBoard.resetScoreBoard();

        keyAdepter = new MyKeyAdapter();
        addKeyListener(keyAdepter);
        gameOver = false;
        lines = 0;

        timer = new Timer(speed, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (tryToMove(currentShape, currentColumn,
                        currentRow + 1)) {
                    currentRow++;
                } else if (gameOver) {
                    processGameOver();
                } else {
                    movePieceToBoard(currentShape, matrixBoard, currentRow, currentColumn);
                    createCurrentShape();
                }
                repaint();
            }
        });
        timer.start();
    }

    private void movePieceToBoard(Shape piece,
            Tetrominoes[][] board,
            int row, int column) {
        int newRow, newColumn;
        for (int i = 0; i < 4; i++) {
            newRow = row + piece.getY(i);
            newColumn = column + piece.getX(i);
            if ((newRow >= 0) && (newRow < ROWS) && (newColumn >= 0)
                    && (newColumn < COLUMNS)) {
                board[newRow][newColumn] = piece.getShape();
            }
        }
        detectCompletedLines();
    }

    private void detectCompletedLines() {
        Boolean completed;
        for (int i = 0; i < ROWS; i++) {
            completed = true;
            for (int j = 0; j < COLUMNS; j++) {
                if (matrixBoard[i][j] == Tetrominoes.NoShape) {
                    completed = false;
                    break;
                }
            }
            if (completed) {
                lines++;
                System.out.println("" + lines);
                removeLine(matrixBoard, i);
                scoreBoard.incrementScore();
                if (lines % 10 == 0 && scoreBoard.getLevel() <= 10) {
                    changeLevel();
                    scoreBoard.incrementLevel();
                }
            }
        }
    }

    public void changeLevel() {
        timer.setDelay(config.getSpeed());
    }

    private void removeLine(Tetrominoes[][] board, int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = board[i - 1][j];
            }
        }
        for (int j = 0; j < COLUMNS; j++) {
            board[0][j] = Tetrominoes.NoShape;
        }
    }

    private boolean tryToMove(Shape piece, int column, int row) {
        if (row + piece.maxY() >= ROWS) {
            return false;
        }
        if ((column + piece.minX() < 0) || (column + piece.maxX() > COLUMNS - 1)) {
            return false;
        }
        // Detect collision with the rest of the matrixBoard
        for (int i = 0; i < 4; i++) {
            int pointRow = row + piece.getY(i);
            int pointColumn = column + piece.getX(i);
            if ((pointRow >= 0) && (pointRow < ROWS)
                    && (pointColumn >= 0) && (pointColumn < COLUMNS)) {
                if (matrixBoard[pointRow][pointColumn] != Tetrominoes.NoShape) {
                    if (currentRow < 1) {
                        gameOver = true;
                    }
                    return false;
                }
            }
        }

        return true;
    }

    private void drawSquare(Graphics g, int row, int col, Tetrominoes shape) {
        Color colors[] = {new Color(0, 0, 0),
            new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
        };
        int x = col * squareWidth();
        int y = row * squareHeight();
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2,
                squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1,
                y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    private int squareWidth() {
        return getWidth() / COLUMNS;
    }

    private int squareHeight() {
        return getHeight() / ROWS;
    }

    private void processGameOver() {
        currentShape = null;
        timer.stop();
        removeKeyListener(keyAdepter);
        timer = new Timer(10, new ActionListener() {
            int row = 0;
            int col = 0;
            int increment = 1;

            @Override
            public void actionPerformed(ActionEvent e) {

                matrixBoard[row][col] = Tetrominoes.LineShape;
                col += increment;
                if (col > COLUMNS - 1) {
                    row++;
                    increment = -1;
                    col--;
                } else if (col < 0) {
                    row++;
                    col = 0;
                    increment = 1;
                }
                if (row == ROWS) {
                    timer.stop();
                    scoreBoard.setGameOver();
                    int n = JOptionPane.showConfirmDialog(
                            Board.this, "Do you want Start again?",
                            "Start again?", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        initGame();
                    } else {
                        System.exit(0);
                    }

                }
                repaint();
            }
        });
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
