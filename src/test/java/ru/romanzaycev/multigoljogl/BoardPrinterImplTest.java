package ru.romanzaycev.multigoljogl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class BoardPrinterImplTest {
    @Test
    public void print() {
        // Arrange
        Board board = new BoardImpl(3, 3);
        board.setAlive(1, 0);
        board.setAlive(1, 1);
        board.setAlive(1, 2);

        // Act
        BoardPrinter cut = new BoardPrinterImpl();
        String result = cut.print(board);

        // Assert
        assertThat(result).isEqualTo("_*_\n_*_\n_*_\n");
    }
}
