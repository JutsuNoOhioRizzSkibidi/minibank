package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockedAccountRegistryTest {
    @Test
    void BlockedAccountIsRecognized(){
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");
        registry.block(number);
        assertTrue(registry.isBlocked(new AccountNumber("0000000001")));
    }
    @Test
    void blockingSameAccountTwiceDoesNotCreateDupl(){
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        registry.block(new AccountNumber("0000000001"));
        registry.block(new AccountNumber("0000000001"));
        assertEquals(1, registry.size());
    }
    @Test
    void unblockRemovesAccountNumber(){
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");
        registry.block(number);
        registry.unblock(new AccountNumber("0000000001"));
        assertFalse(registry.isBlocked(number));
        assertEquals(0, registry.size());
    }
    @Test
    void unknownAccountIsNotBlocked(){
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        registry.block(new AccountNumber("0000000001"));
        boolean blocked = registry.isBlocked(new AccountNumber("0000000002"));
        assertFalse(blocked);
    }
}
