package Interfaces;

import java.util.Map;


public interface IMapAbleJson<T> {
     Map<String, String> toMap();
    T fromMap(Map<String, String> map);

}
