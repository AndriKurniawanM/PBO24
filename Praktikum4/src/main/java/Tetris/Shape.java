package Tetris;
import java.util.Random;

public class Shape {
    public enum Tetrominoe {
        NoShape, ZShape, SShape, LineShape,
        TShape, SquareShape, LShape, MirroredLShape
    }

    private Tetrominoe pieceShape;
    private int[][] coords = new int[4][2];

    public Shape() {
        setShape(Tetrominoe.NoShape);
    }

    public void setShape(Tetrominoe shape) {
        // template koordinat semua bentuk
        int[][][] coordsTable = {
                {{0,0},{0,0},{0,0},{0,0}},    // NoShape
                {{0,-1},{0,0},{-1,0},{-1,1}}, // Z
                {{0,-1},{0,0},{1,0},{1,1}},   // S
                {{0,-1},{0,0},{0,1},{0,2}},   // Line
                {{-1,0},{0,0},{1,0},{0,1}},   // T
                {{0,0},{1,0},{0,1},{1,1}},    // Square
                {{-1,-1},{0,-1},{0,0},{0,1}}, // L
                {{1,-1},{0,-1},{0,0},{0,1}}   // MirroredL
        };
        for (int i = 0; i < 4; i++) {
            coords[i][0] = coordsTable[shape.ordinal()][i][0];
            coords[i][1] = coordsTable[shape.ordinal()][i][1];
        }
        pieceShape = shape;
    }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        setShape(Tetrominoe.values()[x]);
    }

    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetrominoe getShape() { return pieceShape; }
    public int minX() {
        int m = coords[0][0];
        for (int i = 1; i < 4; i++) m = Math.min(m, coords[i][0]);
        return m;
    }
    public int minY() {
        int m = coords[0][1];
        for (int i = 1; i < 4; i++) m = Math.min(m, coords[i][1]);
        return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) return this;
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; i++) {
            result.coords[i][0] =  y(i);
            result.coords[i][1] = -x(i);
        }
        return result;
    }

    public Shape rotateRight() {
        if (pieceShape == Tetrominoe.SquareShape) return this;
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; i++) {
            result.coords[i][0] = -y(i);
            result.coords[i][1] =  x(i);
        }
        return result;
    }
}
