package bank;

import bank.adapters.JsonStorage;
import bank.adapters.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonStorageTest{
    @Test
    void roundTripTest(@TempDir Path tempDir) throws IOException{
        JsonStorage storage = new JsonStorage();
        List<AccountDto> source = List.of(
                new AccountDto("1234567890", "sanya", 10000, "DebitAccount"),
                new AccountDto("1234567891", "sanya1", 2000, "DebitAccount")
        );

        Path path = tempDir.resolve("accounts.json");
        storage.saveAccounts(path, source);
        List<AccountDto> restored = storage.loadAccounts(path);
        assertTrue(Files.exists(path));
        assertEquals(source, restored);
    }

    @Test
    void emptyListTest(@TempDir Path tempDir) throws IOException{
        JsonStorage storage = new JsonStorage();
        List<AccountDto> source = List.of();
        Path path = tempDir.resolve("accounts.json");

        storage.saveAccounts(path, source);
        List<AccountDto> restored = storage.loadAccounts(path);
        assertEquals(source, restored);
        assertTrue(restored.isEmpty());
    }

    @Test
    void differentAccountTypesTest(@TempDir Path tempDir) throws IOException{
        JsonStorage storage = new JsonStorage();

        List<AccountDto> source = List.of(
                new AccountDto("1234567890", "sanya", 10000, "DebitAccount"),
                new AccountDto("1234567891", "sanya1", 20000, "SavingsAccount"),
                new AccountDto("1234567892", "sanya2", 30000, "CreditAccount")
        );

        Path path = tempDir.resolve("accounts.json");
        storage.saveAccounts(path, source);
        List<AccountDto> restored = storage.loadAccounts(path);
        assertEquals(source, restored);
        assertEquals(3, restored.size());
    }
    @Test
    void invalidJsonTest(@TempDir Path tempDir) throws IOException{
        JsonStorage storage = new JsonStorage();
        Path path = tempDir.resolve("accounts.json");
        Files.writeString(path, "{invalid json");
        assertThrows(IOException.class,
                () -> storage.loadAccounts(path));
    }
    @Test
    void missingFileTest(@TempDir Path tempDir){
        JsonStorage storage = new JsonStorage();
        Path path = tempDir.resolve("missing.json");
        assertThrows(IOException.class,
                () -> storage.loadAccounts(path));
    }
}