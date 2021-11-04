package ru.otus.core.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    private enum Action {
        GET,
        PUT,
        REMOVE;
    }

    //Надо реализовать эти методы
    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, Action.PUT);
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        notifyListeners(key, value, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyListeners(key, value, Action.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> targetListener) {
        listeners.removeIf(hwListenerWeakReference -> {
            HwListener<K, V> hwListener = hwListenerWeakReference.get();
            return hwListener != null && hwListener.equals(targetListener);
        });
    }

    private void notifyListeners(K key, V value, Action action) {
        listeners.forEach(listener -> notify(key, value, action, listener));
    }

    private void notify(K key, V value, Action action, WeakReference<HwListener<K, V>> weakListener) {
        HwListener<K, V> hwListener = weakListener.get();
        Optional.ofNullable(hwListener).ifPresent(listener -> listener.notify(key, value, action.name()));
    }
}
