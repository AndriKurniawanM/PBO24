package Sokoban;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

import static Sokoban.Commons.*;

public class Board extends Canvas {

    private final String level =
            "    ######\n"
                    + "    ##   #\n"
                    + "    ##$  #\n"
                    + "  ####  $##\n"
                    + "  ##  $ $ #\n"
                    + "#### # ## #   ######\n"
                    + "##   # ## #####  ..#\n"
                    + "## $  $          ..#\n"
                    + "###### ### #@##  ..#\n"
                    + "    ##     #########\n"
                    + "    ########\n";

    private final List<Point> walls  = new ArrayList<>();
    private final List<Point> baggs  = new ArrayList<>();
    private final List<Point> areas  = new ArrayList<>();
    private int sokoRow, sokoCol;
    private int nRows, nCols;
    private boolean completed = false;

    public Board() {
        parseLevel();
        setWidth(OFFSET*2 + nCols*SPACE);
        setHeight(OFFSET*2 + nRows*SPACE + 20);
        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (completed && e.getCode()!=KeyCode.R) return;
            switch (e.getCode()) {
                case LEFT  -> move(0, -1);
                case RIGHT -> move(0, +1);
                case UP    -> move(-1, 0);
                case DOWN  -> move(+1, 0);
                case R     -> { completed=false; parseLevel();
                }
                
            }
            draw();
        });
        draw();
    }

    private void parseLevel() {
        walls.clear(); baggs.clear(); areas.clear();
        nRows = 0; nCols = 0;
        int row=0, col=0;
        for (char c: level.toCharArray()) {
            if (c=='\n') {
                row++; col=0;
            } else {
                switch(c) {
                    case '#' -> walls.add(new Point(row,col));
                    case '$' -> baggs.add(new Point(row,col));
                    case '.' -> areas.add(new Point(row,col));
                    case '@' -> { sokoRow=row; sokoCol=col; }
                    default  -> {}
                }
                col++;
                nCols = Math.max(nCols, col);
            }
            nRows = Math.max(nRows, row+1);
        }
    }

    private void move(int dr, int dc) {
        if (completed) return;
        int nr = sokoRow + dr, nc = sokoCol + dc;
        if (walls.contains(new Point(nr,nc))) return;

        Point next = new Point(nr,nc);
        if (baggs.contains(next)) {
            // coba dorong
            Point beyond = new Point(nr+dr, nc+dc);
            if (walls.contains(beyond) || baggs.contains(beyond)) return;
            // geser box
            baggs.remove(next);
            baggs.add(beyond);
        }
        // geser player
        sokoRow = nr; sokoCol = nc;
        checkCompleted();
    }

    private void checkCompleted() {
        completed = areas.stream().allMatch(areas::contains) &&
                baggs.stream().allMatch(areas::contains);
    }

    private void draw() {
        GraphicsContext g = getGraphicsContext2D();
        // background
        g.setFill(Color.BEIGE);
        g.fillRect(0,0, getWidth(), getHeight());

        // walls
        g.setFill(Color.DARKGRAY);
        walls.forEach(p ->
                g.fillRect(OFFSET + p.col*SPACE, OFFSET + p.row*SPACE, SPACE, SPACE)
        );

        // areas
        g.setFill(Color.LIGHTGREEN);
        areas.forEach(p ->
                g.fillRect(OFFSET + p.col*SPACE+2, OFFSET + p.row*SPACE+2, SPACE-4, SPACE-4)
        );

        // baggage
        g.setFill(Color.SADDLEBROWN);
        baggs.forEach(p ->
                g.fillRect(OFFSET + p.col*SPACE+4, OFFSET + p.row*SPACE+4, SPACE-8, SPACE-8)
        );

        // player
        g.setFill(Color.DEEPSKYBLUE);
        g.fillOval(OFFSET + sokoCol*SPACE+4, OFFSET + sokoRow*SPACE+4, SPACE-8, SPACE-8);

        // completed text
        if (completed) {
            g.setFill(Color.BLACK);
            g.setFont(Font.font(20));
            g.fillText("Completed!", 25, 25);
        }
    }

    // helper for grid coords
    private static class Point {
        final int row, col;
        Point(int row, int col) { this.row=row; this.col=col; }
        @Override public boolean equals(Object o){
            if (!(o instanceof Point p)) return false;
            return p.row==row && p.col==col;
        }
        @Override public int hashCode(){ return row*31 + col; }
    }
}
