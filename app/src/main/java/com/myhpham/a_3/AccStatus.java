package com.myhpham.a_3;

public class AccStatus {
    boolean moved;
    float X;
    float Y;

    public AccStatus(boolean moved, float x, float y) {
        this.moved = moved;
        this.X = x;
        this.Y = y;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}

