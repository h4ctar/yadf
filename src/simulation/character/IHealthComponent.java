package simulation.character;

import simulation.character.component.ICharacterComponent;

public interface IHealthComponent extends ICharacterComponent {

    void decrementHealth();

    int getHealth();
}
