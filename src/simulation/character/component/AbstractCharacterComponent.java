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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.character.IGameCharacter;

/**
 * The Interface ICharacterComponent.
 */
public abstract class AbstractCharacterComponent implements ICharacterComponent {

    /** The character that this component belongs to. */
    private final IGameCharacter character;

    /**
     * Constructor.
     * @param characterTmp the character that this component belongs to
     */
    public AbstractCharacterComponent(final IGameCharacter characterTmp) {
        character = characterTmp;
    }

    /** The listeners to be notified of changes to this component. */
    private final Set<ICharacterComponentListener> listeners = new CopyOnWriteArraySet<>();

    /**
     * Add a listener to this component.
     * @param listener the listener to add
     */
    @Override
    public void addListener(final ICharacterComponentListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener to from component.
     * @param listener the listener to add
     */
    @Override
    public void removeListener(final ICharacterComponentListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all of the listeners.
     */
    protected void notifyListeners() {
        for (ICharacterComponentListener listener : listeners) {
            listener.componentChanged(this);
        }
    }

    /**
     * Get the character that this component belongs to.
     * @return the character that this component belongs to
     */
    protected IGameCharacter getCharacter() {
        return character;
    }
}
