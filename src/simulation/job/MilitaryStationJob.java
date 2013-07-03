package simulation.job;

import simulation.IRegion;
import simulation.ITimeListener;
import simulation.character.ICharacterListener;
import simulation.character.IGameCharacter;
import simulation.character.component.AttackComponent;
import simulation.character.component.ChaseMovementComponent;
import simulation.character.component.GuardMovementComponent;
import simulation.character.component.IAttackComponent;
import simulation.character.component.ICharacterComponentListener;
import simulation.character.component.IEatDrinkComponent;
import simulation.character.component.IHealthComponent;
import simulation.character.component.ILookoutComponent;
import simulation.character.component.IMovementComponent;
import simulation.character.component.LookoutComponent;
import simulation.job.jobstate.AbstractJobState;
import simulation.job.jobstate.IJobState;
import simulation.job.jobstate.WalkToPositionState;
import simulation.job.jobstate.WasteTimeState;
import simulation.map.MapIndex;

/**
 * The military station job.
 */
public class MilitaryStationJob extends AbstractJob {

    /** Where they are stationed. */
    private MapIndex target;

    /** The soldier dwarf. */
    private IGameCharacter soldier;

    /**
     * Constructor.
     * @param targetTmp where they are stationed
     * @param soldierTmp the soldier
     */
    public MilitaryStationJob(final MapIndex targetTmp, final IGameCharacter soldierTmp) {
        super(soldierTmp.getPlayer());
        target = targetTmp;
        soldier = soldierTmp;
        setJobState(new WaitForSoldierState());
        soldier.getComponent(IEatDrinkComponent.class).setSpawnJobs(false);
    }

    @Override
    public String toString() {
        return "Military station";
    }

    @Override
    public MapIndex getPosition() {
        return target;
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        soldier.getComponent(IEatDrinkComponent.class).setSpawnJobs(true);
        soldier.releaseLock();
    }

    /**
     * The waiting for soldier state.
     */
    private class WaitForSoldierState extends simulation.job.jobstate.WaitingForDwarfState {

        /**
         * Constructor.
         */
        public WaitForSoldierState() {
            super(soldier, MilitaryStationJob.this);
        }

        @Override
        public String toString() {
            return "Waiting for soldier";
        }

        @Override
        public IJobState getNextState() {
            return new WalkToTargetState();
        }
    }

    /**
     * Walk to target state.
     */
    private class WalkToTargetState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToTargetState() {
            super(target, soldier, false, MilitaryStationJob.this);
        }

        @Override
        public String toString() {
            return "Walking to target";
        }

        @Override
        public IJobState getNextState() {
            return new GuardTargetState();
        }
    }

    /**
     * Guard target state.
     */
    private class GuardTargetState extends AbstractJobState implements ITimeListener, ICharacterComponentListener {

        /** How long before the soldier consumes a ration pack. */
        private static final long TIME_BETWEEN_EAT_AND_DRINK_RATIONS = IRegion.SIMULATION_STEPS_PER_DAY;

        /** The next time that the soldier will consume a ration pack. */
        private long eatAndDrinkRationTime;

        /** The next job state. */
        private IJobState nextState;

        /** The lookout component. */
        private LookoutComponent lookoutComponent;

        /**
         * Constructor.
         */
        public GuardTargetState() {
            super(MilitaryStationJob.this);
        }

        @Override
        public String toString() {
            return "Guarding target";
        }

        @Override
        public void start() {
            eatAndDrinkRationTime = soldier.getRegion().addTimeListener(TIME_BETWEEN_EAT_AND_DRINK_RATIONS, this);
            lookoutComponent = new LookoutComponent(soldier);
            lookoutComponent.addListener(this);
            soldier.setComponent(IMovementComponent.class, new GuardMovementComponent(soldier, target));
            soldier.setComponent(ILookoutComponent.class, lookoutComponent);
        }

        @Override
        public void interrupt(final String message) {
            soldier.getRegion().removeTimeListener(eatAndDrinkRationTime, this);
            lookoutComponent.removeListener(this);
            soldier.removeComponent(ILookoutComponent.class);
        }

        @Override
        protected void doFinalActions() {
            soldier.getRegion().removeTimeListener(eatAndDrinkRationTime, this);
            lookoutComponent.removeListener(this);
            soldier.removeComponent(ILookoutComponent.class);
        }

        @Override
        public IJobState getNextState() {
            return nextState;
        }

        @Override
        public void notifyTimeEvent() {
            nextState = new EatAndDrinkRationState();
            finishState();
        }

        @Override
        public void componentChanged(final Object component) {
            assert component == lookoutComponent;
            nextState = new AttackState(lookoutComponent.getEnemy());
            finishState();
        }
    }

    /**
     * The attack state.
     */
    public class AttackState extends AbstractJobState implements ICharacterListener, ITimeListener,
            ICharacterComponentListener {

        /** How long does it take for the soldier to prepare for a hit. */
        private static final long PREPARE_FOR_HIT_DURATION = IRegion.SIMULATION_STEPS_PER_MINUTE * 20;

        /** How far away can the soldier hit the enemy from. */
        private static final long HIT_DISTANCE = 1;

        /** Is the soldier ready to hit the enemy. */
        private boolean readyToHit = false;

        /** The dwarf to attack. */
        private final IGameCharacter enemy;

        /** When the prepare for hit time is up. */
        private long hitTime;

        private ChaseMovementComponent chaseMovementComponent;

        /**
         * Constructor.
         * @param enemyTmp the enemy to attack
         */
        public AttackState(final IGameCharacter enemyTmp) {
            super(MilitaryStationJob.this);
            enemy = enemyTmp;
        }

        @Override
        public String toString() {
            return "Attacking enemy";
        }

        @Override
        public void start() {
            chaseMovementComponent = new ChaseMovementComponent(soldier, enemy);
            chaseMovementComponent.addListener(this);
            soldier.setComponent(IMovementComponent.class, chaseMovementComponent);
            soldier.setComponent(IAttackComponent.class, new AttackComponent(soldier, enemy));
            enemy.addListener(this);
            hitTime = soldier.getRegion().addTimeListener(PREPARE_FOR_HIT_DURATION, this);
        }

        @Override
        public void interrupt(final String message) {
            soldier.getRegion().removeTimeListener(hitTime, this);
        }

        @Override
        public IJobState getNextState() {
            return new GuardTargetState();
        }

        @Override
        public void characterChanged(final Object character) {
            assert character == enemy;
            if (enemy.isDead()) {
                soldier.getRegion().removeTimeListener(hitTime, this);
                finishState();
            }
        }

        @Override
        public void notifyTimeEvent() {
            assert !readyToHit;
            readyToHit = true;
            hitEnemy();
        }

        @Override
        public void componentChanged(final Object component) {
            assert component == chaseMovementComponent;
            hitEnemy();
        }

        /**
         * Hit the enemy.
         */
        private void hitEnemy() {
            if (readyToHit && enemy.getPosition().distance(soldier.getPosition()) <= HIT_DISTANCE) {
                // TODO: move this into damage component
                for (int i = 0; i < 10; i++) {
                    enemy.getComponent(IHealthComponent.class).decrementHealth();
                }
                hitTime = soldier.getRegion().addTimeListener(PREPARE_FOR_HIT_DURATION, this);
                readyToHit = false;
            }
        }
    }

    /**
     * The eat and drink ration state.
     */
    public class EatAndDrinkRationState extends WasteTimeState {

        /** How long does it take to eat a ration. */
        private static final long EAT_DRINK_RATION_DURATION = IRegion.SIMULATION_STEPS_PER_MINUTE * 10;

        /**
         * Constructor.
         */
        public EatAndDrinkRationState() {
            super(EAT_DRINK_RATION_DURATION, soldier, MilitaryStationJob.this);
            // TODO: make the dwarf actually carry rations and interrupt the job if he's out
        }

        @Override
        public String toString() {
            return "Eating and drinking ration";
        }

        @Override
        public IJobState getNextState() {
            return new GuardTargetState();
        }
    }
}
