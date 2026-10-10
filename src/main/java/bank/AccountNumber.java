package bank;

public record AccountNumber(String value){
    public AccountNumber{
        if (value == null || !value.matches("\\d{10}")) {
            throw new IllegalArgumentException("номер счета должен содержать 10 цифр ровно");
        }
    }
}