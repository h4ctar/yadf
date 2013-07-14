package simulation.job.jobstate;

import simulation.character.Dwarf;
import simulation.character.ICharacterAvailableListener;
import simulation.character.ICharacterManager;
import simulation.character.IGameCharacter;
import simulation.character.component.ISkillComponent;
import simulation.job.AbstractJob;
import simulation.labor.LaborType;

/**
 * Generic state to look for a dwarf.
 */
public abstract class LookingForDwarfState extends AbstractJobState implements ICharacterAvailableListener {

    /** The found dwarf. */
    private IGameCharacter dwarf;

    /** The required labor type. */
    private final LaborType requiredLabor;

    /**
     * Constructor.
     * @param requiredLaborTmp the required labor
     * @param jobTmp the job that this state belongs to
     */
    public LookingForDwarfState(final LaborType requiredLaborTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        requiredLabor = requiredLaborTmp;
    }

    @Override
    public String toString() {
        return "Looking for dwarf";
    }

    @Override
    public void start() {
        dwarf = getJob().getPlayer().getComponent(ICharacterManager.class).getIdleCharacter(requiredLabor);
        if (dwarf == null) {
            getJob().getPlayer().getComponent(ICharacterManager.class).addListener(this);
        } else {
            dwarf.setJob(getJob());
            finishState();
        }
    }

    @Override
    public void characterAvailable(final IGameCharacter character) {
        assert character instanceof Dwarf;
        if (character.getComponent(ISkillComponent.class).canDoJob(requiredLabor) && character.isFree()) {
            character.setJob(getJob());
            getJob().getPlayer().getComponent(ICharacterManager.class).removeListener(this);
            dwarf = character;
            finishState();
        }
    }

    /**
     * Gets the found dwarf.
     * @return the dwarf
     */
    public IGameCharacter getDwarf() {
        return dwarf;
    }

    @Override
    public void interrupt(final String message) {
        getJob().getPlayer().getComponent(ICharacterManager.class).removeListener(this);
    }
}
