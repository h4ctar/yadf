package simulation.character.component;

import simulation.character.GameCharacter;
import simulation.labor.LaborType;

public interface ISkillComponent extends ICharacterComponent {

    void increaseSkillLevel(LaborType laborType);

    boolean canDoJob(LaborType requiredLabor, GameCharacter dwarf);

    LaborType getProfession();

    int getLaborSkill(LaborType laborType);

    boolean isLaborEnabled(LaborType laborType);

    void setLaborEnabled(String laborTypeName, boolean enabled);
}
