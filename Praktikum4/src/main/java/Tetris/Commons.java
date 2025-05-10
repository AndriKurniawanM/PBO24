package Tetris;

public interface Commons {
    int BOARD_WIDTH    = 10;   // jumlah kolom
    int BOARD_HEIGHT   = 22;   // jumlah baris
    int BLOCK_SIZE     = 20;   // ukuran satu kotak dalam pixel
    int WINDOW_WIDTH   = BOARD_WIDTH * BLOCK_SIZE;
    int WINDOW_HEIGHT  = BOARD_HEIGHT * BLOCK_SIZE;
    int GAME_SPEED_MS  = 400;  // kecepatan turun (ms)
}

