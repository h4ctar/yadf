package misc;

public interface IObserver<ObservedType> {
    public void update(Observable<ObservedType> object, ObservedType data);
}
