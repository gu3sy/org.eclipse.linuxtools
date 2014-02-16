package org.eclipse.linuxtools.tmf.core.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @since 3.0
 */
public class TmfAnalysisRequirement {

    private final String fType;
    private final Map<String, TmfValueLevel> fValues = new HashMap<>();

    /**
     * @author
     *
     */
    public enum TmfValueLevel {
        MANDATORY,
        OPTIONAL,
        INFORMATIVE
    }

    /**
     * @param type
     */
    public TmfAnalysisRequirement(String type) {
        fType = type;
    }

    /**
     * @param values
     * @param level
     */
    public TmfAnalysisRequirement(String type, Iterable<String> values, TmfValueLevel level) {
        fType = type;

		for(String value : values) {
			fValues.put(value, level);
		}
    }

    /**
     * @param req
     * @param level
     */
    public void merge(TmfAnalysisRequirement req, TmfValueLevel level) {
		/* If a value is already in a requirement, we update the level by the highest option (MANDATORY > OPTIONAL > INFORMATIVE) */
		List<String> values = req.getValues();
		for(String value: values) {
			if(fValues.containsKey(value)) {
				TmfValueLevel currentLevel = getValueLevel(value);
				TmfValueLevel highestLevel = (currentLevel.ordinal() < level.ordinal()) ? currentLevel: level;
				modifyValueLevel(value, highestLevel);
			}
			else {
				addValue(value, level);
			}
		}
    }

    /**
     * @param req
     */
    public void merge(TmfAnalysisRequirement req) {
		/* If a value is already in a requirement, we update the level by the highest option (MANDATORY > OPTIONAL > INFORMATIVE) */
		List<String> values = req.getValues();
		for(String value: values) {
			TmfValueLevel reqLevel = req.getValueLevel(value);

			if(fValues.containsKey(value)) {
				TmfValueLevel currentLevel = getValueLevel(value);
				TmfValueLevel highestLevel = (currentLevel.ordinal() < reqLevel.ordinal()) ? currentLevel: reqLevel;
				modifyValueLevel(value, highestLevel);
			}
			else {
				addValue(value, reqLevel);
			}
		}
    }

    /**
     * @param values
     * @param level
     */
    public void addValues(Iterable<String> values, TmfValueLevel level) {
		for(String value : values) {
			//TODO: do we test if the adding was successful?
			addValue(value, level);
		}
    }

    /**
     * @param value
     * @param level
     * @return
     */
    public boolean addValue(String value, TmfValueLevel level) {
		if(!fValues.containsKey(value)) {
			fValues.put(value, level);
			return true;
		}
		return false;
    }

    /**
     * @param value
     * @param level
     * @return
     */
    public boolean modifyValueLevel(String value, TmfValueLevel level) {
		//TODO: do we force an addValueLevel before a call to modify?
		if(fValues.containsKey(value)) {
			fValues.put(value, level);
			return true;
		}
		//TODO: return boolean that tells if the modifcation was successful?
		return false;
    }

    /**
     * @return
     */
    public String getType() {
        return fType;
    }

    /**
     * @return
     */
    public List<String> getValues() {
        return new ArrayList<>(fValues.keySet());
    }

    /**
     * @param value
     * @return
     */
    public TmfValueLevel getValueLevel(String value) {
        return fValues.get(value);
    }
}
