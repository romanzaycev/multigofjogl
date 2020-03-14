package ru.romanzaycev.multigoljogl;

public interface Board {
    void setAlive(int x, int y);

    void setAliveLinearized(int xy);

    void setDead(int x, int y);

    void setDeadLinearized(int xy);

    boolean isAlive(int x, int y);

    boolean isAliveLinearized(int xy);

    int getAliveCountN(int x, int y);

    int getAliveCountNLinearized(int xy);

    int getX(int xy);

    int getY(int xy);

    int getLinearizedXY(int x, int y);

    int getWidth();

    int getHeight();
}
