package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryRepository<
    ID,
    T extends Identifiable<ID>>
    implements Repository<ID, T>{
    private final List<T> values = new ArrayList<>();
    @Override
    public int size(){
        return values.size();
    }
    @Override
    public T findById(ID id){
        for (T value : values){
            if (Objects.equals(value.getId(), id)) {
                return value;
            }
        }
        return null;
    }
    @Override
    public boolean existsById(ID id) {
        return findById(id) != null;
    }
    @Override
    public void save(T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "нельзя сохранить null"
            );
        }
        for (int i = 0; i < values.size(); i++) {
            T existing = values.get(i);
            if (Objects.equals(existing.getId(), value.getId())) {
                values.set(i, value);
                return;
            }
        }
        values.add(value);
    }
}
