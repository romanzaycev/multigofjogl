package ru.romanzaycev.multigofjogl;

public class GenerationBuilderImpl implements GenerationBuilder {
    @Override
    public Board getNextGeneration(Board currentGeneration) {
        final int width = currentGeneration.getWidth();
        final int height = currentGeneration.getHeight();

        Board nextGeneration = new BoardImpl(width, height);
        GofAlgorithm.calculateChunk(width * height, 0, currentGeneration, nextGeneration);

        return nextGeneration;
    }
}
