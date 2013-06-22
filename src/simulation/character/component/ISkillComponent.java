package simulation.character.component;

import simulation.labor.LaborType;

/**
 * Interface for a skill component.
 */
public interface ISkillComponent extends ICharacterComponent {

    void increaseSkillLevel(LaborType laborType);

    boolean canDoJob(LaborType requiredLabor);

    LaborType getProfession();

    int getLaborSkill(LaborType laborType);

    boolean isLaborEnabled(LaborType laborType);

    void setLaborEnabled(String laborTypeName, boolean enabled);
}
