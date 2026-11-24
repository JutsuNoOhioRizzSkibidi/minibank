
package bank.ports;

import bank.domain.Identifiable;

public interface Repository<ID, T extends Identifiable<ID>>
        extends ReadRepository<ID, T>, WriteRepository<ID, T>{
}