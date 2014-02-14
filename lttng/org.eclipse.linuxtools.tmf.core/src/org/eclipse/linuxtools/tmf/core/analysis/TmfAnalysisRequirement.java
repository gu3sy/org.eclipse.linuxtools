package org.eclipse.linuxtools.tmf.core.analysis;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @since 3.0
 */
public class TmfAnalysisRequirement {

    private final String fType;
    private final Map<String, TmfValueLevel> fValues = new HashMap<>();

    public enum TmfValueLevel {
        MANDATORY,
        OPTIONAL,
        INFORMATIVE
    }

    public TmfAnalysisRequirement(String type) {
        fType = type;
    }

    /**
     * @param values
     * @param level
     */
    public TmfAnalysisRequirement(String type, Iterable<String> values, TmfValueLevel level) {
        fType = type;
        fValues.clear();
    }

    /**
     * @param req
     * @param level
     */
    public void merge(TmfAnalysisRequirement req, TmfValueLevel level) {
    }

    /**
     * @param req
     */
    public void merge(TmfAnalysisRequirement req) {
    }

    /**
     * @param values
     * @param level
     */
    public void addValues(Iterable<String> values, TmfValueLevel level) {
    }

    /**
     * @param value
     * @param level
     */
    public void addValue(String value, TmfValueLevel level) {
    }

    /**
     * @param value
     * @param level
     */
    public void modifyValueLevel(String value, TmfValueLevel level) {
    }

    public String getType() {
        return fType;
    }

    public List<String> getValues() {
        return null;
    }

    /**
     * @param value
     */
    public TmfValueLevel getValueLevel(String value) {
        return null;
    }
}
