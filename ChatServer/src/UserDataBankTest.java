import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDataBankTest {

    @Test
    void checkLoginInfo() {

        UserDataBank db = new UserDataBank();

        assertTrue(db.CheckLoginInfo("Hans;1234", "1"));
        assertTrue(db.CheckLoginInfo("Peter;P@ssw0rt", "2"));
        assertTrue(db.CheckLoginInfo("Helga;helga666", "3"));

        assertFalse(db.CheckLoginInfo("test;test", "4"));
        assertFalse(db.CheckLoginInfo("null;null", "5"));
        assertFalse(db.CheckLoginInfo("true", "6"));
        assertFalse(db.CheckLoginInfo("test;test;test", "7"));
    }
}