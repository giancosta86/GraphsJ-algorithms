/*§
  ===========================================================================
  GraphsJ - Algorithms
  ===========================================================================
  Copyright (C) 2009-2015 Gianluca Costa
  ===========================================================================
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  ===========================================================================
*/

package info.gianlucacosta.graphsj3.algorithms.cpm;

import info.gianlucacosta.arcontes.algorithms.AlgorithmException;
import info.gianlucacosta.arcontes.algorithms.AlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.ScriptedAlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.test.CommonAlgorithmTest;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultNameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultWeightInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.graphsj3.algorithms.cpm.metainfo.CpmVertexInfo;
import info.gianlucacosta.helios.metainfo.DefaultMetaInfoRepository;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CpmTest extends CommonAlgorithmTest<Cpm> {

    private Vertex v0;
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;
    private Vertex v5;
    private Vertex v6;
    private Vertex v7;
    private Vertex v8;
    private Vertex v9;
    private Vertex v10;
    private Link v0_v1;
    private Link v0_v2;
    private Link v1_v4;
    private Link v2_v3;
    private Link v3_v4;
    private Link v3_v6;
    private Link v4_v5;
    private Link v5_v6;
    private Link v6_v7;
    private Link v7_v8;
    private Link v7_v9;
    private Link v8_v10;
    private Link v9_v10;

    @Override
    protected GraphContext createGraphContext() {
        Graph graph = new DefaultGraph();

        v0 = new DefaultVertex();
        graph.addVertex(v0);

        v1 = new DefaultVertex();
        graph.addVertex(v1);

        v2 = new DefaultVertex();
        graph.addVertex(v2);

        v3 = new DefaultVertex();
        graph.addVertex(v3);

        v4 = new DefaultVertex();
        graph.addVertex(v4);

        v5 = new DefaultVertex();
        graph.addVertex(v5);

        v6 = new DefaultVertex();
        graph.addVertex(v6);

        v7 = new DefaultVertex();
        graph.addVertex(v7);

        v8 = new DefaultVertex();
        graph.addVertex(v8);

        v9 = new DefaultVertex();
        graph.addVertex(v9);

        v10 = new DefaultVertex();
        graph.addVertex(v10);

        v0_v1 = new DefaultLink(v0, v1);
        graph.addLink(v0_v1);

        v0_v2 = new DefaultLink(v0, v2);
        graph.addLink(v0_v2);

        v1_v4 = new DefaultLink(v1, v4);
        graph.addLink(v1_v4);

        v2_v3 = new DefaultLink(v2, v3);
        graph.addLink(v2_v3);

        v3_v4 = new DefaultLink(v3, v4);
        graph.addLink(v3_v4);

        v3_v6 = new DefaultLink(v3, v6);
        graph.addLink(v3_v6);

        v4_v5 = new DefaultLink(v4, v5);
        graph.addLink(v4_v5);

        v5_v6 = new DefaultLink(v5, v6);
        graph.addLink(v5_v6);

        v6_v7 = new DefaultLink(v6, v7);
        graph.addLink(v6_v7);

        v7_v8 = new DefaultLink(v7, v8);
        graph.addLink(v7_v8);

        v7_v9 = new DefaultLink(v7, v9);
        graph.addLink(v7_v9);

        v8_v10 = new DefaultLink(v8, v10);
        graph.addLink(v8_v10);

        v9_v10 = new DefaultLink(v9, v10);
        graph.addLink(v9_v10);

        MetaInfoRepository metaInfoRepository = new DefaultMetaInfoRepository();

        metaInfoRepository.putMetaInfo(v0, new DefaultNameInfo("V0"));
        metaInfoRepository.putMetaInfo(v1, new DefaultNameInfo("V1"));
        metaInfoRepository.putMetaInfo(v2, new DefaultNameInfo("V2"));
        metaInfoRepository.putMetaInfo(v3, new DefaultNameInfo("V3"));
        metaInfoRepository.putMetaInfo(v4, new DefaultNameInfo("V4"));
        metaInfoRepository.putMetaInfo(v5, new DefaultNameInfo("V5"));
        metaInfoRepository.putMetaInfo(v6, new DefaultNameInfo("V6"));
        metaInfoRepository.putMetaInfo(v7, new DefaultNameInfo("V7"));
        metaInfoRepository.putMetaInfo(v8, new DefaultNameInfo("V8"));
        metaInfoRepository.putMetaInfo(v9, new DefaultNameInfo("V9"));
        metaInfoRepository.putMetaInfo(v10, new DefaultNameInfo("V10"));

        metaInfoRepository.putMetaInfo(v0_v1, new DefaultNameInfo(""));
        metaInfoRepository.putMetaInfo(v0_v2, new DefaultNameInfo(""));
        metaInfoRepository.putMetaInfo(v1_v4, new DefaultNameInfo("A"));
        metaInfoRepository.putMetaInfo(v2_v3, new DefaultNameInfo("B"));
        metaInfoRepository.putMetaInfo(v3_v4, new DefaultNameInfo(""));
        metaInfoRepository.putMetaInfo(v3_v6, new DefaultNameInfo("E"));
        metaInfoRepository.putMetaInfo(v4_v5, new DefaultNameInfo("C"));
        metaInfoRepository.putMetaInfo(v5_v6, new DefaultNameInfo("D"));
        metaInfoRepository.putMetaInfo(v6_v7, new DefaultNameInfo("F"));
        metaInfoRepository.putMetaInfo(v7_v8, new DefaultNameInfo("H"));
        metaInfoRepository.putMetaInfo(v7_v9, new DefaultNameInfo("G"));
        metaInfoRepository.putMetaInfo(v8_v10, new DefaultNameInfo(""));
        metaInfoRepository.putMetaInfo(v9_v10, new DefaultNameInfo(""));

        metaInfoRepository.putMetaInfo(v0_v1, new DefaultWeightInfo<>(0.0));
        metaInfoRepository.putMetaInfo(v0_v2, new DefaultWeightInfo<>(0.0));
        metaInfoRepository.putMetaInfo(v1_v4, new DefaultWeightInfo<>(10.0));
        metaInfoRepository.putMetaInfo(v2_v3, new DefaultWeightInfo<>(5.0));
        metaInfoRepository.putMetaInfo(v3_v4, new DefaultWeightInfo<>(0.0));
        metaInfoRepository.putMetaInfo(v3_v6, new DefaultWeightInfo<>(3.0));
        metaInfoRepository.putMetaInfo(v4_v5, new DefaultWeightInfo<>(5.0));
        metaInfoRepository.putMetaInfo(v5_v6, new DefaultWeightInfo<>(1.0));
        metaInfoRepository.putMetaInfo(v6_v7, new DefaultWeightInfo<>(2.0));
        metaInfoRepository.putMetaInfo(v7_v8, new DefaultWeightInfo<>(2.0));
        metaInfoRepository.putMetaInfo(v7_v9, new DefaultWeightInfo<>(1.0));
        metaInfoRepository.putMetaInfo(v8_v10, new DefaultWeightInfo<>(0.0));
        metaInfoRepository.putMetaInfo(v9_v10, new DefaultWeightInfo<>(0.0));

        return new DefaultGraphContext(graph, metaInfoRepository);
    }

    @Override
    protected AlgorithmInput createAlgorithmInput() {
        return new ScriptedAlgorithmInput() {
            @Override
            protected Queue<Vertex> initInputVertexes() {
                LinkedList<Vertex> result = new LinkedList<>();

                result.add(v0);
                result.add(v10);

                return result;
            }

        };
    }

    @Override
    protected Cpm createAlgorithm() {
        return new Cpm(getGraphContext(), getAlgorithmSettings(), getAlgorithmInput(), getAlgorithmOutput());
    }

    private void testStructuralMetaInfo() {
        testVertexNames();
        testLinkNames();
        testLinkWeights();
    }

    private void testVertexNames() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, NameInfo.class).getName(), equalTo("V0"));
        assertThat(metaInfoRepository.getMetaInfo(v1, NameInfo.class).getName(), equalTo("V1"));
        assertThat(metaInfoRepository.getMetaInfo(v2, NameInfo.class).getName(), equalTo("V2"));
        assertThat(metaInfoRepository.getMetaInfo(v3, NameInfo.class).getName(), equalTo("V3"));
        assertThat(metaInfoRepository.getMetaInfo(v4, NameInfo.class).getName(), equalTo("V4"));
        assertThat(metaInfoRepository.getMetaInfo(v5, NameInfo.class).getName(), equalTo("V5"));
        assertThat(metaInfoRepository.getMetaInfo(v6, NameInfo.class).getName(), equalTo("V6"));
        assertThat(metaInfoRepository.getMetaInfo(v7, NameInfo.class).getName(), equalTo("V7"));
        assertThat(metaInfoRepository.getMetaInfo(v8, NameInfo.class).getName(), equalTo("V8"));
        assertThat(metaInfoRepository.getMetaInfo(v9, NameInfo.class).getName(), equalTo("V9"));
        assertThat(metaInfoRepository.getMetaInfo(v10, NameInfo.class).getName(), equalTo("V10"));
    }

    private void testLinkNames() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v0_v1, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v0_v2, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class)).getWeight(), equalTo(10.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v3, WeightInfo.class)).getWeight(), equalTo(5.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v4, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v6, WeightInfo.class)).getWeight(), equalTo(3.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class)).getWeight(), equalTo(5.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v5_v6, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v6_v7, WeightInfo.class)).getWeight(), equalTo(2.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v7_v8, WeightInfo.class)).getWeight(), equalTo(2.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v7_v9, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v8_v10, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v9_v10, WeightInfo.class)).getWeight(), equalTo(0.0));
    }

    private void testLinkWeights() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v0_v1, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v0_v2, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class)).getWeight(), equalTo(10.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v3, WeightInfo.class)).getWeight(), equalTo(5.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v4, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v6, WeightInfo.class)).getWeight(), equalTo(3.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class)).getWeight(), equalTo(5.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v5_v6, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v6_v7, WeightInfo.class)).getWeight(), equalTo(2.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v7_v8, WeightInfo.class)).getWeight(), equalTo(2.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v7_v9, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v8_v10, WeightInfo.class)).getWeight(), equalTo(0.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v9_v10, WeightInfo.class)).getWeight(), equalTo(0.0));
    }

    @Test
    public void theAlgorithm_shouldRunTheCorrectNumberOfSteps() throws AlgorithmException {
        fullRun();

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCurrentStep(), equalTo(3));
    }

    private void testVertexIndexes() {
        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, CpmVertexInfo.class).getIndex(), equalTo(0));
        assertThat(metaInfoRepository.getMetaInfo(v1, CpmVertexInfo.class).getIndex(), equalTo(1));
        assertThat(metaInfoRepository.getMetaInfo(v2, CpmVertexInfo.class).getIndex(), equalTo(2));
        assertThat(metaInfoRepository.getMetaInfo(v3, CpmVertexInfo.class).getIndex(), equalTo(3));
        assertThat(metaInfoRepository.getMetaInfo(v4, CpmVertexInfo.class).getIndex(), equalTo(4));
        assertThat(metaInfoRepository.getMetaInfo(v5, CpmVertexInfo.class).getIndex(), equalTo(5));
        assertThat(metaInfoRepository.getMetaInfo(v6, CpmVertexInfo.class).getIndex(), equalTo(6));
        assertThat(metaInfoRepository.getMetaInfo(v7, CpmVertexInfo.class).getIndex(), equalTo(7));
        assertThat(metaInfoRepository.getMetaInfo(v8, CpmVertexInfo.class).getIndex(), equalTo(8));
        assertThat(metaInfoRepository.getMetaInfo(v9, CpmVertexInfo.class).getIndex(), equalTo(9));
        assertThat(metaInfoRepository.getMetaInfo(v10, CpmVertexInfo.class).getIndex(), equalTo(10));
    }

    private void asserTMinsZero() {
        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v1, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v2, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v3, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v4, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v5, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v6, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v7, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v8, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v9, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v10, CpmVertexInfo.class).getTMin(), equalTo(0.0));
    }

    private void testTMins() {
        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v1, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v2, CpmVertexInfo.class).getTMin(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v3, CpmVertexInfo.class).getTMin(), equalTo(5.0));
        assertThat(metaInfoRepository.getMetaInfo(v4, CpmVertexInfo.class).getTMin(), equalTo(10.0));
        assertThat(metaInfoRepository.getMetaInfo(v5, CpmVertexInfo.class).getTMin(), equalTo(15.0));
        assertThat(metaInfoRepository.getMetaInfo(v6, CpmVertexInfo.class).getTMin(), equalTo(16.0));
        assertThat(metaInfoRepository.getMetaInfo(v7, CpmVertexInfo.class).getTMin(), equalTo(18.0));
        assertThat(metaInfoRepository.getMetaInfo(v8, CpmVertexInfo.class).getTMin(), equalTo(20.0));
        assertThat(metaInfoRepository.getMetaInfo(v9, CpmVertexInfo.class).getTMin(), equalTo(19.0));
        assertThat(metaInfoRepository.getMetaInfo(v10, CpmVertexInfo.class).getTMin(), equalTo(20.0));
    }

    private void assertTMaxesZero() {
        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v1, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v2, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v3, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v4, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v5, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v6, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v7, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v8, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v9, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v10, CpmVertexInfo.class).getTMax(), equalTo(0.0));
    }

    private void testTMaxes() {
        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v0, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v1, CpmVertexInfo.class).getTMax(), equalTo(0.0));
        assertThat(metaInfoRepository.getMetaInfo(v2, CpmVertexInfo.class).getTMax(), equalTo(5.0));
        assertThat(metaInfoRepository.getMetaInfo(v3, CpmVertexInfo.class).getTMax(), equalTo(10.0));
        assertThat(metaInfoRepository.getMetaInfo(v4, CpmVertexInfo.class).getTMax(), equalTo(10.0));
        assertThat(metaInfoRepository.getMetaInfo(v5, CpmVertexInfo.class).getTMax(), equalTo(15.0));
        assertThat(metaInfoRepository.getMetaInfo(v6, CpmVertexInfo.class).getTMax(), equalTo(16.0));
        assertThat(metaInfoRepository.getMetaInfo(v7, CpmVertexInfo.class).getTMax(), equalTo(18.0));
        assertThat(metaInfoRepository.getMetaInfo(v8, CpmVertexInfo.class).getTMax(), equalTo(20.0));
        assertThat(metaInfoRepository.getMetaInfo(v9, CpmVertexInfo.class).getTMax(), equalTo(20.0));
        assertThat(metaInfoRepository.getMetaInfo(v10, CpmVertexInfo.class).getTMax(), equalTo(20.0));
    }

    @Test
    public void vertexIndexes_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testVertexIndexes();
    }

    @Test
    public void vertexIndexes_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testVertexIndexes();
    }

    @Test
    public void vertexIndexes_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testVertexIndexes();
    }

    @Test
    public void tMins_shouldBeZeroAfterStep1() throws AlgorithmException {
        runSteps(1);

        asserTMinsZero();
    }

    @Test
    public void tMins_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testTMins();
    }

    @Test
    public void tMins_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testTMins();
    }

    @Test
    public void tMaxes_shouldBeZeroAfterStep1() throws AlgorithmException {
        runSteps(1);

        assertTMaxesZero();
    }

    @Test
    public void tMaxes_shouldBeZeroAfterStep2() throws AlgorithmException {
        runSteps(2);

        assertTMaxesZero();
    }

    @Test
    public void tMaxes_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testTMaxes();
    }

    @Test
    public void getCriticalActivities_shouldBeEmptyAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCriticalActivities(), empty());
    }

    @Test
    public void getCriticalActivities_shouldBeEmptyAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCriticalActivities(), empty());
    }

    @Test
    public void getCriticalActivities_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(
                getAlgorithm().getCriticalActivities(),
                contains(
                        new Activity("A", 10, 0, 0, v1_v4),
                        new Activity("C", 5, 10, 10, v4_v5),
                        new Activity("D", 1, 15, 15, v5_v6),
                        new Activity("F", 2, 16, 16, v6_v7),
                        new Activity("H", 2, 18, 18, v7_v8)));
    }

    @Test
    public void getCriticalLinks_shouldBeEmptyAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCriticalLinks(), empty());
    }

    @Test
    public void getCriticalLinks_shouldBeEmptyAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCriticalLinks(), empty());
    }

    @Test
    public void getCriticalLinks_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCriticalLinks(), containsInAnyOrder(
                v1_v4,
                v4_v5,
                v5_v6,
                v6_v7,
                v7_v8));
    }

}
