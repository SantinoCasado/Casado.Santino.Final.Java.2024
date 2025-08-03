package Interfaces;

import java.util.ArrayList;
import java.util.Map;


public interface IMapAbleJson<T>{
    Map<T, T> toMap(); 
    ArrayList<T> fromMap(Map<T, T> map);
}
