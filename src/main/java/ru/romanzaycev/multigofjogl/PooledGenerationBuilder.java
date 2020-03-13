package ru.romanzaycev.multigofjogl;

import java.util.ArrayList;
import java.util.List;

public class PooledGenerationBuilder implements GenerationBuilder {
    private final int threads;

    public PooledGenerationBuilder(int threads) {
        this.threads = threads;
    }

    @Override
    public Board getNextGeneration(Board currentGeneration) {
        final int width = currentGeneration.getWidth();
        final int height = currentGeneration.getHeight();
        final int size = width * height;
        final int chunkSize = (int) Math.ceil(size / threads);
        final Board nextGeneration = new BoardImpl(width, height);
        int s = size;

        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            s -= chunkSize;
            Thread t = new Thread(
                    new AlgoThread(
                            currentGeneration,
                            nextGeneration,
                            chunkSize * i,
                            (s > 0) ? chunkSize : chunkSize + s
                    )
            );
            t.start();
            list.add(t);
        }

        for (Thread thread : list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return nextGeneration;
    }

    class AlgoThread implements Runnable {
        private Board currentGeneration;
        private Board nextGeneration;
        private int start;
        private int chunkSize;

        AlgoThread(Board currentGeneration, Board nextGeneration, int start, int chunkSize) {
            this.currentGeneration = currentGeneration;
            this.nextGeneration = nextGeneration;
            this.start = start;
            this.chunkSize = chunkSize;
        }

        @Override
        public void run() {
            GofAlgorithm.calculateChunk(chunkSize, start, currentGeneration, nextGeneration);
        }
    }
}
