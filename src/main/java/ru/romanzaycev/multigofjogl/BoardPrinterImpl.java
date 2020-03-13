package ru.romanzaycev.multigofjogl;

public class BoardPrinterImpl implements BoardPrinter {
    @Override
    public String print(Board board) {
        final int W = board.getWidth();
        final int H = board.getHeight();
        final int SIZE = W * H;
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            char c = board.isAliveLinearized(i) ? '*' : '_';

            builder.append(c);

            if ((i + 1) % W == 0) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
