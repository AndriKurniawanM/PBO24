package Space;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static Space.Commons.*;

public class Board extends Canvas {

    private final List<Alien> aliens = new ArrayList<>();
    private final Random rand = new Random();
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;
    private boolean inGame = true;
    private String message = "Game Over";

    public Board() {
        setWidth(BOARD_WIDTH);
        setHeight(BOARD_HEIGHT);

        initGame();

        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT)  player.dx = -PLAYER_SPEED;
            if (e.getCode() == KeyCode.RIGHT) player.dx =  PLAYER_SPEED;
            if (e.getCode() == KeyCode.SPACE && inGame && !shot.visible) {
                shot = new Shot(player.x + PLAYER_WIDTH/2, player.y);
            }
        });
        setOnKeyReleased(e -> {
            if (e.getCode()==KeyCode.LEFT || e.getCode()==KeyCode.RIGHT)
                player.dx = 0;
        });

        Timeline loop = new Timeline(new KeyFrame(Duration.millis(GAME_LOOP_MS), e -> {
            if (inGame) updateGame();
            draw();
        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    private void initGame() {
        aliens.clear();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 6; j++)
                aliens.add(new Alien(ALIEN_INIT_X + 18*j, ALIEN_INIT_Y + 18*i));
        player = new Player((BOARD_WIDTH-PLAYER_WIDTH)/2, PLAYER_Y);
        shot = new Shot();
        inGame = true;
        deaths = 0;
        direction = -1;
        message = "Game Over";
    }

    private void updateGame() {
        // win?
        if (deaths == NUMBER_OF_ALIENS) {
            inGame = false;
            message = "You Win!";
            return;
        }

        // move player
        player.move();

        // move shot
        if (shot.visible) {
            shot.y -= SHOT_SPEED;
            if (shot.y + SHOT_HEIGHT < 0) shot.visible = false;
            else {
                // check hit alien
                for (Alien a : aliens) {
                    if (a.visible &&
                            shot.x >= a.x && shot.x <= a.x+ALIEN_WIDTH &&
                            shot.y >= a.y && shot.y <= a.y+ALIEN_HEIGHT) {
                        a.visible = false;
                        shot.visible = false;
                        deaths++;
                        break;
                    }
                }
            }
        }

        // move aliens down at edges
        for (Alien a : aliens) {
            if (a.visible) {
                if (a.x >= BOARD_WIDTH - BORDER_RIGHT && direction>0) {
                    direction = -1; dropAliens();
                }
                if (a.x <= BORDER_LEFT && direction<0) {
                    direction = +1; dropAliens();
                }
                break;
            }
        }
        // advance aliens
        for (Alien a : aliens)
            if (a.visible) a.x += direction;

        // aliens reach ground?
        for (Alien a: aliens) {
            if (a.visible && a.y + ALIEN_HEIGHT >= GROUND) {
                inGame = false;
                message = "Invasion!";
            }
        }

        // bombs
        for (Alien a: aliens) {
            if (a.visible && a.bomb.destroyed &&
                    rand.nextInt(100) < CHANCE) {
                a.bomb.drop(a.x+ALIEN_WIDTH/2, a.y+ALIEN_HEIGHT);
            }
            Bomb b = a.bomb;
            if (!b.destroyed) {
                b.y += BOMB_SPEED;
                // hit player?
                if (b.x >= player.x && b.x <= player.x+PLAYER_WIDTH &&
                        b.y >= player.y && b.y <= player.y+PLAYER_HEIGHT) {
                    inGame = false;
                    message = "You Died";
                }
                // hit ground
                if (b.y >= GROUND) b.destroyed = true;
            }
        }
    }

    private void dropAliens() {
        for (Alien a: aliens)
            a.y += GO_DOWN;
    }

    private void draw() {
        GraphicsContext g = getGraphicsContext2D();
        // clear
        g.setFill(Color.BLACK);
        g.fillRect(0,0, BOARD_WIDTH, BOARD_HEIGHT);

        if (inGame) {
            // ground line
            g.setStroke(Color.GREEN);
            g.strokeLine(0, GROUND, BOARD_WIDTH, GROUND);

            // draw aliens
            g.setFill(Color.LIME);
            for (Alien a: aliens)
                if (a.visible)
                    g.fillRect(a.x, a.y, ALIEN_WIDTH, ALIEN_HEIGHT);

            // draw player
            g.setFill(Color.CORNFLOWERBLUE);
            g.fillRect(player.x, player.y, PLAYER_WIDTH, PLAYER_HEIGHT);

            // draw shot
            if (shot.visible) {
                g.setFill(Color.YELLOW);
                g.fillRect(shot.x, shot.y, SHOT_WIDTH, SHOT_HEIGHT);
            }

            // draw bombs
            g.setFill(Color.CRIMSON);
            for (Alien a: aliens) {
                Bomb b = a.bomb;
                if (!b.destroyed)
                    g.fillOval(b.x-BOMB_RADIUS, b.y-BOMB_RADIUS,
                            BOMB_RADIUS*2, BOMB_RADIUS*2);
            }
        } else {
            // game over / win screen
            g.setFill(Color.BLACK);
            g.fillRect(0,0, BOARD_WIDTH, BOARD_HEIGHT);
            g.setFill(Color.WHITE);
            g.setFont(Font.font(24));
            g.fillText(message,
                    BOARD_WIDTH/2 - message.length()*6,
                    BOARD_HEIGHT/2);
            g.setFont(Font.font(14));
            g.fillText("Press R to Restart",
                    BOARD_WIDTH/2 - 60,
                    BOARD_HEIGHT/2 + 30);
        }
    }

    // --- sprite inner classes ---

    private static class Alien {
        int x,y;
        boolean visible = true;
        final Bomb bomb = new Bomb();
        Alien(int x,int y){ this.x=x; this.y=y; }
    }

    private static class Bomb {
        int x,y;
        boolean destroyed = true;
        void drop(int x,int y){ this.x=x; this.y=y; destroyed=false; }
    }

    private static class Player {
        int x,y,dx=0;
        Player(int x,int y){ this.x=x; this.y=y; }
        void move(){
            x += dx;
            if (x < BORDER_LEFT) x = BORDER_LEFT;
            if (x > BOARD_WIDTH-2*PLAYER_WIDTH) x = BOARD_WIDTH-2*PLAYER_WIDTH;
        }
    }

    private static class Shot {
        int x,y;
        boolean visible = false;
        Shot(){}
        Shot(int x,int y){ this.x=x; this.y=y; visible=true; }
    }
}
