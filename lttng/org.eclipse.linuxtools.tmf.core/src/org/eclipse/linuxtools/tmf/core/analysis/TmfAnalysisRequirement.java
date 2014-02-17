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
 * For these values, a level would be assigned indicating how important is that
 * value based on three possibility: Mandatory, Optional and Informative.
 *
 * @author Guilliano Molaire
 * @author Mathieu Rail
 * @since 3.0
 */
public class TmfAnalysisRequirement {

    private final String fType;
    private final Map<String, TmfValueLevel> fValues = new HashMap<>();

    /**
     * The possible level for each value
     *
     * @author Guilliano Molaire
     * @author Mathieu Rail
     *
     */
    public enum TmfValueLevel {
        /** The value must be present at runtime (for the analysis) */
        MANDATORY,
        /** The value could be absent and the analysis would still work */
        OPTIONAL,
        /** An information about the analysis */
        INFORMATIVE
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
    public TmfAnalysisRequirement(String type, Iterable<String> values, TmfValueLevel level) {
        fType = type;

        for (String value : values) {
            fValues.put(value, level);
        }
    }

    /**
     * Merges a requirement with the current one. All new values will take the
     * same level. If a value was already inside the current requirement, we
     * will override his level only if the new level is of higher priority.
     *
     * @param req
     *            The requirement to be merged
     * @param level
     *            The level associated with all the new values or currently
     *            lower priority ones
     */
    public void merge(TmfAnalysisRequirement req, TmfValueLevel level) {
        List<String> values = req.getValues();
        for (String value : values) {
            /*
             * If a value is already in a requirement, we update the level by
             * the highest option (MANDATORY > OPTIONAL > INFORMATIVE)
             */
            if (fValues.containsKey(value)) {
                TmfValueLevel currentLevel = getValueLevel(value);
                TmfValueLevel highestLevel = (currentLevel.ordinal() < level.ordinal()) ? currentLevel : level;
                modifyValueLevel(value, highestLevel);
            }
            else {
                addValue(value, level);
            }
        }
    }

    /**
     * Merges a requirement with the current one. If a value was already inside
     * the current requirement, we will override his level only if the new level
     * is of higher priority.
     *
     * @param req
     *            The requirement to be merged
     */
    public void merge(TmfAnalysisRequirement req) {
        List<String> values = req.getValues();
        for (String value : values) {
            TmfValueLevel reqLevel = req.getValueLevel(value);
            /*
             * If a value is already in a requirement, we update the level by
             * the highest option (MANDATORY > OPTIONAL > INFORMATIVE)
             */
            if (fValues.containsKey(value)) {
                TmfValueLevel currentLevel = getValueLevel(value);
                TmfValueLevel highestLevel = (currentLevel.ordinal() < reqLevel.ordinal()) ? currentLevel : reqLevel;
                modifyValueLevel(value, highestLevel);
            }
            else {
                addValue(value, reqLevel);
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
    public void addValues(Iterable<String> values, TmfValueLevel level) {
        for (String value : values) {
            // TODO: do we test if the adding was successful?
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
    public boolean addValue(String value, TmfValueLevel level) {
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
    public boolean modifyValueLevel(String value, TmfValueLevel level) {
        // TODO: do we force an addValueLevel before a call to modify?
        // TODO: can we modify a mandatory level to lower priority?
        if (fValues.containsKey(value)) {
            fValues.put(value, level);
            return true;
        }
        // TODO: return boolean that tells if the modification was successful?
        return false;
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
     * Gets the level associated with a particular type
     *
     * @param value
     *            The value
     * @return The level or null is the value does not exist
     */
    public TmfValueLevel getValueLevel(String value) {
        // TODO: do we want to return null?
        return fValues.get(value);
    }
}
