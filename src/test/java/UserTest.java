
import com.example.CAB302_EuclidSolver.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private static final Integer USER1_USERID = 2;
    private static final Integer USER2_USERID = 4;
    private static final String USER1_USERNAME = "michael";
    private static final String USER2_USERNAME = "XAlexX";
    private static final String USER1_EMAIL = "michael@gmail.com";
    private static final String USER2_EMAIL = "alex@gmail.com";
    private static final String USER1_PASSWORD = "123456";
    private static final String USER2_PASSWORD = "f#22hd";



    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = new User(USER1_USERID, USER1_USERNAME, USER1_EMAIL, USER1_PASSWORD);
        user2 = new User(USER2_USERID, USER2_USERNAME, USER2_EMAIL, USER2_PASSWORD);
    }

    /*=====================================*/
    /* User Properties GETTER/SETTER Tests */
    /*=====================================*/

    @Test
    public void testGetUserID1() {
        assertEquals(USER1_USERID, user1.getUserID());
    }
    @Test
    public void testGetUserID2() {
        assertEquals(USER2_USERID, user2.getUserID());
    }

    @Test
    public void testGetUsername1() {
        assertEquals(USER1_USERNAME, user1.getUsername());
    }
    @Test
    public void testSetUsername1() {
        user1.setUsername(USER2_USERNAME);
        assertEquals(USER2_USERNAME, user1.getUsername());
    }
    @Test
    public void testGetUsername2() {
        assertEquals(USER2_USERNAME, user2.getUsername());
    }
    @Test
    public void testSetUsername2() {
        user2.setUsername(USER1_USERNAME);
        assertEquals(USER1_USERNAME, user2.getUsername());
    }

    @Test
    public void testGetEmail1() {
        assertEquals(USER1_EMAIL, user1.getEmail());
    }
    @Test
    public void testSetEmail1() {
        user1.setEmail(USER2_EMAIL);
        assertEquals(USER2_EMAIL, user1.getEmail());
    }
    @Test
    public void testGetEmail2() {
        assertEquals(USER2_EMAIL, user2.getEmail());
    }
    @Test
    public void testSetEmail2() {
        user2.setEmail(USER1_EMAIL);
        assertEquals(USER1_EMAIL, user2.getEmail());
    }

    @Test
    public void testGetPassword1() {
        assertEquals(USER1_PASSWORD, user1.getPassword());
    }
    @Test
    public void testSetPassword1() {
        user1.setPassword(USER2_PASSWORD);
        assertEquals(USER2_PASSWORD, user1.getPassword());
    }
    @Test
    public void testGetPassword2() {
        assertEquals(USER2_PASSWORD, user2.getPassword());
    }
    @Test
    public void testSetPassword2() {
        user2.setPassword(USER1_PASSWORD);
        assertEquals(USER1_PASSWORD, user2.getPassword());
    }


    /*===========================*/
    /* User Public Methods Tests */
    /*===========================*/

    @Test
    public void testUserToString1() {
        assertEquals("User{" +
            "userID=" + USER1_USERID +
            ", username='" + USER1_USERNAME + "'" +
            ", email='" + USER1_EMAIL + "'" +
            ", password=" + USER1_PASSWORD + "'" +
            '}', user1.toString());
    }
    @Test
    public void testUserToString2() {
        assertEquals("User{" +
            "userID=" + USER2_USERID +
            ", username='" + USER2_USERNAME + "'" +
            ", email='" + USER2_EMAIL + "'" +
            ", password=" + USER2_PASSWORD + "'" +
            '}', user2.toString());
    }

    /*=============================*/
    /* User Constructor EXCEPTIONS */
    /*=============================*/

    @Test
    void testConstructorThrowsOnNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(1, null, USER1_EMAIL, USER1_PASSWORD);
        });
    }
    @Test
    void testConstructorThrowsOnEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(2, "", USER1_EMAIL, USER1_PASSWORD);
        });
    }
    @Test
    void testConstructorThrowsOnNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(1, USER1_USERNAME, null, USER1_PASSWORD);
        });
    }
    @Test
    void testConstructorThrowsOnEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(2, USER1_USERNAME, "", USER1_PASSWORD);
        });
    }
    @Test
    void testConstructorThrowsOnNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(1, USER1_USERNAME, USER1_EMAIL, null);
        });
    }
    @Test
    void testConstructorThrowsOnEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(2, USER1_USERNAME, USER1_EMAIL, "");
        });
    }
}

