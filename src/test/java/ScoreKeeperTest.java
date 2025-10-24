import com.CAB302_EuclidSolver.model.question.scoreKeeper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreKeeperTest {

    private scoreKeeper keeper;

    @BeforeEach
    void setUp() {
        keeper = new scoreKeeper(5);
    }

    @Test
    void testInitialState() {
        assertEquals(0, keeper.getCorrect());
        assertEquals(0, keeper.getAttempted());
        assertEquals(5, keeper.getTotal());
        assertEquals(0.0, keeper.getPercent());
        assertEquals("0/5 (0.0%)", keeper.summary());
    }

    @Test
    void testRecordCorrectAnswer() {
        keeper.record(true);
        assertEquals(1, keeper.getCorrect());
        assertEquals(1, keeper.getAttempted());
        assertEquals(20.0, keeper.getPercent());
        assertEquals("1/5 (20.0%)", keeper.summary());
    }

    @Test
    void testRecordIncorrectAnswer() {
        keeper.record(false);
        assertEquals(0, keeper.getCorrect());
        assertEquals(1, keeper.getAttempted());
        assertEquals(0.0, keeper.getPercent());
        assertEquals("0/5 (0.0%)", keeper.summary());
    }

    @Test
    void testMixedAnswers() {
        keeper.record(true);
        keeper.record(false);
        keeper.record(true);

        assertEquals(2, keeper.getCorrect());
        assertEquals(3, keeper.getAttempted());
        assertEquals(40.0, keeper.getPercent());
        assertEquals("2/5 (40.0%)", keeper.summary());
    }

    @Test
    void testPercentRounding() {
        scoreKeeper keeper10 = new scoreKeeper(3);
        keeper10.record(true);
        assertEquals(33.3, keeper10.getPercent(), 0.1);
        assertEquals("1/3 (33.3%)", keeper10.summary());
    }
}
