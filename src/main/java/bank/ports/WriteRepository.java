package bank.ports;

import bank.domain.Identifiable;

public interface WriteRepository<ID, T extends Identifiable<ID>>{
    void save(T value);
    boolean deleteById(ID id);
}