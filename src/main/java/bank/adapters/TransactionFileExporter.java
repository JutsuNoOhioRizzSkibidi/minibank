package bank.adapters;

import bank.adapters.dto.TransactionDto;
import bank.domain.Transaction;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileExporter{
    public void export(Path path, List<Transaction> transactions) throws IOException{
        if (path.getParent() != null){
            Files.createDirectories(path.getParent());
        }
        List<String> lines = new ArrayList<>();
        lines.add("id;from;to;amount;status");
        lines.addAll(transactions.stream()
                .map(TransactionDto::fromDomain)
                .map(dto -> dto.id() + ";" + dto.from() + ";" + dto.to() + ";" +
                        dto.amount() + ";" + dto.status())
                .toList());
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}