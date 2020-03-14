package ru.romanzaycev.multigoljogl;

public class BoardImpl implements Board {
    private final int width;
    private final int height;
    private final int[] state;

    private final static int U_ALIVE = 1;

    private final static int U_0 = 1 << 1;
    private final static int U_1 = 1 << 2;
    private final static int U_2 = 1 << 3;
    private final static int U_3 = 1 << 4;
    private final static int U_4 = 1 << 5;
    private final static int U_5 = 1 << 6;
    private final static int U_6 = 1 << 7;
    private final static int U_7 = 1 << 8;

    private final static int[] xLine = {-1,  0,  1,  1,  1,  0, -1, -1};
    private final static int[] yLine = {-1, -1,  -1,  0,  1,  1,  1,  0};

    private final static int[] nPos    =  {  4,      5,      6,      7,      0,      1,      2,      3};
    private final static int[] nPosVal =  {U_4,    U_5,    U_6,    U_7,    U_0,    U_1,    U_2,    U_3};

    public BoardImpl(int width, int height) {
        this.width = width;
        this.height = height;
        state = new int[width * height];
    }

    @Override
    public void setAlive(int x, int y) {
        setAliveLinearized(getLinearizedXY(x, y));
    }

    @Override
    public void setAliveLinearized(int xy) {
        state[xy] |= U_ALIVE;
        int x = getX(xy);
        int y = getY(xy);

        for (int i = 0; i < 8; i++) {
            int xN = xLine[i] + x;
            int yN = yLine[i] + y;

            if (xN >= 0 && xN < width && yN >= 0 && yN < height) {
                state[getLinearizedXY(xN, yN)] |= nPosVal[nPos[i]];
            }
        }
    }

    @Override
    public void setDead(int x, int y) {
        setDeadLinearized(getLinearizedXY(x, y));
    }

    @Override
    public void setDeadLinearized(int xy) {
        state[xy] &= ~ U_ALIVE;
        int x = getX(xy);
        int y = getY(xy);

        for (int i = 0; i < 8; i++) {
            int xN = xLine[i] + x;
            int yN = yLine[i] + y;

            if (xN >= 0 && xN < width && yN >= 0 && yN < height) {
                state[getLinearizedXY(xN, yN)] &= ~ nPosVal[nPos[i]];
            }
        }
    }

    @Override
    public boolean isAlive(int x, int y) {
        return isAliveLinearized(getLinearizedXY(x, y));
    }

    @Override
    public boolean isAliveLinearized(int xy) {
        return (state[xy] & U_ALIVE) == U_ALIVE;
    }

    @Override
    public int getAliveCountN(int x, int y) {
        return getAliveCountNLinearized(getLinearizedXY(x, y));
    }

    @Override
    public int getAliveCountNLinearized(int xy) {
        int s = state[xy];

        if (s == U_ALIVE || s == 0) {
            return 0;
        }

        int r = 0;

        r += ((s & U_0) == U_0) ? 1 : 0;
        r += ((s & U_1) == U_1) ? 1 : 0;
        r += ((s & U_2) == U_2) ? 1 : 0;
        r += ((s & U_3) == U_3) ? 1 : 0;
        r += ((s & U_4) == U_4) ? 1 : 0;
        r += ((s & U_5) == U_5) ? 1 : 0;
        r += ((s & U_6) == U_6) ? 1 : 0;
        r += ((s & U_7) == U_7) ? 1 : 0;

        return r;
    }

    @Override
    public int getX(int xy) {
        return xy % width;
    }

    @Override
    public int getY(int xy) {
        return (int)Math.ceil(xy / width);
    }

    @Override
    public int getLinearizedXY(int x, int y) {
        if (x == 0 && y == 0) {
            return 0;
        }

        return width * y + x;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
