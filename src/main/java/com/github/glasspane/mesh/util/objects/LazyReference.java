package com.github.glasspane.mesh.util.objects;

import java.util.function.Supplier;

public class LazyReference<T> {

    private T object = null;
    private final Supplier<T> getter;

    public LazyReference(Supplier<T> getter) {
        this.getter = getter;
    }

    public T get() {
        if(this.object == null) {
            this.object = getter.get();
        }
        return this.object;
    }
}
