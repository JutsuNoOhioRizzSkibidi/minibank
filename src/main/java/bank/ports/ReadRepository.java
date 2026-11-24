package bank.ports;

import bank.domain.Identifiable;
import java.util.List;

public interface ReadRepository<ID, T extends Identifiable<ID>>{
    T findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    int size();
}