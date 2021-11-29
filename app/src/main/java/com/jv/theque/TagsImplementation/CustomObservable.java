package com.jv.theque.TagsImplementation;

public interface CustomObservable {
    public void addObserver(CustomObserver o);
    public void notifyObserver();
}
