package yadf.simulation;

import yadf.simulation.map.MapIndex;

public interface IEntityManager<T extends IEntity> extends IGameObjectManager<T> {

    T getEntity(MapIndex position);
}
