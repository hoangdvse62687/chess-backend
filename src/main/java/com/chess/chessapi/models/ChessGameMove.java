package com.chess.chessapi.models;

public class ChessGameMove {
    private int status;
    private int turnPlayer;
    private String move;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(int turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
