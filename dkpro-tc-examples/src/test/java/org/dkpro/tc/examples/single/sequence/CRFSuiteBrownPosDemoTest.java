/**
 * Copyright 2016
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package org.dkpro.tc.examples.single.sequence;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.dkpro.lab.task.Dimension;
import org.dkpro.lab.task.ParameterSpace;
import org.dkpro.tc.core.Constants;
import org.dkpro.tc.crfsuite.CRFSuiteAdapter;
import org.dkpro.tc.crfsuite.task.CRFSuiteTestTask;
import org.dkpro.tc.evaluation.Id2Outcome;
import org.dkpro.tc.evaluation.evaluator.EvaluatorBase;
import org.dkpro.tc.evaluation.evaluator.EvaluatorFactory;
import org.dkpro.tc.evaluation.measures.label.Accuracy;
import org.dkpro.tc.examples.utils.JavaDemosTest_Base;
import org.junit.Before;
import org.junit.Test;

/**
 * This test just ensures that the experiment runs without throwing any exception.
 */
public class CRFSuiteBrownPosDemoTest
    extends JavaDemosTest_Base
{
    CRFSuiteBrownPosDemo javaExperiment;

    @Override
    @Before
    public void setup()
        throws Exception
    {
        super.setup();
        javaExperiment = new CRFSuiteBrownPosDemo();
    }

    @Test
    public void testJavaCrossValidation()
        throws Exception
    {
        @SuppressWarnings("unchecked")
        Dimension<List<String>> dimClassificationArgs = Dimension.create(
                Constants.DIM_CLASSIFICATION_ARGS,
                asList(new String[] { CRFSuiteAdapter.ALGORITHM_AVERAGED_PERCEPTRON }));
        ParameterSpace pSpace = CRFSuiteBrownPosDemo.getParameterSpace(Constants.FM_SEQUENCE,
                Constants.LM_SINGLE_LABEL, dimClassificationArgs);

        javaExperiment.runCrossValidation(pSpace);
    }

    @Test
    public void testTrainTest()
        throws Exception
    {
        @SuppressWarnings("unchecked")
        // Random parameters for demonstration!
        Dimension<List<String>> dimClassificationArgs = Dimension.create(
                Constants.DIM_CLASSIFICATION_ARGS,
                asList(CRFSuiteAdapter.ALGORITHM_ADAPTIVE_REGULARIZATION_OF_WEIGHT_VECTOR));
        ParameterSpace pSpace = CRFSuiteBrownPosDemo.getParameterSpace(Constants.FM_SEQUENCE,
                Constants.LM_SINGLE_LABEL, dimClassificationArgs);

        ContextMemoryReport.key = CRFSuiteTestTask.class.getName();
        javaExperiment.runTrainTest(pSpace);

        Id2Outcome o = new Id2Outcome(ContextMemoryReport.id2outcome, Constants.LM_SINGLE_LABEL);
        EvaluatorBase createEvaluator = EvaluatorFactory.createEvaluator(o, true, false);
        Double double1 = createEvaluator.calculateEvaluationMeasures()
                .get(Accuracy.class.getSimpleName());
        assertEquals(0.3225806, double1, 0.0001);
    }

    @Test
    public void testTrainTestWithParametrization()
        throws Exception
    {
        @SuppressWarnings("unchecked")
        // Random parameters for demonstration!
        Dimension<List<String>> dimClassificationArgs = Dimension.create(
                Constants.DIM_CLASSIFICATION_ARGS,
                asList(new String[] {
                        CRFSuiteAdapter.ALGORITHM_ADAPTIVE_REGULARIZATION_OF_WEIGHT_VECTOR, "-p",
                        "feature.minfreq=3", "-p", "gamma=5.0", "-p", "max_iterations=15" }));
        ParameterSpace pSpace = CRFSuiteBrownPosDemo.getParameterSpace(Constants.FM_SEQUENCE,
                Constants.LM_SINGLE_LABEL, dimClassificationArgs);

        ContextMemoryReport.key = CRFSuiteTestTask.class.getName();
        javaExperiment.runTrainTest(pSpace);

        Id2Outcome o = new Id2Outcome(ContextMemoryReport.id2outcome, Constants.LM_SINGLE_LABEL);
        EvaluatorBase createEvaluator = EvaluatorFactory.createEvaluator(o, true, false);
        Double double1 = createEvaluator.calculateEvaluationMeasures()
                .get(Accuracy.class.getSimpleName());
        assertEquals(0.290322, double1, 0.0001);
    }
}
