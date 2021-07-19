import java.util.HashMap;
import java.util.Map;

public class BiMap<T, U, V> {
    Map<T, Map<U,V>> map;

    BiMap(){
        map = new HashMap<>();
    }

    public void put(T t, U u, V v){
         Map<U,V> hm = map.get(t);
         if (hm == null){
             map.put(t, new HashMap<>());
         }
         map.get(t).put(u,v);
    }

    public V get(T t, U u){
        Map<U, V> hm = map.get(t);
        if (hm == null)
            return null;
        return hm.get(u);
    }

    public int size(){
        int i = 0;
        for (Map<U,V> hm : map.values()){
            i += hm.size();
        }
        return i;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
