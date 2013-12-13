package yadf.simulation.job;

import java.util.Random;

import yadf.misc.MyRandom;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.IRegion;
import yadf.simulation.ITimeListener;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.AttackComponent;
import yadf.simulation.character.component.ChaseMovementComponent;
import yadf.simulation.character.component.IAttackComponent;
import yadf.simulation.character.component.ICharacterComponentListener;
import yadf.simulation.character.component.IEatDrinkComponent;
import yadf.simulation.character.component.IHealthComponent;
import yadf.simulation.character.component.ILookoutComponent;
import yadf.simulation.character.component.IMovementComponent;
import yadf.simulation.character.component.ISleepComponent;
import yadf.simulation.character.component.LookoutComponent;
import yadf.simulation.character.component.WalkMovementComponent;
import yadf.simulation.job.jobstate.AbstractJobState;
import yadf.simulation.job.jobstate.IJobState;
import yadf.simulation.job.jobstate.WalkToPositionState;
import yadf.simulation.job.jobstate.WasteTimeState;
import yadf.simulation.map.MapIndex;

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
        soldier.getComponent(ISleepComponent.class).setSpawnJobs(false);
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
        soldier.getComponent(ISleepComponent.class).setSpawnJobs(true);
        soldier.setAvailable(true);
    }

    /**
     * The waiting for soldier state.
     */
    private class WaitForSoldierState extends yadf.simulation.job.jobstate.WaitingForDwarfState {

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

        /** The walk movement component. */
        private WalkMovementComponent walkComponent;

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
            walkComponent = new WalkMovementComponent(soldier, getWanderPosition(), false);
            walkComponent.addListener(this);
            soldier.setComponent(IMovementComponent.class, walkComponent);
            soldier.setComponent(ILookoutComponent.class, lookoutComponent);
        }

        @Override
        public void interrupt(final String message) {
            soldier.getRegion().removeTimeListener(eatAndDrinkRationTime, this);
            lookoutComponent.removeListener(this);
            soldier.removeComponent(ILookoutComponent.class);
            walkComponent.removeListener(this);
            // TODO: interrupt walk component
        }

        @Override
        protected void doFinalActions() {
            soldier.getRegion().removeTimeListener(eatAndDrinkRationTime, this);
            lookoutComponent.removeListener(this);
            soldier.removeComponent(ILookoutComponent.class);
            walkComponent.removeListener(this);
            // TODO: interrupt walk component
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
            if (component == lookoutComponent) {
                nextState = new AttackState(lookoutComponent.getEnemy());
                finishState();
            } else if (component == walkComponent) {
                walkComponent.removeListener(this);
                walkComponent = new WalkMovementComponent(soldier, getWanderPosition(), false);
                walkComponent.addListener(this);
                soldier.setComponent(IMovementComponent.class, walkComponent);
            }
        }

        /**
         * Get a random position close to the target.
         * @return the position
         */
        private MapIndex getWanderPosition() {
            Random random = MyRandom.getInstance();
            MapIndex wanderPosition = new MapIndex();
            wanderPosition.x = random.nextInt(8) - 4 + target.x;
            wanderPosition.y = random.nextInt(8) - 4 + target.y;
            wanderPosition.z = soldier.getRegion().getMap().getHeight(wanderPosition.x, wanderPosition.y);
            return wanderPosition;
        }
    }

    /**
     * The attack state.
     */
    public class AttackState extends AbstractJobState implements IGameObjectListener, ITimeListener,
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

        /** The chase movement component. */
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
            enemy.addGameObjectListener(this);
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
        public void gameObjectChanged(final IGameObject gameObject) {
            assert gameObject == enemy;
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
                for (int i = 0; i < 100; i++) {
                    enemy.getComponent(IHealthComponent.class).decrementHealth();
                }
                hitTime = soldier.getRegion().addTimeListener(PREPARE_FOR_HIT_DURATION, this);
                readyToHit = false;
            }
        }

        @Override
        public void gameObjectDeleted(final IGameObject gameObject) {
            // TODO Auto-generated method stub
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
        protected void doFinalActions() {
            soldier.getComponent(IEatDrinkComponent.class).eat();
            soldier.getComponent(IEatDrinkComponent.class).drink();
        }

        @Override
        public IJobState getNextState() {
            return new GuardTargetState();
        }
    }
}
