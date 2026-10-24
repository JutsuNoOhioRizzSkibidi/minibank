package bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferServiceTest {
    @Test
    void successfulTransferUpdatesBothBalances() {
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000001"), "john", 10000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000002"), "pork", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 3000);
        assertEquals(7000.0, from.getBalance(), 0.01);
        assertEquals(5000.0, to.getBalance(), 0.01);
    }
    @Test
    void insufficientFundsLeavesBothBalancesUnchanged() {
        BankAccount from = new DebitAccount(new AccountNumber("0000000003"), "oleg", 1000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000004"), "maria", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(from, to, 3000));
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferNegAmount(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000005"), "Vasya", 1000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000006"), "Anya", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(from, to, -500));
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferNoAmount(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000007"), "Giga", 1000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000008"), "Shila", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(from, to, 0)
        );
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferToYourself(){
        BankAccount account = new DebitAccount(new AccountNumber("0000000009"), "Jojo", 1000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(account, account, 100)
        );
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void commissionIsOnlyForSender(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000010"), "Sanya", 2000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000011"), "Pasha", 1000);
        TransferService service = new TransferService(
                new PercentCommission(1),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 500);
        assertEquals(1495, from.getBalance(), 0.01);
        assertEquals(1500, to.getBalance(), 0.01);
    }
    @Test
    void cantTransferWhenPerDontCoverCom(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000012"), "Filip", 10000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000013"), "Lena", 2000);
        TransferService service = new TransferService(
                new PercentCommission(1),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(from, to, 10000)
        );
        assertEquals(10000, from.getBalance(), 0.01);
        assertEquals(2000, to.getBalance(), 0.01);
    }
    @Test
    void transferFromDebitToSavings(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000014"), "Anna", 5000);
        BankAccount to =
                new SavingsAccount(new AccountNumber("0000000015"), "Sergey", 2000, 1000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 1500);
        assertEquals(3500.0, from.getBalance(), 0.01);
        assertEquals(3500.0, to.getBalance(), 0.01);
    }
    @Test
    void transferFromCreditToDebitUsingLimit(){
        BankAccount from =
                new CreditAccount(new AccountNumber("0000000016"), "Ioaan", 1000, 5000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000017"), "David", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 4000);
        assertEquals(-3000, from.getBalance(), 0.01);
        assertEquals(6000, to.getBalance(), 0.01);
    }
    @Test
    void transferFromSavingsToDebit(){
        BankAccount from =
                new SavingsAccount(new AccountNumber("0000000018"), "Jenya", 5000, 1000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000019"), "Leonid", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 4000);
        assertEquals(1000, from.getBalance(), 0.01);
        assertEquals(6000, to.getBalance(), 0.01);
    }
    @Test
    void successfulTransferSendsOneNotification(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000020"), "Polina", 5000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000021"), "KOolya", 1000);
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(),
                notificationService,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 3000);
        assertEquals(1, notificationService.getNotificationCount());
        assertEquals("перевод 3000.0 выполнен", notificationService.getLastMessage());
    }
    @Test
    void failTransferDontSendNotification(){
        BankAccount from =
                new DebitAccount(new AccountNumber("0000000023"), "Petya", 1000);
        BankAccount to =
                new DebitAccount(new AccountNumber("0000000024"), "Jojo", 2000);
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(),
                notificationService,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(from, to, 3000)
        );
        assertEquals(0, notificationService.getNotificationCount());
        assertNull(notificationService.getLastMessage());
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
    @Test
    void canTransferBetweenDifferentAccountsOfSameOwner() {
        // Подготовка (Arrange)
        BankAccount account = new DebitAccount(new AccountNumber("0000000025"), "Jojo", 1000);
        BankAccount account2 = new DebitAccount(new AccountNumber("0000000026"), "Jojo", 1000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(account, account2, 100);
        assertEquals(900.0, account.getBalance(), 0.01);
        assertEquals(1100.0, account2.getBalance(), 0.01);
    }
    @Test
    void transferAboveLimitIsRej(){
        BankAccount from = new DebitAccount(new AccountNumber("0000000027"), "Sayo", 100000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000028"), "Kayo", 2000);
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(),
                notificationService,
                new BlockedAccountRegistry(),
                new TransactionJournal());
        assertThrows(TransferLimitExceededException.class,
                () -> service.transfer(from, to, 50001)
        );
        assertEquals(100000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
        assertEquals(0, notificationService.getNotificationCount());
    }
    @Test
    void transferAtLimitIsAllowed() {
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000029"), "Олег", 60000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000030"), "Мария", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal());
        service.transfer(from, to, 50000);
        assertEquals(10000.0, from.getBalance(), 0.01);
        assertEquals(52000.0, to.getBalance(), 0.01);
    }
    @Test
    void failedTransferDoesNotChangeBalances() {
        BankAccount from = new DebitAccount(new AccountNumber("0000000031"), "Александр", 1000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000032"), "Басанов", 2000);
        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(from, to, 5000));
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
    @Test
    void transferFromBlockedAccountDontChangeBalancesOrNotify(){
        BankAccount from = new DebitAccount(new AccountNumber("0000000001"),
                "Alex", 5000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000002"),
                "allex", 2000);
        BlockedAccountRegistry blockedAccounts =
                new BlockedAccountRegistry();
        blockedAccounts.block(from.getId());
        FakeNotification notificationService = new FakeNotification();
        TransferService service = new TransferService(
                new NoCommission(),
                notificationService,
                blockedAccounts,
                new TransactionJournal()
        );
        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> service.transfer(from, to , 1000)
        );
        assertEquals("данный счет заблокирован", exception.getMessage());
        assertEquals(5000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
        assertEquals(0, notificationService.getNotificationCount());
        assertNull(notificationService.getLastMessage());
    }
    @Test
    void successfulTransferAddsTransactionToJounral(){
        BankAccount from  = new DebitAccount(
                new AccountNumber("0000000001"),"Sanya", 5000
        );
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000002"), "alex", 2000
        );
        TransactionJournal journal = new TransactionJournal();
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                journal
        );
        service.transfer(from, to, 1000);
        assertEquals(1, journal.size());
        Transaction transaction = journal.findAll().get(0);
        assertEquals(TransactionType.TRANSFER, transaction.type());
        assertEquals(from.getId(), transaction.account());
        assertEquals(1000.0, transaction.amount(), 0.01);
        assertEquals(TransactionStatus.SUCCESS, transaction.status());

    }
    @Test
    void failedTransferDontAddTransactionToJournal(){
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000001"), "Sanya", 1000
        );
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000002"), "Sasha", 2000
        );
        TransactionJournal journal = new TransactionJournal();
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                journal
        );
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(from, to, 3000));
        assertEquals(0, journal.size());
        assertTrue(journal.findAll().isEmpty());
    }
    @Test
    void transferLambdaNoCommission(){
        BankAccount from = new DebitAccount(
                new AccountNumber("1234567890"), "Sanya", 5000
        );
        BankAccount to = new DebitAccount(
                new AccountNumber("1234567891"), "Alex", 1000
        );
        CommissionPolicy noCommission = amount -> 0.0;
        NotificationService console = message -> System.out.println(message);
        TransferService service = new TransferService(
                noCommission,
                console,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 1000);
        assertEquals(4000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
    @Test
    void trasnferWithOnePercentCommissionLambda(){
        BankAccount from = new DebitAccount(
                new AccountNumber("1234567890"), "Alex", 5000
        );
        BankAccount to = new DebitAccount(
                new AccountNumber("1234567891"), "Aleksandr", 1000
        );
        CommissionPolicy onePercent = amount -> amount * 0.01;
        NotificationService console = System.out::println;
        TransferService service = new TransferService(
                onePercent,
                console,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        service.transfer(from, to, 1000);
        assertEquals(3990.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
}