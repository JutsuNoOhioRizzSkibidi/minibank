package bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TransferServiceTest {
    @Test
    void successfulTransferUpdatesBothBalances() {
        BankAccount from =
                new DebitAccount("111", "john", 10000);
        BankAccount to =
                new DebitAccount("222", "pork", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 3000);
        assertTrue(result);
        assertEquals(7000.0, from.getBalance(), 0.01);
        assertEquals(5000.0, to.getBalance(), 0.01);
    }
    @Test
    void insufficientFundsLeavesBothBalancesUnchanged() {
        BankAccount from = new DebitAccount("333", "oleg", 1000);
        BankAccount to = new DebitAccount("444", "maria", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 3000);
        assertFalse(result);
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferNegAmount(){
        BankAccount from =
                new DebitAccount("201", "Vasya", 1000);
        BankAccount to =
                new DebitAccount("202", "Anya", 2000);
        TransferService service = new TransferService(
                new NoCommission(), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, -500);
        assertFalse(result);
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferNoAmount(){
        BankAccount from =
                new DebitAccount("301", "Giga", 1000);
        BankAccount to =
                new DebitAccount("302", "Shila", 2000);
        TransferService service = new TransferService(
                new NoCommission(), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to , 0);
        assertFalse(result);
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void cantTrasnferToYourself(){
        BankAccount account = new DebitAccount("401", "Jojo", 1000);
        TransferService service = new TransferService(
                new NoCommission(), new ConsoleNotificationService()
        );
        boolean result = service.transfer(account, account, 100);
        assertFalse(result);
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void commissionIsOnlyForSender(){
        BankAccount from =
                new DebitAccount("501", "Sanya", 2000);
        BankAccount to =
                new DebitAccount("502", "Pasha", 1000);
        TransferService service = new TransferService(
                new PercentCommission(1), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 500);
        assertTrue(result);
        assertEquals(1495, from.getBalance(), 0.01);
        assertEquals(1500, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferWhenPerDontCoverCom(){
        BankAccount from =
                new DebitAccount("601", "Filip", 10000);
        BankAccount to =
                new DebitAccount("602", "Lena", 2000);
        TransferService service = new TransferService(
                new PercentCommission(1), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 10000);
        assertFalse(result);
        assertEquals(10000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void transferFromDebitToSavings(){
        BankAccount from =
                new DebitAccount("701", "Anna", 5000);
        BankAccount to =
                new SavingsAccount("702", "Sergey", 2000, 1000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 1500);
        assertTrue(result);
        assertEquals(3500.0, from.getBalance(), 0.01);
        assertEquals(3500.0, to.getBalance(), 0.01);
    }
    @Test
    void transferFromCreditToDebitUsingLimit(){
        BankAccount from =
                new CreditAccount("801", "Ioaan", 1000, 5000);
        BankAccount to =
                new DebitAccount("802", "David", 2000);
        TransferService service = new TransferService(
                new NoCommission(), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 4000);
        assertTrue(result);
        assertEquals(-3000, from.getBalance(), 0.01);
        assertEquals(6000, to.getBalance(), 0.01);
    }
    @Test
    void transferFromSavingsToDebit(){
        BankAccount from =
                new SavingsAccount("10", "Jenya", 5000, 1000);
        BankAccount to =
                new DebitAccount("11", "Leonid", 2000);
        TransferService service = new TransferService(
                new NoCommission(), new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 4000);
        assertTrue(result);
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(6000, to.getBalance(), 0.01);
    }
    @Test
    void successfulTransferSendsOneNotification(){
        BankAccount from =
                new DebitAccount("12", "Polina", 5000);
        BankAccount to =
                new DebitAccount("13", "KOolya", 1000);
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(), notificationService
        );
        boolean result = service.transfer(from, to, 3000);
        assertTrue(result);
        assertEquals(1, notificationService.getNotificationCount());
        assertEquals("перевод в размере3000.0 ед валюты выполнен", notificationService.getLastMessage());
    }
    @Test
    void failTransferDontSendNotification(){
        BankAccount from =
                new DebitAccount("14", "Petya", 1000);
        BankAccount to =
                new DebitAccount("15", "Jojo", 2000);
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(), notificationService
        );
        boolean result = service.transfer(from,  to, 3000);
        assertFalse(result);
        assertEquals(0, notificationService.getNotificationCount());
        assertNull(notificationService.getLastMessage());
    }
}