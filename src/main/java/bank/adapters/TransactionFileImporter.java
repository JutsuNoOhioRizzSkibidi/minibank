package bank.adapters;

import bank.adapters.dto.TransactionDto;
import bank.domain.TransactionStatus;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileImporter{
    public List<TransactionDto> load(Path path) throws IOException{
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<TransactionDto> transactions = new ArrayList<>();
        if (lines.isEmpty()){
            throw new IOException("файл пустой");
        }
        if (!lines.get(0).equals("id;from;to;amount;status")){
            throw new IOException("неверный заголовок файла");
        }
        for (int i = 1; i < lines.size(); i++){
            String[] parts = lines.get(i).split(";", -1);

            if (parts.length != 5){
                throw new IOException("неверное количество полей в строке " + (i + 1));
            }
            try{
                long id = Long.parseLong(parts[0]);
                String from = parts[1];
                String to = parts[2];
                double amount = Double.parseDouble(parts[3]);
                TransactionStatus status = TransactionStatus.valueOf(parts[4]);

                if (from.isBlank() || !Double.isFinite(amount)){
                    throw new IllegalArgumentException("неверные данные");
                }
                TransactionDto dto = new TransactionDto(
                        id,
                        from,
                        to,
                        amount,
                        status.name()
                );
                transactions.add(dto);
            }
            catch (IllegalArgumentException e){
                throw new IOException("ошибка в строке " + (i + 1), e);
            }
        }
        return transactions;
    }
}