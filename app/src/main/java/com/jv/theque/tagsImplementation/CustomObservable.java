package com.jv.theque.tagsImplementation;

public interface CustomObservable {
    public void addObserver(CustomObserver o);
    public void notifyObserver();
}
