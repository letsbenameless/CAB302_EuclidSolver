
import com.CAB302_EuclidSolver.model.user.SessionException;
import com.CAB302_EuclidSolver.model.user.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    private UserSession session;

    @BeforeEach
    void setUp() {
        session = UserSession.getInstance();
        // reset between tests
        session.TESTreset();
    }

    @Test
    void testSingletonInstance() {
        UserSession s1 = UserSession.getInstance();
        UserSession s2 = UserSession.getInstance();
        assertSame(s1, s2);
    }

    @Test
    void testNotLoggedInInitially() {
        assertFalse(session.isLoggedIn());
    }

    @Test
    void testLoginStoresUsername() {
        session.TESTlogin("Andre");
        assertTrue(session.isLoggedIn());
        assertEquals("Andre", session.getUsername());
    }

    @Test
    void testLogoutClearsSession() {
        session.TESTlogin("Andre");
        session.TESTlogout(); // simpler logout that doesn’t load scene
        assertFalse(session.isLoggedIn());
    }

    @Test
    void testGetUsernameThrowsWhenNotLoggedIn() {
        assertThrows(SessionException.class, () -> session.getUsername());
    }
}
