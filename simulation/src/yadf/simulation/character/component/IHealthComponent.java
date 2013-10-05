package yadf.simulation.character.component;

/**
 * Interface for a health component.
 */
public interface IHealthComponent extends ICharacterComponent {

    /**
     * Decrement the health of the character.
     */
    void decrementHealth();

    /**
     * Get the health of the character.
     * @return the health of the character
     */
    int getHealth();
}
