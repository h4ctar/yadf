package yadf.simulation;

import yadf.simulation.map.MapIndex;

public class AbstractEntityManager<T extends IEntity> extends AbstractGameObjectManager<T> implements
        IEntityManager<T> {

    @Override
    public T getEntity(final MapIndex index) {
        for (T entity : getGameObjects()) {
            if (entity.getArea().containesIndex(index)) {
                return entity;
            }
        }
        return null;
    }
}
