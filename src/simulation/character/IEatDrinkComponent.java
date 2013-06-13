package simulation.character;

import simulation.character.component.ICharacterComponent;

public interface IEatDrinkComponent extends ICharacterComponent {

    void eat();

    void drink();

    boolean canWork();

    int getHunger();

    int getThirst();

}
