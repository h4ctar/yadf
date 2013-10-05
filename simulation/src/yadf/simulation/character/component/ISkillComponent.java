package yadf.simulation.character.component;

import yadf.simulation.labor.LaborType;

/**
 * Interface for a skill component.
 */
public interface ISkillComponent extends ICharacterComponent {

    /**
     * Increase the skill level of a particular labor.
     * @param laborType the labor type to increase skill level for
     */
    void increaseSkillLevel(LaborType laborType);

    /**
     * Can the dwarf do a particular job.
     * @param requiredLabor the labor type required for the job
     * @return true if the dwarf can do the job
     */
    boolean canDoJob(LaborType requiredLabor);

    /**
     * Get the profession of the dwarf.
     * @return the profession
     */
    LaborType getProfession();

    /**
     * Get the skill level for a particular labor type.
     * @param laborType the labor type
     * @return the skill level
     */
    int getLaborSkill(LaborType laborType);

    /**
     * Is the labor type enabled.
     * @param laborType the labor type
     * @return true if it's enabled
     */
    boolean isLaborEnabled(LaborType laborType);

    /**
     * Enable or disable a labor type.
     * @param laborTypeName the labor type name to modify
     * @param enabled true to enable
     */
    void setLaborEnabled(String laborTypeName, boolean enabled);
}
