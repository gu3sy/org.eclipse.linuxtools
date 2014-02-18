/*******************************************************************************
 * Copyright (c) 2014 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mathieu Rail
 *   Guilliano Molaire
 *******************************************************************************/

package org.eclipse.linuxtools.tmf.core.tests.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.linuxtools.tmf.core.analysis.TmfAnalysisRequirement;
import org.eclipse.linuxtools.tmf.core.analysis.TmfAnalysisRequirement.TmfValueLevel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link TmfAnalysisRequirement} class.
 */
public class AnalysisRequirementTest {

    private static TmfAnalysisRequirement requirement;
    private static TmfAnalysisRequirement subRequirement;

    /**
     * Test suite for the {@link TmfAnalysisRequirement#addValues} and the
     * {@link TmfAnalysisRequirement#addValue} methods.
     */
    @Test
    public void testAddValuesToRequirement() {
        requirement = new TmfAnalysisRequirement("Requirement");

        List<String> values = new ArrayList<>();
        values.add("Value A");
        values.add("Value B");
        values.add("Value C");
        values.add("Value C");

        /* Add values to the requirement with the same level, Value C should only exist once */
        requirement.addValues(values, TmfValueLevel.MANDATORY);
        assertEquals(3, requirement.getValues().size());

        //TODO: Test all values to see if they are there and if their level is the same?

        requirement.addValue("Value D", TmfValueLevel.OPTIONAL);
        requirement.addValue("Value D", TmfValueLevel.MANDATORY);
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#modifyValueLevel} method.
     */
    @Test
    public void testModifyValueLevel() {
        requirement = new TmfAnalysisRequirement("Requirement");

        requirement.addValue("Value A", TmfValueLevel.OPTIONAL);
        requirement.modifyValueLevel("Value A", TmfValueLevel.MANDATORY);

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value A"));

        //TODO: test cases where we modify the level of a value that doesn't exist?
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#merge} method
     * with the parameter value MANDATORY.
     */
    @Test
    public void testMergeMandatory() {
        initMergeRequirements();

        requirement.merge(subRequirement, TmfValueLevel.MANDATORY);

        assertEquals(requirement.getValues().size(), 12);

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value A"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value B"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value C"));

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value D"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value E"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value F"));

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value G"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value H"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value I"));

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value J"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value K"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value L"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#merge} method
     * with the parameter value OPTIONAL.
     */
    @Test
    public void testMergeOptional() {
        initMergeRequirements();

        requirement.merge(subRequirement, TmfValueLevel.OPTIONAL);

        assertEquals(requirement.getValues().size(), 12);

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value A"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value B"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value C"));

        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value E"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value F"));

        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value G"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value H"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value I"));

        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value J"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value K"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value L"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#merge} method
     * with the parameter value INFORMATIVE.
     */
    @Test
    public void testMergeInformative() {
        //TODO: Is an informative merge even possible?
        initMergeRequirements();

        requirement.merge(subRequirement, TmfValueLevel.INFORMATIVE);

        assertEquals(requirement.getValues().size(), 12);

        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value A"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value B"));
        assertEquals(TmfValueLevel.MANDATORY, requirement.getValueLevel("Value C"));

        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value E"));
        assertEquals(TmfValueLevel.OPTIONAL, requirement.getValueLevel("Value F"));

        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value G"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value H"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value I"));

        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value J"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value K"));
        assertEquals(TmfValueLevel.INFORMATIVE, requirement.getValueLevel("Value L"));
    }

    /**
     * Initialize the requirement and subrequirement for the merge tests.
     */
    private static void initMergeRequirements() {
        requirement = new TmfAnalysisRequirement("Requirement");
        requirement.addValue("Value A", TmfValueLevel.MANDATORY);
        requirement.addValue("Value B", TmfValueLevel.MANDATORY);
        requirement.addValue("Value C", TmfValueLevel.MANDATORY);

        requirement.addValue("Value D", TmfValueLevel.OPTIONAL);
        requirement.addValue("Value E", TmfValueLevel.OPTIONAL);
        requirement.addValue("Value F", TmfValueLevel.OPTIONAL);

        requirement.addValue("Value G", TmfValueLevel.INFORMATIVE);
        requirement.addValue("Value H", TmfValueLevel.INFORMATIVE);
        requirement.addValue("Value I", TmfValueLevel.INFORMATIVE);

        /* This sub-requirement will be merged into requirement */
        subRequirement = new TmfAnalysisRequirement("Sub-requirement");
        subRequirement.addValue("Value A", TmfValueLevel.MANDATORY);
        subRequirement.addValue("Value B", TmfValueLevel.OPTIONAL);
        subRequirement.addValue("Value C", TmfValueLevel.INFORMATIVE);

        subRequirement.addValue("Value D", TmfValueLevel.MANDATORY);
        subRequirement.addValue("Value E", TmfValueLevel.OPTIONAL);
        subRequirement.addValue("Value F", TmfValueLevel.INFORMATIVE);

        subRequirement.addValue("Value G", TmfValueLevel.MANDATORY);
        subRequirement.addValue("Value H", TmfValueLevel.OPTIONAL);
        subRequirement.addValue("Value I", TmfValueLevel.INFORMATIVE);

        subRequirement.addValue("Value J", TmfValueLevel.MANDATORY);
        subRequirement.addValue("Value K", TmfValueLevel.OPTIONAL);
        subRequirement.addValue("Value L", TmfValueLevel.INFORMATIVE);
    }
}
