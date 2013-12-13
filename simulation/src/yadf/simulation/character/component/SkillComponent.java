/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package yadf.simulation.character.component;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.PickupToolJob;
import yadf.simulation.labor.LaborType;
import yadf.simulation.labor.LaborTypeManager;

/**
 * The Class SkillComponent.
 */
public class SkillComponent extends AbstractCharacterComponent implements ISkillComponent {

    /** How proficient the dwarf is in the different labors. */
    private final Map<LaborType, Integer> laborSkills;

    /** What labors are enabled. */
    private final Set<LaborType> enabledLabors;

    /** What the dwarf is best at. */
    private LaborType profession = LaborTypeManager.getInstance().getLaborType("No profession");

    /** The pickup tool job. */
    private PickupToolJob pickupToolJob;

    /**
     * Constructor.
     * @param character the character that this component belongs to
     */
    public SkillComponent(final IGameCharacter character) {
        super(character);
        laborSkills = new HashMap<>();
        enabledLabors = new LinkedHashSet<>();
        // Enable all labors by default
        for (LaborType laborType : LaborTypeManager.getInstance().getLaborTypes()) {
            enabledLabors.add(laborType);
        }
        laborSkills.put(profession, Integer.valueOf(0));
    }

    @Override
    public boolean canDoJob(final LaborType requiredLabor) {
        assert requiredLabor != null;

        // Does the dwarf have the labor enabled
        if (!isLaborEnabled(requiredLabor)) {
            return false;
        }

        // Does the labor require a tool
        if (requiredLabor.toolType == null) {
            return true;
        }

        // Does the dwarf have the required tool
        Item tool = getCharacter().getComponent(IInventoryComponent.class).getToolHolding();

        if (tool == null) {
            return false;
        }

        if (tool.getType().equals(requiredLabor.toolType)) {
            return true;
        }

        return false;
    }

    /**
     * Gets the labor skill.
     * @param laborType the labor type
     * @return the labor skill
     */
    @Override
    public int getLaborSkill(final LaborType laborType) {
        int skill = 0;
        if (laborSkills.containsKey(laborType)) {
            skill = laborSkills.get(laborType).intValue();
        }
        return skill;
    }

    /**
     * Gets the profession.
     * @return the profession
     */
    @Override
    public LaborType getProfession() {
        return profession;
    }

    /**
     * Increase skill level.
     * @param laborType the labor type
     */
    @Override
    public void increaseSkillLevel(final LaborType laborType) {
        if (!laborSkills.containsKey(laborType)) {
            laborSkills.put(laborType, Integer.valueOf(0));
        }
        laborSkills.put(laborType, Integer.valueOf(laborSkills.get(laborType).intValue() + 1));
        if (laborSkills.get(laborType).intValue() > laborSkills.get(profession).intValue()) {
            profession = laborType;
        }
        notifyListeners();
    }

    /**
     * Checks if is labor enabled.
     * @param requiredLabor the required labor
     * @return true, if is labor enabled
     */
    @Override
    public boolean isLaborEnabled(final LaborType requiredLabor) {
        return enabledLabors.contains(requiredLabor);
    }

    @Override
    public void kill() {
        if (pickupToolJob != null) {
            pickupToolJob.interrupt("Character died");
        }
    }

    /**
     * Sets the labor enabled.
     * @param laborTypeName the labor type name
     * @param enabled the enabled
     */
    @Override
    public void setLaborEnabled(final String laborTypeName, final boolean enabled) {
        LaborType laborType = LaborTypeManager.getInstance().getLaborType(laborTypeName);
        if (enabled) {
            if (!enabledLabors.contains(laborType)) {
                enabledLabors.add(laborType);
                notifyListeners();
            }
        } else {
            if (enabledLabors.contains(laborType)) {
                enabledLabors.remove(laborType);
                notifyListeners();
            }
        }
    }

    @Override
    public void update(final IRegion region) {
        IPlayer player = getCharacter().getPlayer();

        // If dwarf is holding a tool that he no longer needs, drop it
        Item tool = getCharacter().getComponent(IInventoryComponent.class).getToolHolding();
        if (tool != null) {
            boolean required = false;
            for (LaborType laborType : enabledLabors) {
                if (laborType.toolType != null && laborType.toolType.equals(tool.getType())) {
                    required = true;
                    break;
                }
            }
            if (!required) {
                getCharacter().getComponent(IInventoryComponent.class).dropTool();
                tool = null;
            }
        }

        // If a labor is enabled that requires a tool, and the dwarf is not
        // holding a tool, try to find one
        if (tool == null && pickupToolJob == null) {
            for (LaborType laborType : enabledLabors) {
                if (laborType.toolType == null) {
                    continue;
                }
                tool = player.getComponent(IStockManager.class).getItem(laborType.toolType.name, false, false);
                if (tool == null) {
                    continue;
                }
                tool.setAvailable(false);
                pickupToolJob = new PickupToolJob(getCharacter(), tool);
                player.getComponent(IJobManager.class).addJob(pickupToolJob);
                break;
            }
        }

        if (pickupToolJob != null && pickupToolJob.isDone()) {
            pickupToolJob = null;
        }
    }
}
