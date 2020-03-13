package ru.romanzaycev.multigofjogl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class GenerationBuilderImplTest {
    @Test
    public void getNextGeneration() {
        // Arrange
        Board cGeneration = new BoardImpl(3, 3);
        cGeneration.setAlive(1, 0);
        cGeneration.setAlive(1, 1);
        cGeneration.setAlive(1, 2);
        BoardPrinter printer = new BoardPrinterImpl();

        // Act
        GenerationBuilder cut = new GenerationBuilderImpl();
        Board nGeneration = cut.getNextGeneration(cGeneration);

        String cGenerationPrint = printer.print(cGeneration);
        String nGenerationPrint = printer.print(nGeneration);

        // Assert
        assertThat(cGenerationPrint).isEqualTo("_*_\n_*_\n_*_\n");
        assertThat(nGenerationPrint).isEqualTo("___\n***\n___\n");
    }
}
