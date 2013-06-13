package simulation.character.component;

import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;

public interface ICharacterComponent {

    void update(GameCharacter gameCharacter, Player player, Region region);

    void addListener(ICharacterComponentListener dwarfInterface);

}
