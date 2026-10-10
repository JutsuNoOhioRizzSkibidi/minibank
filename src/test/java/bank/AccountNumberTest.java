package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountNumberTest {
    @Test
    void validAccountNumberPreservesValue(){
        String value = "0000000001";
        AccountNumber number = new AccountNumber(value);
        assertEquals(value, number.value());
    }
    @Test
    void cantCreateAccountNumberWithNull() {
        String value = null;
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountNumber(value)
        );
    }
    @Test
    void cantCreateAccountNumberWithEmptyValue() {
        String value = "";
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountNumber(value)
        );
    }
    @Test
    void cantCreateAccountNumberShorterTen() {
        String value = "000000001";
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountNumber(value)
        );
    }
    @Test
    void cantCreateAccountNumberLongerTen() {
        String value = "00000000000001";
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountNumber(value)
        );
    }
    @Test
    void cantCreateAccountNumberWithLetters() {
        String value = "000000000A";
        assertThrows(
                IllegalArgumentException.class,
                () -> new AccountNumber(value)
        );
    }
}
