package misc;

public interface IObserver<ObservedType> {
    void update(Observable<ObservedType> object, ObservedType data);
}
