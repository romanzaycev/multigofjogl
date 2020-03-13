package ru.romanzaycev.multigofjogl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class BoardImplTest {

    @Test
    public void isDeadByDefault() {
        // Arrange
        int x = 0;
        int y = 0;

        // Act
        BoardImpl cut = new BoardImpl(3, 3);

        // Assert
        assertThat(cut.isAlive(x, y)).isFalse();
    }

    @Test
    public void setAlive() {
        // Arrange
        int x = 0;
        int y = 0;

        // Act
        BoardImpl cut = new BoardImpl(3, 3);
        cut.setAlive(x, y);

        // Assert
        assertThat(cut.isAlive(x, y)).isTrue();
    }

    @Test
    public void setAliveLinearized() {
        // Arrange
        int xy = 1;

        // Act
        BoardImpl cut = new BoardImpl(3, 3);
        cut.setAliveLinearized(xy);

        // Assert
        assertThat(cut.isAliveLinearized(xy)).isTrue();
    }

    @Test
    public void setDead() {
        // Arrange
        int x = 0;
        int y = 0;

        // Act
        BoardImpl cut = new BoardImpl(3, 3);
        cut.setAlive(x, y);

        // Assert
        assertThat(cut.isAlive(x, y)).isTrue();
        cut.setDead(x, y);
        assertThat(cut.isAlive(x, y)).isFalse();
    }

    @Test
    public void getAliveCount() {
        // Arrange
        int x = 1;
        int y = 0;

        // Act
        BoardImpl cut = new BoardImpl(3, 3);

        // Assert
        assertThat(cut.getAliveCountN(x, y)).isEqualTo(0);
        cut.setAlive(0, 0);
        assertThat(cut.getAliveCountN(x, y)).isEqualTo(1);
    }

    @Test
    public void getX() {
        // Arrange
        int width = 5;
        int height = 3;

        // Act
        BoardImpl cut = new BoardImpl(width, height);

        // Assert
        assertThat(cut.getX(0)).isEqualTo(0);
        assertThat(cut.getX(1)).isEqualTo(1);
        assertThat(cut.getX(2)).isEqualTo(2);
        assertThat(cut.getX(3)).isEqualTo(3);
        assertThat(cut.getX(4)).isEqualTo(4);
        assertThat(cut.getX(5)).isEqualTo(0);
        assertThat(cut.getX(6)).isEqualTo(1);
    }

    @Test
    public void getY() {
        // Arrange
        int width = 5;
        int height = 3;

        // Act
        BoardImpl cut = new BoardImpl(width, height);

        // Assert
        assertThat(cut.getY(0)).isEqualTo(0);
        assertThat(cut.getY(1)).isEqualTo(0);
        assertThat(cut.getY(2)).isEqualTo(0);
        assertThat(cut.getY(3)).isEqualTo(0);
        assertThat(cut.getY(4)).isEqualTo(0);
        assertThat(cut.getY(5)).isEqualTo(1);
        assertThat(cut.getY(6)).isEqualTo(1);
        assertThat(cut.getY(7)).isEqualTo(1);
        assertThat(cut.getY(8)).isEqualTo(1);
        assertThat(cut.getY(9)).isEqualTo(1);
        assertThat(cut.getY(10)).isEqualTo(2);
    }

    @Test
    public void getLinearizedXY() {
        // Arrange
        int width = 3;
        int height = 7;

        // Act
        BoardImpl cut = new BoardImpl(width, height);

        // Assert
        assertThat(cut.getLinearizedXY(0, 0)).isEqualTo(0);
        assertThat(cut.getLinearizedXY(1, 0)).isEqualTo(1);
        assertThat(cut.getLinearizedXY(0, 1)).isEqualTo(3);
    }
}
