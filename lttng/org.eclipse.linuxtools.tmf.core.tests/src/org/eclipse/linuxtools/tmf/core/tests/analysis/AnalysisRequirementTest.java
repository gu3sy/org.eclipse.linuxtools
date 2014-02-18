/*******************************************************************************
 * Copyright (c) 2014 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Guilliano Molaire - Initial API and implementation
 *   Mathieu Rail - Initial API and implementation
 *******************************************************************************/

package org.eclipse.linuxtools.tmf.core.tests.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.linuxtools.tmf.core.analysis.TmfAnalysisRequirement;
import org.eclipse.linuxtools.tmf.core.analysis.TmfAnalysisRequirement.AnalysisRequirementValueLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test suite for the {@link TmfAnalysisRequirement} class.
 *
 * @author Guilliano Molaire
 * @author Mathieu Rail
 */
public class AnalysisRequirementTest {

    private static TmfAnalysisRequirement requirement;
    private static TmfAnalysisRequirement subRequirement;

    /**
     * Test suite for the {@link TmfAnalysisRequirement#addInformations} and the
     * {@link TmfAnalysisRequirement#getInformations} methods.
     */
    @Test
    public void testAddAndGetInformations() {
        requirement = new TmfAnalysisRequirement("Requirement");

        requirement.addInformation("This is a test.");
        requirement.addInformation("This is another test.");

        List<String> informations = requirement.getInformations();

        assertEquals(2, informations.size());

        assertTrue(informations.contains("This is a test."));
        assertTrue(informations.contains("This is another test."));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#addValues} and the
     * {@link TmfAnalysisRequirement#addValue} methods.
     */
    @Test
    public void testAddValuesToRequirement() {
        requirement = new TmfAnalysisRequirement("Requirement");

        assertEquals(0, requirement.getValues().size());

        List<String> values = new ArrayList<>();
        values.add("Value A");
        values.add("Value B");
        values.add("Value C");
        values.add("Value C");

        /*
         * Add values to the requirement with the same level, Value C should
         * only exist once
         */
        requirement.addValues(values, AnalysisRequirementValueLevel.MANDATORY);
        assertEquals(3, requirement.getValues().size());

        /* Try to modify the level of a value by adding it a second time */
        requirement.addValue("Value D", AnalysisRequirementValueLevel.OPTIONAL);
        requirement.addValue("Value D", AnalysisRequirementValueLevel.MANDATORY);

        assertEquals(4, requirement.getValues().size());
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#getValueLevel} method.
     */
    @Test
    public void testGetValueLevel() {
        requirement = new TmfAnalysisRequirement("Requirement");
        requirement.addValue("Value A", AnalysisRequirementValueLevel.OPTIONAL);

        /* Try to get the level of a value */
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value A"));

        /* Try to get the level of a value that doesn't exist */
        assertNull(requirement.getValueLevel("Value B"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#modifyValueLevel}
     * method.
     */
    @Test
    public void testModifyValueLevel() {
        requirement = new TmfAnalysisRequirement("Requirement");
        requirement.addValue("Value A", AnalysisRequirementValueLevel.OPTIONAL);

        /* Modify the level of value A */
        assertTrue(requirement.modifyValueLevel("Value A", AnalysisRequirementValueLevel.MANDATORY));
        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value A"));

        /* Try to modify the level of a value that doesn't exist */
        assertFalse(requirement.modifyValueLevel("Value B", AnalysisRequirementValueLevel.OPTIONAL));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#merge} method with the
     * parameter value MANDATORY.
     */
    @Test
    public void testMergeMandatory() {
        initMergeRequirements();

        requirement.merge(subRequirement, AnalysisRequirementValueLevel.MANDATORY);

        assertEquals(requirement.getValues().size(), 6);

        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value A"));
        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value B"));

        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value C"));
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));

        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value E"));
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value F"));
    }

    /**
     * Test suite for the {@link TmfAnalysisRequirement#merge} method with the
     * parameter value OPTIONAL.
     */
    @Test
    public void testMergeOptional() {
        initMergeRequirements();

        requirement.merge(subRequirement, AnalysisRequirementValueLevel.OPTIONAL);

        assertEquals(requirement.getValues().size(), 6);

        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value A"));
        assertEquals(AnalysisRequirementValueLevel.MANDATORY, requirement.getValueLevel("Value B"));

        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value C"));
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value D"));

        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value E"));
        assertEquals(AnalysisRequirementValueLevel.OPTIONAL, requirement.getValueLevel("Value F"));
    }

    /**
     * Initialize the requirement and sub-requirement for the merge tests.
     */
    private static void initMergeRequirements() {
        requirement = new TmfAnalysisRequirement("Requirement");
        requirement.addValue("Value A", AnalysisRequirementValueLevel.MANDATORY);
        requirement.addValue("Value B", AnalysisRequirementValueLevel.MANDATORY);

        requirement.addValue("Value C", AnalysisRequirementValueLevel.OPTIONAL);
        requirement.addValue("Value D", AnalysisRequirementValueLevel.OPTIONAL);

        /* This sub-requirement will be merged into requirement */
        subRequirement = new TmfAnalysisRequirement("Sub-requirement");
        subRequirement.addValue("Value A", AnalysisRequirementValueLevel.MANDATORY);
        subRequirement.addValue("Value B", AnalysisRequirementValueLevel.OPTIONAL);

        subRequirement.addValue("Value C", AnalysisRequirementValueLevel.MANDATORY);
        subRequirement.addValue("Value D", AnalysisRequirementValueLevel.OPTIONAL);

        subRequirement.addValue("Value E", AnalysisRequirementValueLevel.MANDATORY);
        subRequirement.addValue("Value F", AnalysisRequirementValueLevel.OPTIONAL);
    }
}
