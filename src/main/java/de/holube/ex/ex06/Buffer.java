package de.holube.ex.ex06;

public interface Buffer<V> {

    // Wert in Buffer ablegen; warten falls Buffer voll (value == null erlaubt)
    public void put(V value);

    // Wert liefern und Buffer leeren; warten falls Buffer leer
    public V get();
}
