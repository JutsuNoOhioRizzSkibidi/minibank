package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<
    ID,
    T extends Identifiable<ID>>
    implements Repository<ID, T>{
    private final Map<ID, T> values = new HashMap<>();
    @Override
    public int size(){
        return values.size();
    }
    @Override
    public T findById(ID id){
        return values.get(id);
    }
    @Override
    public boolean existsById(ID id) {
        return values.containsKey(id);
    }
    @Override
    public void save(T value) {
        if (value == null){
            throw new IllegalArgumentException("нельзя сохранить null");
        }
        values.put(value.getId(), value);
    }
    @Override
    public boolean deleteById(ID id){
        return values.remove(id) != null;
    }
    @Override
    public List<T> findAll(){
        return new ArrayList<>(values.values());
    }
}
