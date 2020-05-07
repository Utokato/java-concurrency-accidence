package cn.utoakto.concurrency.chapter4;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/5/2
 */
public class Subject {

    private List<Observer> observers = new ArrayList<>();

    private int state;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        if (state == this.state) {
            return;
        }
        this.state = state;
        notifyAllObserver();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    private void notifyAllObserver() {
        observers.forEach(Observer::update);
    }
}
