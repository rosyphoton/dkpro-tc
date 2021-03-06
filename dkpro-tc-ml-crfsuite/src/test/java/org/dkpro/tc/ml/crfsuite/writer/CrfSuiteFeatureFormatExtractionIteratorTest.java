/*******************************************************************************
 * Copyright 2018
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.dkpro.tc.ml.crfsuite.writer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dkpro.tc.api.features.Feature;
import org.dkpro.tc.api.features.FeatureType;
import org.dkpro.tc.api.features.Instance;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CrfSuiteFeatureFormatExtractionIteratorTest
{

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    List<Instance> fs;
    File outputDirectory;

    @Before
    public void setUp() throws Exception
    {
        buildFeatures();
        outputDirectory = folder.newFolder();
    }

    private void buildFeatures() throws Exception
    {
        fs = new ArrayList<>();

        List<Feature> features1 = new ArrayList<Feature>();
        features1.add(new Feature("feature1", 1.0, FeatureType.NUMERIC));
        features1.add(new Feature("feature2", 0.0, FeatureType.NUMERIC));
        features1.add(new Feature("feature3", "Water", FeatureType.STRING));

        List<Feature> features2 = new ArrayList<Feature>();
        features2.add(new Feature("feature2", 0.5, FeatureType.NUMERIC));
        features2.add(new Feature("feature1", 0.5, FeatureType.NUMERIC));
        features2.add(new Feature("feature3", "Fanta", FeatureType.STRING));

        Instance instance1 = new Instance(features1, "1");
        instance1.setJcasId(0);
        instance1.setSequenceId(0);
        instance1.setSequencePosition(0);
        Instance instance2 = new Instance(features2, "2");
        instance1.setJcasId(0);
        instance2.setSequenceId(0);
        instance2.setSequencePosition(1);
        Instance instance3 = new Instance(features1, "3");
        instance1.setJcasId(0);
        instance3.setSequenceId(0);
        instance3.setSequencePosition(2);

        Instance instance4 = new Instance(features1, "4");
        instance1.setJcasId(0);
        instance4.setSequenceId(1);
        instance4.setSequencePosition(0);
        Instance instance5 = new Instance(features2, "4");
        instance1.setJcasId(0);
        instance5.setSequenceId(1);
        instance5.setSequencePosition(1);

        fs.add(instance1);
        fs.add(instance2);
        fs.add(instance3);
        fs.add(instance4);
        fs.add(instance5);
    }

    @Test
    public void sequenceIteratorTest() throws Exception
    {

        CrfSuiteFeatureFormatExtractionIterator iterator = new CrfSuiteFeatureFormatExtractionIterator(
                fs);

        List<String> output = new ArrayList<String>();
        while (iterator.hasNext()) {
            StringBuilder next = iterator.next();
            output.add(next.toString());
        }
        assertEquals(2, output.size());
        assertEquals(
                "1\tfeature1=1.0\tfeature2=0.0\tfeature3=Water\t__BOS__" + "\n"
                        + "2\tfeature1=0.5\tfeature2=0.5\tfeature3=Fanta" + "\n"
                        + "3\tfeature1=1.0\tfeature2=0.0\tfeature3=Water\t__EOS__" + "\n",
                output.get(0));

        assertEquals(
                "4\tfeature1=1.0\tfeature2=0.0\tfeature3=Water\t__BOS__" + "\n"
                        + "4\tfeature1=0.5\tfeature2=0.5\tfeature3=Fanta\t__EOS__" + "\n",
                output.get(1));

    }

}
