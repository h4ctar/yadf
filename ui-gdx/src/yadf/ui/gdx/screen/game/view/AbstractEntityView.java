package yadf.ui.gdx.screen.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;

import yadf.simulation.IEntity;
import yadf.simulation.map.MapArea;
import yadf.ui.gdx.screen.game.GameScreen;

public abstract class AbstractEntityView<T extends IEntity> extends Actor {

    /** The entity. */
    private T entity;

    public AbstractEntityView(T entityTmp) {
        entity = entityTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapArea area = entity.getArea();
        setX(area.pos.x * GameScreen.SPRITE_SIZE);
        setY(area.pos.y * GameScreen.SPRITE_SIZE);
        setWidth(area.width * GameScreen.SPRITE_SIZE);
        setHeight(area.height * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == area.pos.z);
    }
    
    protected T getEntity() {
        return entity;
    }
}
