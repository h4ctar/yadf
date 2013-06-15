package simulation.character.component;

/**
 * Interface for an eating and drinking component.
 */
public interface IEatDrinkComponent extends ICharacterComponent {

    /**
     * Eat.
     */
    void eat();

    /**
     * Drink.
     */
    void drink();

    /**
     * Can the character work.
     * @return true if they can work
     */
    boolean isHungryOrThirsty();

    /**
     * Gets how hungry the character is as a percentage; 100% means they need to eat.
     * @return the hunger level
     */
    int getHunger();

    /**
     * Gets how thirsty the character is as a percentage; 100% means they need to drink.
     * @return the thirst level
     */
    int getThirst();
}
