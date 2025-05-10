package Pacman;

public interface Commons {
    int BLOCK_SIZE    = 24;      // ukuran satu kotak
    int N_BLOCKS      = 15;      // jumlah kotak per sisi
    int SCREEN_SIZE   = BLOCK_SIZE * N_BLOCKS;
    int PAC_SPEED     = 6;       // kecepatan pixel per frame
    int GHOST_SPEED   = 4;
    int PAC_ANIM_DELAY= 4;       // frame per langkah animasi
    int MAX_GHOSTS    = 6;
    int INIT_LIVES    = 3;
    int INIT_GHOSTS   = 4;
    int GAME_LOOP_MS  = 40;
}
