/*******************************************************************************
 * Copyright (c) 2014 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mathieu Rail - Initial API and implementation
 *   Guilliano Molaire - Initial API and implementation
 *******************************************************************************/

package org.eclipse.linuxtools.tmf.core.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class that contains all the values associated with a type needed by an
 * analysis in order to execute. Each value is peered with a level that
 * determines the importance of that specific value for the requirement.
 *
 * The type gives an indication about the kind of value the requirement
 * contains. The value should depend on the type. For instance, a requirement
 * type could be "event" and all the values that would be added in the
 * requirement object could indicate the possible event handled by the analysis.
 *
 * For these values, a level will be assigned indicating how important the value
 * is based on two possibilities: Mandatory or optional.
 *
 * @author Guilliano Molaire
 * @author Mathieu Rail
 * @since 3.0
 */
public class TmfAnalysisRequirement {

    private final String fType;
    private final Map<String, AnalysisRequirementValueLevel> fValues = new HashMap<>();
    private final List<String> fInformations = new ArrayList<>();

    /**
     * The possible level for each value
     *
     * @author Guilliano Molaire
     * @author Mathieu Rail
     */
    public enum AnalysisRequirementValueLevel {
        /** The value must be present at runtime (for the analysis) */
        MANDATORY,
        /** The value could be absent and the analysis would still work */
        OPTIONAL
    }

    /**
     * Default constructor
     *
     * @param type
     *            The type of the requirement
     */
    public TmfAnalysisRequirement(String type) {
        fType = type;
    }

    /**
     * Constructor. Instantiate a requirement object with a list of values of
     * the same level
     *
     * @param type
     *            The type of the requirement
     * @param values
     *            All the values associated with that type
     * @param level
     *            A level associated with all the values
     */
    public TmfAnalysisRequirement(String type, Iterable<String> values, AnalysisRequirementValueLevel level) {
        fType = type;
        addValues(values, level);
    }

    /**
     * Merges a requirement with the current one. All new values will take the
     * same level. If a value was already inside the current requirement, we
     * will override his level only if the new level is of higher priority.
     *
     * @param subRequirement
     *            The requirement to be merged
     * @param maxSubRequirementValueLevel
     *            The level associated with all the new values or currently
     *            lower priority ones
     */
    public void merge(TmfAnalysisRequirement subRequirement, AnalysisRequirementValueLevel maxSubRequirementValueLevel) {
        List<String> values = subRequirement.getValues();
        for (String value : values) {
            AnalysisRequirementValueLevel subRequirementValueLevel = subRequirement.getValueLevel(value);

            if (subRequirementValueLevel.ordinal() < maxSubRequirementValueLevel.ordinal()) {
                subRequirementValueLevel = maxSubRequirementValueLevel;
            }

            if (fValues.containsKey(value)) {
                /*
                 * If a value is already in a requirement, we update the level
                 * by the highest value between the current level in the
                 * requirement and the level of the value in the
                 * sub-requirement.
                 */
                AnalysisRequirementValueLevel requirementValueLevel = getValueLevel(value);
                AnalysisRequirementValueLevel highestLevel = (requirementValueLevel.ordinal() < subRequirementValueLevel.ordinal()) ? requirementValueLevel : subRequirementValueLevel;
                modifyValueLevel(value, highestLevel);
            }
            else {
                addValue(value, subRequirementValueLevel);
            }
        }
    }

    /**
     * Adds a list of value inside the requirement with the same level.
     *
     * @param values
     *            A list of value
     * @param level
     *            The level associated with all the values
     */
    public void addValues(Iterable<String> values, AnalysisRequirementValueLevel level) {
        for (String value : values) {
            addValue(value, level);
        }
    }

    /**
     * Adds a value with his associated level into the requirement. This method
     * does not modify an existing value. To do so, the user should call to
     * modifyValueLevel.
     *
     * @param value
     *            The value
     * @param level
     *            The level
     * @return True if the addition was successful
     */
    public boolean addValue(String value, AnalysisRequirementValueLevel level) {
        if (!fValues.containsKey(value)) {
            fValues.put(value, level);
            return true;
        }
        return false;
    }

    /**
     * Modifies an existing value level to a new level.
     *
     * @param value
     *            The value to be modified
     * @param level
     *            The new level to be associated with the value
     * @return True if the modification was successful
     */
    public boolean modifyValueLevel(String value, AnalysisRequirementValueLevel level) {
        if (fValues.containsKey(value)) {
            fValues.put(value, level);
            return true;
        }
        return false;
    }

    /**
     * Adds an information about the requirement.
     *
     * @param information
     *            The information to be added
     */
    public void addInformation(String information) {
        fInformations.add(information);
    }

    /**
     * Gets the requirement type. The type is read only.
     *
     * @return The type of this requirement
     */
    public String getType() {
        return fType;
    }

    /**
     * Gets all the values associated with the requirement
     *
     * @return The list of values
     */
    public List<String> getValues() {
        return new ArrayList<>(fValues.keySet());
    }

    /**
     * Gets informations about the requirement.
     *
     * @return The list of all the informations
     */
    public List<String> getInformations() {
        return fInformations;
    }

    /**
     * Gets the level associated with a particular type
     *
     * @param value
     *            The value
     * @return The level or null if the value does not exist
     */
    public AnalysisRequirementValueLevel getValueLevel(String value) {
        return fValues.get(value);
    }
}
