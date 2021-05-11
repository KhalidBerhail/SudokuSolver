package helpers;

import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class MyMap<K,V> {

	private List<Node<K,V>> map ;
	
	public MyMap() {
		// TODO Auto-generated constructor stub
		map = new ArrayList<Node<K,V>>();
	}
	public void put(K key,V value) {
		map.add(new Node<K, V>(key,value));
	}
	public List<Node<K,V>> getMap() {
		return map;
	}
	
	public int size() {
		return map.size();
	}
	
	
	
	
}
 