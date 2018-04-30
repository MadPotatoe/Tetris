/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vmalonso
 */
public class Shape {

    private Tetrominoes pieceShape;
    private int coords[][];
    private static final int[][][] coordsTable = new int[][][]{
        {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
        {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
        {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
        {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
        {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
        {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
        {{1, -1}, {0, -1}, {0, 0}, {0, 1}}

    };

    public Shape() {
        coords = new int[4][2];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = coordsTable[Tetrominoes.NoShape.ordinal()][i][j];
            }
        }
        pieceShape = Tetrominoes.NoShape;
    }

    public void setShape(Tetrominoes shapeType) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = coordsTable[shapeType.ordinal()][i][j];
            }
        }
        pieceShape = shapeType;
    }
    
    public void setRandomShape() {
        int number = (int) (Math.random() * 7 + 1);
        Tetrominoes[] values = Tetrominoes.values();
        setShape(values[number]);
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int getX(int index) {
        return coords[index][0];
    }

    public int getY(int index) {
        return coords[index][1];
    }

    public Tetrominoes getShape() {
        return pieceShape;
    }

    public int minX() {
        int minim = coords[0][0];
        for (int i = 1; i < 4; i++) {
            minim = Math.min(minim, coords[i][0]);
            /*if (coords[i][0]<minim) {
				minim = coords[i][0];
			} */
        }
        return minim;
    }

    public int minY() {
        int minim = coords[0][1];
        for (int i = 1; i < 4; i++) {
            minim = Math.min(minim, coords[i][1]);
            /*if (coords[i][0]<minim) {
				minim = coords[i][0];
			} */
        }
        return minim;
    }

    public int maxX() {
        int max = coords[0][0];
        for (int i = 1; i < 4; i++) {
            max = Math.max(max, coords[i][0]);
            /*if (coords[i][0]>max) {
				max = coords[i][0];
			} */
        }
        return max;
    }

    public int maxY() {
        int max = coords[0][1];
        for (int i = 1; i < 4; i++) {
            max = Math.max(max, coords[i][1]);
            /*if (coords[i][0]>max) {
				max = coords[i][0];
			} */
        }
        return max;
    }

    public Shape rotateRight() {
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }
        Shape newShape = new Shape();
        newShape.setShape(pieceShape);
        for (int i = 0; i < 4; i++) {
            newShape.setX(i, getY(i));
            newShape.setY(i, -getX(i));
        }
        return newShape;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape) {
            return this;
        }
        Shape newShape = new Shape();
        newShape.setShape(pieceShape);
        for (int i = 0; i < 4; i++) {
            newShape.setX(i, -getY(i));
            newShape.setY(i, getX(i));
        }
        return newShape;
    }

}
