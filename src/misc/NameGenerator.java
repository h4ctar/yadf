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
package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * This class is released under GNU general public license
 * 
 * Description: This class generates random names from syllables, and provides programmer a simple way to set a group of
 * rules for generator to avoid unpronounceable and bizarre names.
 * 
 * SYLLABLE FILE REQUIREMENTS/FORMAT: 1) all syllables are separated by line break. 2) Syllable should not contain or
 * start with whitespace, as this character is ignored and only first part of the syllable is read. 3) + and -
 * characters are used to set rules, and using them in other way, may result in unpredictable results. 4) Empty lines
 * are ignored.
 * 
 * SYLLABLE CLASSIFICATION: Name is usually composed from 3 different class of syllables, which include prefix, middle
 * part and suffix. To declare syllable as a prefix in the file, insert "-" as a first character of the line. To declare
 * syllable as a suffix in the file, insert "+" as a first character of the line. everything else is read as a middle
 * part.
 * 
 * NUMBER OF SYLLABLES: Names may have any positive number of syllables. In case of 2 syllables, name will be composed
 * from prefix and suffix. In case of 1 syllable, name will be chosen from amongst the prefixes. In case of 3 and more
 * syllables, name will begin with prefix, is filled with middle parts and ended with suffix.
 * 
 * ASSIGNING RULES: I included a way to set 4 kind of rules for every syllable. To add rules to the syllables, write
 * them right after the syllable and SEPARATE WITH WHITESPACE. (example: "aad +v -c"). The order of rules is not
 * important.
 * 
 * RULES: 1) +v means that next syllable must definitely start with a Vowel. 2) +c means that next syllable must
 * definitely start with a consonant. 3) -v means that this syllable can only be added to another syllable, that ends
 * with a Vowel. 4) -c means that this syllable can only be added to another syllable, that ends with a consonant. So,
 * our example: "aad +v -c" means that "aad" can only be after consonant and next syllable must start with Vowel. Beware
 * of creating logical mistakes, like providing only syllables ending with consonants, but expecting only Vowels, which
 * will be detected and RuntimeException will be thrown.
 * 
 * TO START: Create a new NameGenerator object, provide the syllable file, and create names using compose() method.
 * 
 * 
 */
// CHECKSTYLE:OFF
public class NameGenerator implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 5303569956306407988L;

    /**
     * Allow cons.
     * 
     * @param array the array
     * @return true, if successful
     */
    private static boolean allowCons(final ArrayList<String> array) {
        for (String s : array) {
            if (hatesPreviousVowels(s) || !hatesPreviousConsonants(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Allow vocs.
     * 
     * @param array the array
     * @return true, if successful
     */
    private static boolean allowVocs(final ArrayList<String> array) {
        for (String s : array) {
            if (hatesPreviousConsonants(s) || !hatesPreviousVowels(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consonant first.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean consonantFirst(final String s) {
        return (String.copyValueOf(CONSONANTS).contains(String.valueOf(s.charAt(0)).toLowerCase()));
    }

    /**
     * Consonant last.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean consonantLast(final String s) {
        return (String.copyValueOf(CONSONANTS).contains(String.valueOf(s.charAt(s.length() - 1)).toLowerCase()));
    }

    /** The pre. */
    ArrayList<String> pre = new ArrayList<>();

    /** The mid. */
    ArrayList<String> mid = new ArrayList<>();

    /** The sur. */
    ArrayList<String> sur = new ArrayList<>();

    /** The Constant Vowels. */
    private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'ä', 'ö', 'õ', 'ü', 'y' };

    /** The Constant consonants. */
    private static final char[] CONSONANTS = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r',
            's', 't', 'v', 'w', 'x', 'y' };

    /**
     * Contains cons first.
     * 
     * @param array the array
     * @return true, if successful
     */
    private static boolean containsConsFirst(final ArrayList<String> array) {
        for (String s : array) {
            if (consonantFirst(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains voc first.
     * 
     * @param array the array
     * @return true, if successful
     */
    private static boolean containsVocFirst(final ArrayList<String> array) {
        for (String s : array) {
            if (vowelFirst(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Expects consonant.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean expectsConsonant(final String s) {
        return (s.substring(1).contains("+c"));
    }

    /**
     * Expects vowel.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean expectsVowel(final String s) {
        return (s.substring(1).contains("+v"));
    }

    /**
     * Hates previous consonants.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean hatesPreviousConsonants(final String s) {
        return (s.substring(1).contains("-v"));
    }

    /**
     * Hates previous vowels.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean hatesPreviousVowels(final String s) {
        return (s.substring(1).contains("-c"));
    }

    /**
     * Pure syl.
     * 
     * @param s the s
     * @return the string
     */
    private static String pureSyl(final String s) {
        String s2 = s.trim();
        if (s2.charAt(0) == '+' || s2.charAt(0) == '-') {
            s2 = s2.substring(1);
        }
        return s2.split(" ")[0];
    }

    /**
     * Upper.
     * 
     * @param s the s
     * @return the string
     */
    private static String upper(final String s) {
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }

    /**
     * Vowel first.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean vowelFirst(final String s) {
        return (String.copyValueOf(VOWELS).contains(String.valueOf(s.charAt(0)).toLowerCase()));
    }

    /**
     * Vowel last.
     * 
     * @param s the s
     * @return true, if successful
     */
    private static boolean vowelLast(final String s) {
        return (String.copyValueOf(VOWELS).contains(String.valueOf(s.charAt(s.length() - 1)).toLowerCase()));
    }

    /** The ref. */
    private final String ref;

    /**
     * Create new random name generator object. refresh() is automatically called.
     * 
     * @param ref insert file name, where syllables are located
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws URISyntaxException the uRI syntax exception
     */
    public NameGenerator(final String ref) throws IOException, URISyntaxException {
        this.ref = ref;
        refresh();
    }

    /**
     * Compose a new name.
     * 
     * @param syls The number of syllables used in name.
     * @return Returns composed name as a String
     */
    public String compose(final int syls) {
        if (syls > 2 && mid.size() == 0) {
            throw new RuntimeException(
                    "You are trying to create a name with more than 3 parts, which requires middle parts, "
                            + "which you have none in the file "
                            + "asdfasdf"
                            + ". You should add some. Every word, which doesn't have + or - for a prefix is counted as a middle part.");
        }
        if (pre.size() == 0) {
            throw new RuntimeException(
                    "You have no prefixes to start creating a name. add some and use \"-\" prefix, to identify it as a prefix for a name. (example: -asd)");
        }
        if (sur.size() == 0) {
            throw new RuntimeException(
                    "You have no suffixes to end a name. add some and use \"+\" prefix, to identify it as a suffix for a name. (example: +asd)");
        }
        if (syls < 1) {
            throw new RuntimeException("compose(int syls) can't have less than 1 syllable");
        }
        int expecting = 0; // 1 for Vowel, 2 for consonant
        int last = 0; // 1 for Vowel, 2 for consonant
        String name;
        int a = (int) (MyRandom.getInstance().nextDouble() * pre.size());

        if (vowelLast(pureSyl(pre.get(a)))) {
            last = 1;
        } else {
            last = 2;
        }

        if (syls > 2) {
            if (expectsVowel(pre.get(a))) {
                expecting = 1;
                if (!containsVocFirst(mid)) {
                    throw new RuntimeException("Expecting \"middle\" part starting with Vowel, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
            if (expectsConsonant(pre.get(a))) {
                expecting = 2;
                if (!containsConsFirst(mid)) {
                    throw new RuntimeException("Expecting \"middle\" part starting with consonant, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
        } else {
            if (expectsVowel(pre.get(a))) {
                expecting = 1;
                if (!containsVocFirst(sur)) {
                    throw new RuntimeException("Expecting \"suffix\" part starting with Vowel, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
            if (expectsConsonant(pre.get(a))) {
                expecting = 2;
                if (!containsConsFirst(sur)) {
                    throw new RuntimeException("Expecting \"suffix\" part starting with consonant, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
        }
        if (vowelLast(pureSyl(pre.get(a))) && !allowVocs(mid)) {
            throw new RuntimeException(
                    "Expecting \"middle\" part that allows last character of prefix to be a Vowel, "
                            + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the prefix used, was : \""
                            + pre.get(a)
                            + "\", which"
                            + "means there should be a part available, that has \"-v\" requirement or no requirements for previous syllables at all.");
        }

        if (consonantLast(pureSyl(pre.get(a))) && allowCons(mid) == false) {
            throw new RuntimeException(
                    "Expecting \"middle\" part that allows last character of prefix to be a consonant, "
                            + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the prefix used, was : \""
                            + pre.get(a)
                            + "\", which"
                            + "means there should be a part available, that has \"-c\" requirement or no requirements for previous syllables at all.");
        }

        int b[] = new int[syls];
        for (int i = 0; i < b.length - 2; i++) {

            do {
                b[i] = (int) (MyRandom.getInstance().nextDouble() * mid.size());
            } while (expecting == 1 && vowelFirst(pureSyl(mid.get(b[i]))) == false || expecting == 2
                    && consonantFirst(pureSyl(mid.get(b[i]))) == false || last == 1
                    && hatesPreviousVowels(mid.get(b[i])) || last == 2 && hatesPreviousConsonants(mid.get(b[i])));

            expecting = 0;
            if (expectsVowel(mid.get(b[i]))) {
                expecting = 1;
                if (i < b.length - 3 && containsVocFirst(mid) == false) {
                    throw new RuntimeException("Expecting \"middle\" part starting with Vowel, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
                if (i == b.length - 3 && containsVocFirst(sur) == false) {
                    throw new RuntimeException("Expecting \"suffix\" part starting with Vowel, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
            if (expectsConsonant(mid.get(b[i]))) {
                expecting = 2;
                if (i < b.length - 3 && containsConsFirst(mid) == false) {
                    throw new RuntimeException("Expecting \"middle\" part starting with consonant, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
                if (i == b.length - 3 && containsConsFirst(sur) == false) {
                    throw new RuntimeException("Expecting \"suffix\" part starting with consonant, "
                            + "but there is none. You should add one, or remove requirement for one.. ");
                }
            }
            if (vowelLast(pureSyl(mid.get(b[i]))) && allowVocs(mid) == false && syls > 3) {
                throw new RuntimeException(
                        "Expecting \"middle\" part that allows last character of last syllable to be a Vowel, "
                                + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the part used, was : \""
                                + mid.get(b[i])
                                + "\", which "
                                + "means there should be a part available, that has \"-v\" requirement or no requirements for previous syllables at all.");
            }

            if (consonantLast(pureSyl(mid.get(b[i]))) && allowCons(mid) == false && syls > 3) {
                throw new RuntimeException(
                        "Expecting \"middle\" part that allows last character of last syllable to be a consonant, "
                                + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the part used, was : \""
                                + mid.get(b[i])
                                + "\", which "
                                + "means there should be a part available, that has \"-c\" requirement or no requirements for previous syllables at all.");
            }
            if (i == b.length - 3) {
                if (vowelLast(pureSyl(mid.get(b[i]))) && allowVocs(sur) == false) {
                    throw new RuntimeException(
                            "Expecting \"suffix\" part that allows last character of last syllable to be a Vowel, "
                                    + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the part used, was : \""
                                    + mid.get(b[i])
                                    + "\", which "
                                    + "means there should be a suffix available, that has \"-v\" requirement or no requirements for previous syllables at all.");
                }

                if (consonantLast(pureSyl(mid.get(b[i]))) && allowCons(sur) == false) {
                    throw new RuntimeException(
                            "Expecting \"suffix\" part that allows last character of last syllable to be a consonant, "
                                    + "but there is none. You should add one, or remove requirements that cannot be fulfilled.. the part used, was : \""
                                    + mid.get(b[i])
                                    + "\", which "
                                    + "means there should be a suffix available, that has \"-c\" requirement or no requirements for previous syllables at all.");
                }
            }
            if (vowelLast(pureSyl(mid.get(b[i])))) {
                last = 1;
            } else {
                last = 2;
            }
        }

        int c;
        do {
            c = (int) (MyRandom.getInstance().nextDouble() * sur.size());
        } while (expecting == 1 && vowelFirst(pureSyl(sur.get(c))) == false || expecting == 2
                && consonantFirst(pureSyl(sur.get(c))) == false || last == 1 && hatesPreviousVowels(sur.get(c))
                || last == 2 && hatesPreviousConsonants(sur.get(c)));

        name = upper(pureSyl(pre.get(a).toLowerCase()));
        for (int i = 0; i < b.length - 2; i++) {
            name = name.concat(pureSyl(mid.get(b[i]).toLowerCase()));
        }
        if (syls > 1) {
            name = name.concat(pureSyl(sur.get(c).toLowerCase()));
        }
        return name;
    }

    /**
     * Refresh names from file. No need to call that method, if you are not changing the file during the operation of
     * program, as this method is called every time file name is changed or new NameGenerator object created.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws URISyntaxException the uRI syntax exception
     */
    public void refresh() throws IOException, URISyntaxException {

        BufferedReader bufRead;
        String line;
        bufRead = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(ref)));
        line = "";

        while (line != null) {
            line = bufRead.readLine();
            if (line != null && !"".equals(line)) {
                if (line.charAt(0) == '-') {
                    pre.add(line.substring(1).toLowerCase());
                } else if (line.charAt(0) == '+') {
                    sur.add(line.substring(1).toLowerCase());
                } else {
                    mid.add(line.toLowerCase());
                }
            }
        }
        bufRead.close();
    }
}
// CHECKSTYLE:ON
