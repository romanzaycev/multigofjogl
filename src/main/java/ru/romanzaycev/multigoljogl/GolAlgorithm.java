package ru.romanzaycev.multigoljogl;

public class GolAlgorithm {
    public static void calculateChunk(int chunkSize, int start, Board currentGeneration, Board nextGeneration) {
        final int size = start + chunkSize;

        for (int i = start; i < size; i++) {
            int n = currentGeneration.getAliveCountNLinearized(i);
            boolean isAlive = currentGeneration.isAliveLinearized(i);

            if (isAlive && (n == 2 || n == 3) || !isAlive && n == 3) {
                nextGeneration.setAliveLinearized(i);
            } else {
                nextGeneration.setDeadLinearized(i);
            }
        }
    }
}
