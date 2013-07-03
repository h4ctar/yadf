package misc;

import java.util.LinkedList;
import java.util.List;

public class Observable<ObservedType> {

    private List<IObserver<ObservedType>> observers = new LinkedList<>();

    public void addObserver(final IObserver<ObservedType> observer) {
        assert observers.contains(observer);
        observers.add(observer);
    }

    public void notifyObservers(final ObservedType data) {
        for (IObserver<ObservedType> observer : observers) {
            observer.update(this, data);
        }
    }
}
