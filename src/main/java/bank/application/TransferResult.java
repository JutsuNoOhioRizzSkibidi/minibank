package bank.application;

public record TransferResult(
        long requestId,
        TransferStatus status,
        String message
){}