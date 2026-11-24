package bank;

import bank.adapters.TransactionFileExporter;
import bank.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionFileExporterTest{
    @Test
    void exportCreatesFile(@TempDir Path tempDir) throws IOException{
        TransactionFileExporter exporter = new TransactionFileExporter();

        Transaction tx1 = new Transaction(
                1L,
                TransactionType.TRANSFER,
                new AccountNumber("1234567890"),
                1000,
                TransactionStatus.SUCCESS
        );

        Transaction tx2 = new Transaction(
                2L,
                TransactionType.TRANSFER,
                new AccountNumber("1234567891"),
                2000,
                TransactionStatus.SUCCESS
        );

        Path path = tempDir.resolve("reports").resolve("transactions.txt");
        exporter.export(path, List.of(tx1, tx2));
        assertTrue(Files.exists(path));
        List<String> lines = Files.readAllLines(path);
        assertEquals(3, lines.size());
        assertEquals("id;from;to;amount;status", lines.get(0));
        assertEquals("1;1234567890;;1000.0;SUCCESS", lines.get(1));
        assertEquals("2;1234567891;;2000.0;SUCCESS", lines.get(2));
    }
}