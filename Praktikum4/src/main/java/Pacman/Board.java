package Pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Random;
import Pacman.Commons;
import Tetris.Shape.Tetrominoe; 

    public class Board extends Canvas implements Commons {

        private short[] screenData    = new short[N_BLOCKS*N_BLOCKS];
        private final short[] levelData = {
                19,26,26,26,18,18,18,18,18,18,18,18,18,18,22,
                21, 0, 0, 0,17,16,16,16,16,16,16,16,16,16,20,
                21, 0, 0, 0,17,16,16,16,16,16,16,16,16,16,20,
                21, 0, 0, 0,17,16,16,24,16,16,16,16,16,16,20,
                17,18,18,18,16,16,20, 0,17,16,16,16,16,16,20,
                17,16,16,16,16,16,20, 0,17,16,16,16,16,24,20,
                25,16,16,16,24,24,28, 0,25,24,24,16,20, 0,21,
                1,17,16,20, 0, 0, 0, 0, 0, 0, 0,17,20, 0,21,
                1,17,16,16,18,18,22, 0,19,18,18,16,20, 0,21,
                1,17,16,16,16,16,20, 0,17,16,16,16,20, 0,21,
                1,17,16,16,16,16,20, 0,17,16,16,16,20, 0,21,
                1,17,16,16,16,16,16,18,16,16,16,16,20, 0,21,
                1,17,16,16,16,16,16,16,16,16,16,16,20, 0,21,
                1,25,24,24,24,24,24,24,24,24,16,16,16,18,20,
                9, 8, 8, 8, 8, 8, 8, 8, 8, 8,25,24,24,24,28
        };

        private int pacmanX, pacmanY, reqDX, reqDY, viewDX, viewDY;
        private int pacAnimCount = PAC_ANIM_DELAY, pacAnimDir = 1, pacAnimPos = 0;
        private boolean inGame = false, dying = false;
        private int score = 0, lives = INIT_LIVES;

        private int nGhosts = INIT_GHOSTS;
        private int[] ghostX = new int[MAX_GHOSTS], ghostY = new int[MAX_GHOSTS];
        private int[] ghostDX = new int[MAX_GHOSTS], ghostDY = new int[MAX_GHOSTS];
        private Random rand = new Random();

        public Board() {
            super(SCREEN_SIZE, SCREEN_SIZE+BLOCK_SIZE);
            initLevel();
            initGame();
            setupInput();
            startGameLoop();
        }

        private void initLevel() {
            System.arraycopy(levelData, 0, screenData, 0, levelData.length);
        }

        private void initGame() {
            inGame = false;
            dying = false;
            score = 0;
            lives = INIT_LIVES;
            pacmanX = 7*BLOCK_SIZE; pacmanY = 11*BLOCK_SIZE;
            reqDX = viewDX = -1; reqDY = viewDY = 0;
            for (int i=0;i<nGhosts;i++) {
                ghostX[i]=4*BLOCK_SIZE; ghostY[i]=4*BLOCK_SIZE;
                ghostDX[i]=(i%2==0?1:-1); ghostDY[i]=0;
            }
        }

        private void setupInput() {
            setFocusTraversable(true);
            setOnKeyPressed(e -> {
                KeyCode code = e.getCode();
                if (inGame) {
                    if      (code==KeyCode.LEFT)  { reqDX=-1; reqDY=0; }
                    else if (code==KeyCode.RIGHT) { reqDX= 1; reqDY=0; }
                    else if (code==KeyCode.UP)    { reqDX=0; reqDY=-1; }
                    else if (code==KeyCode.DOWN)  { reqDX=0; reqDY= 1; }
                    else if (code==KeyCode.ESCAPE) inGame=false;
                    else if (code==KeyCode.P)      inGame=!inGame;
                } else {
                    if (code==KeyCode.S) inGame=true;
                }
            });
        }

        private void startGameLoop() {
            Timeline loop = new Timeline(
                    new KeyFrame(Duration.millis(GAME_LOOP_MS), e->gameCycle())
            );
            loop.setCycleCount(Timeline.INDEFINITE);
            loop.play();
        }

        private void gameCycle() {
            if (inGame) {
                if (dying) {
                    lives--;
                    if (lives==0) inGame=false;
                    else {
                        dying=false;
                        pacmanX = 7*BLOCK_SIZE; pacmanY = 11*BLOCK_SIZE;
                        reqDX=viewDX=-1; reqDY=0;
                    }
                } else {
                    movePacman();
                    moveGhosts();
                    checkMaze();
                }
                animatePacman();
            }
            doDrawing();
        }

        private void movePacman() {
            int pos = pacmanX/BLOCK_SIZE + N_BLOCKS*(pacmanY/BLOCK_SIZE);
            short bits = screenData[pos];
            if ((bits&16)!=0) {
                screenData[pos] = (short)(bits & 15);
                score++;
            }
            if (canMove(pacmanX, pacmanY, reqDX, reqDY)) {
                viewDX=reqDX; viewDY=reqDY;
            }
            if (canMove(pacmanX,pacmanY,viewDX,viewDY)) {
                pacmanX += viewDX*PAC_SPEED;
                pacmanY += viewDY*PAC_SPEED;
            }
        }

        private boolean canMove(int x, int y, int dx, int dy) {
            if (dx==0 && dy==0) return true;
            int nx = x + dx*BLOCK_SIZE/2;
            int ny = y + dy*BLOCK_SIZE/2;
            int c = nx/BLOCK_SIZE + N_BLOCKS*(ny/BLOCK_SIZE);
            return (screenData[c] & (dx==-1?1:dx==1?4:dy==-1?2:8))==0;
        }

        private void moveGhosts() {
            for (int i=0;i<nGhosts;i++) {
                int x = ghostX[i], y=ghostY[i];
                if (x%BLOCK_SIZE==0 && y%BLOCK_SIZE==0) {
                    int pos = x/BLOCK_SIZE + N_BLOCKS*(y/BLOCK_SIZE);
                    int[] dirsX={-1,0,1,0}, dirsY={0,-1,0,1}, choices=new int[4];
                    int count=0;
                    for (int d=0;d<4;d++) {
                        if ((screenData[pos]&(1<<d))==0 &&
                                !(dirsX[d]==-ghostDX[i] && dirsY[d]==-ghostDY[i])) {
                            choices[count++]=d;
                        }
                    }
                    if (count>0) {
                        int d = choices[rand.nextInt(count)];
                        ghostDX[i]=dirsX[d];
                        ghostDY[i]=dirsY[d];
                    } else {
                        ghostDX[i]=-ghostDX[i];
                        ghostDY[i]=-ghostDY[i];
                    }
                }
                ghostX[i]+=ghostDX[i]*GHOST_SPEED;
                ghostY[i]+=ghostDY[i]*GHOST_SPEED;
                // collision?
                if (Math.abs(ghostX[i]-pacmanX)<BLOCK_SIZE/2 &&
                        Math.abs(ghostY[i]-pacmanY)<BLOCK_SIZE/2) {
                    dying=true;
                }
            }
        }

        private void checkMaze() {
            boolean finished=true;
            for (short b: screenData) {
                if ((b&16)!=0) { finished=false; break; }
            }
            if (finished) {
                score+=50;
                inGame=false; // cukup restart
            }
        }

        private void animatePacman() {
            pacAnimCount--;
            if (pacAnimCount<=0) {
                pacAnimCount=PAC_ANIM_DELAY;
                pacAnimPos+=pacAnimDir;
                if (pacAnimPos==3 || pacAnimPos==0) pacAnimDir=-pacAnimDir;
            }
        }

        private void doDrawing() {
            GraphicsContext g = getGraphicsContext2D();
            // background
            g.setFill(Color.BLACK);
            g.fillRect(0,0, SCREEN_SIZE, SCREEN_SIZE+BLOCK_SIZE);
            // maze
            drawMaze(g);
            // pacman
            drawPacman(g);
            // ghosts
            for (int i=0;i<nGhosts;i++) drawGhost(g,ghostX[i],ghostY[i]);
            // score & lives
            g.setFill(Color.WHITE);
            g.fillText("Score: "+score, 5, SCREEN_SIZE+16);
            g.fillText("Lives: "+lives, SCREEN_SIZE-60, SCREEN_SIZE+16);
            if (!inGame) {
                g.fillText("Press S to start", SCREEN_SIZE/2-40, SCREEN_SIZE/2);
            }
        }

        private void drawMaze(GraphicsContext g) {
            g.setStroke(Color.DARKGREEN);
            g.setLineWidth(2);
            int i=0;
            for(int y=0;y<SCREEN_SIZE;y+=BLOCK_SIZE){
                for(int x=0;x<SCREEN_SIZE;x+=BLOCK_SIZE){
                    short b = screenData[i++];
                    if ((b&1)!=0) g.strokeLine(x,y,x,y+BLOCK_SIZE);
                    if ((b&2)!=0) g.strokeLine(x,y,x+BLOCK_SIZE,y);
                    if ((b&4)!=0) g.strokeLine(x+BLOCK_SIZE,y,x+BLOCK_SIZE,y+BLOCK_SIZE);
                    if ((b&8)!=0) g.strokeLine(x,y+BLOCK_SIZE,x+BLOCK_SIZE,y+BLOCK_SIZE);
                    if ((b&16)!=0) {
                        g.setFill(Color.GOLD);
                        g.fillOval(x+BLOCK_SIZE/2-3, y+BLOCK_SIZE/2-3, 6, 6);
                    }
                }
            }
        }

        private void drawPacman(GraphicsContext g) {
            double x = pacmanX+1, y=pacmanY+1;
            int open = pacAnimPos * 15; // derajat bukaan
            double startAngle = 0;
            if (viewDX<0)      startAngle=180+open/2;
            else if (viewDX>0) startAngle=0+open/2;
            else if (viewDY<0) startAngle=90+open/2;
            else               startAngle=270+open/2;
            g.setFill(Color.YELLOW);
            g.fillArc(x, y, BLOCK_SIZE-2, BLOCK_SIZE-2,
                    startAngle, 360-open, javafx.scene.shape.ArcType.ROUND);
        }

        private void drawGhost(GraphicsContext g, int x, int y) {
            g.setFill(Color.PINK);
            g.fillOval(x+2, y+2, BLOCK_SIZE-4, BLOCK_SIZE-4);
            g.setFill(Color.WHITE);
            g.fillOval(x+6, y+6, 6, 6);
            g.fillOval(x+BLOCK_SIZE-12, y+6, 6, 6);
            g.setFill(Color.BLACK);
            g.fillOval(x+8, y+8, 2, 2);
            g.fillOval(x+BLOCK_SIZE-10, y+8, 2, 2);
        }
    }
