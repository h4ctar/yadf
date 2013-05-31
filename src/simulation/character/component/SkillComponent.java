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
package simulation.character.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import simulation.Player;
import simulation.Region;
import simulation.character.GameCharacter;
import simulation.item.Item;
import simulation.job.PickupToolJob;
import simulation.labor.LaborType;
import simulation.labor.LaborTypeManager;

/**
 * The Class SkillComponent.
 */
public class SkillComponent extends AbstractCharacterComponent {

    /** How proficient the dwarf is in the different labors. */
    private final Map<LaborType, Integer> laborSkills;

    /** What labors are enabled. */
    private final Set<LaborType> enabledLabors;

    /** What the dwarf is best at. */
    private LaborType profession = null;

    /** The pickup tool job. */
    private PickupToolJob pickupToolJob;

    /**
     * Instantiates a new skill component.
     */
    public SkillComponent() {
        laborSkills = new HashMap<>();
        enabledLabors = new HashSet<>();
        // Enable all labors by default
        for (LaborType laborType : LaborTypeManager.getInstance().getLaborTypes()) {
            enabledLabors.add(laborType);
        }
    }

    /**
     * Can do job.
     * 
     * @param requiredLabor the required labor
     * @param character the character
     * @return true, if successful
     */
    public boolean canDoJob(final LaborType requiredLabor, final GameCharacter character) {
        if (requiredLabor == null) {
            return true;
        }

        // Does the dwarf have the labor enabled
        if (!isLaborEnabled(requiredLabor)) {
            return false;
        }

        // Does the labor require a tool
        if (requiredLabor.toolType == null) {
            return true;
        }

        // Does the dwarf have the required tool
        Item tool = character.getInventory().getToolHolding();

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
     * 
     * @param laborType the labor type
     * @return the labor skill
     */
    public int getLaborSkill(final LaborType laborType) {
        int skill = 0;
        if (laborSkills.containsKey(laborType)) {
            skill = laborSkills.get(laborType).intValue();
        }
        return skill;
    }

    /**
     * Gets the profession.
     * 
     * @return the profession
     */
    public LaborType getProfession() {
        return profession;
    }

    /**
     * Increase skill level.
     * 
     * @param laborType the labor type
     */
    public void increaseSkillLevel(final LaborType laborType) {
        if (!laborSkills.containsKey(laborType)) {
            laborSkills.put(laborType, Integer.valueOf(0));
        }

        laborSkills.put(laborType, Integer.valueOf(laborSkills.get(laborType).intValue() + 1));

        if (profession == null || laborSkills.get(laborType).intValue() > laborSkills.get(profession).intValue()) {
            profession = laborType;
        }
        notifyListeners();
    }

    /**
     * Checks if is labor enabled.
     * 
     * @param requiredLabor the required labor
     * @return true, if is labor enabled
     */
    public boolean isLaborEnabled(final LaborType requiredLabor) {
        return enabledLabors.contains(requiredLabor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kill() {
        pickupToolJob = null;
    }

    /**
     * Sets the labor enabled.
     * 
     * @param laborTypeName the labor type name
     * @param enabled the enabled
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final GameCharacter character, final Player player, final Region region) {
        assert (player != null);

        // If dwarf is holding a tool that he no longer needs, drop it
        Item tool = character.getInventory().getToolHolding();
        if (tool != null) {
            boolean required = false;
            for (LaborType laborType : enabledLabors) {
                if (laborType.toolType != null && laborType.toolType.equals(tool.getType())) {
                    required = true;
                    break;
                }
            }
            if (!required) {
                character.getInventory().dropTool();
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
                tool = player.getStockManager().getUnusedItem(laborType.toolType.name);
                if (tool == null) {
                    continue;
                }
                tool.setUsed(true);
                if (tool.isStored()) {
                    player.getStockManager().removeItemFromStorage(tool);
                }
                pickupToolJob = new PickupToolJob(character, tool);
                player.getJobManager().addJob(pickupToolJob);
                break;
            }
        }

        if (pickupToolJob != null && pickupToolJob.isDone()) {
            pickupToolJob = null;
        }
    }
}
