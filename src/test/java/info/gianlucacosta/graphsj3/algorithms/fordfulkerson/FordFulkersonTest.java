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

package info.gianlucacosta.graphsj3.algorithms.fordfulkerson;

import info.gianlucacosta.arcontes.algorithms.AlgorithmException;
import info.gianlucacosta.arcontes.algorithms.AlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.ScriptedAlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.test.CommonAlgorithmTest;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.arcontes.graphs.metainfo.*;
import info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo.DefaultFordFulkersonVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo.FordFulkersonLinkLabelInfo;
import info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo.FordFulkersonVertexInfo;
import info.gianlucacosta.helios.metainfo.DefaultMetaInfoRepository;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FordFulkersonTest extends CommonAlgorithmTest<FordFulkerson> {

    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;
    private Vertex v5;
    private Link v1_v2;
    private Link v1_v3;
    private Link v1_v4;
    private Link v2_v5;
    private Link v3_v2;
    private Link v3_v5;
    private Link v4_v3;
    private Link v4_v5;

    @Override
    protected GraphContext createGraphContext() {
        Graph graph = new DefaultGraph();

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

        v1_v2 = new DefaultLink(v1, v2);
        graph.addLink(v1_v2);

        v1_v3 = new DefaultLink(v1, v3);
        graph.addLink(v1_v3);

        v1_v4 = new DefaultLink(v1, v4);
        graph.addLink(v1_v4);

        v2_v5 = new DefaultLink(v2, v5);
        graph.addLink(v2_v5);

        v3_v2 = new DefaultLink(v3, v2);
        graph.addLink(v3_v2);

        v3_v5 = new DefaultLink(v3, v5);
        graph.addLink(v3_v5);

        v4_v3 = new DefaultLink(v4, v3);
        graph.addLink(v4_v3);

        v4_v5 = new DefaultLink(v4, v5);
        graph.addLink(v4_v5);

        MetaInfoRepository metaInfoRepository = new DefaultMetaInfoRepository();

        GraphContext graphContext = new DefaultGraphContext(graph, metaInfoRepository);

        metaInfoRepository.putMetaInfo(v1, new DefaultNameInfo("V1"));
        metaInfoRepository.putMetaInfo(v2, new DefaultNameInfo("V2"));
        metaInfoRepository.putMetaInfo(v3, new DefaultNameInfo("V3"));
        metaInfoRepository.putMetaInfo(v4, new DefaultNameInfo("V4"));
        metaInfoRepository.putMetaInfo(v5, new DefaultNameInfo("V5"));

        metaInfoRepository.putMetaInfo(v1_v2, new DefaultBoundedWeightInfo<>(3.0, 9.0));
        metaInfoRepository.putMetaInfo(v1_v2, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v1_v2));

        metaInfoRepository.putMetaInfo(v1_v3, new DefaultBoundedWeightInfo<>(3.0, 10.0));
        metaInfoRepository.putMetaInfo(v1_v3, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v1_v3));

        metaInfoRepository.putMetaInfo(v1_v4, new DefaultBoundedWeightInfo<>(10.0, 10.0));
        metaInfoRepository.putMetaInfo(v1_v4, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v1_v4));

        metaInfoRepository.putMetaInfo(v2_v5, new DefaultBoundedWeightInfo<>(10.0, 10.0));
        metaInfoRepository.putMetaInfo(v2_v5, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v2_v5));

        metaInfoRepository.putMetaInfo(v3_v2, new DefaultBoundedWeightInfo<>(7.0, 7.0));
        metaInfoRepository.putMetaInfo(v3_v2, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v3_v2));

        metaInfoRepository.putMetaInfo(v3_v5, new DefaultBoundedWeightInfo<>(6.0, 10.0));
        metaInfoRepository.putMetaInfo(v3_v5, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v3_v5));

        metaInfoRepository.putMetaInfo(v4_v3, new DefaultBoundedWeightInfo<>(10.0, 10.0));
        metaInfoRepository.putMetaInfo(v4_v3, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v4_v3));

        metaInfoRepository.putMetaInfo(v4_v5, new DefaultBoundedWeightInfo<>(0.0, 8.0));
        metaInfoRepository.putMetaInfo(v4_v5, new FordFulkersonLinkLabelInfo(graphContext.getMetaInfoRepository(), v4_v5));

        return graphContext;
    }

    @Override
    protected AlgorithmInput createAlgorithmInput() {
        return new ScriptedAlgorithmInput() {
            @Override
            protected Queue<Vertex> initInputVertexes() {
                LinkedList<Vertex> result = new LinkedList<>();

                result.add(v1);
                result.add(v5);

                return result;
            }

        };
    }

    @Override
    protected FordFulkerson createAlgorithm() {
        return new FordFulkerson(getGraphContext(), getAlgorithmSettings(), getAlgorithmInput(), getAlgorithmOutput());
    }

    private void testStructuralMetaInfo() {
        testVertexNames();
        testLinkCapacities();
    }

    private void testVertexNames() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, NameInfo.class).getName(), equalTo("V1"));
        assertThat(metaInfoRepository.getMetaInfo(v2, NameInfo.class).getName(), equalTo("V2"));
        assertThat(metaInfoRepository.getMetaInfo(v3, NameInfo.class).getName(), equalTo("V3"));
        assertThat(metaInfoRepository.getMetaInfo(v4, NameInfo.class).getName(), equalTo("V4"));
        assertThat(metaInfoRepository.getMetaInfo(v5, NameInfo.class).getName(), equalTo("V5"));
    }

    private void testLinkCapacities() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v1_v2, CapacityInfo.class)).getCapacity(), equalTo(9.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v1_v3, CapacityInfo.class)).getCapacity(), equalTo(10.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v1_v4, CapacityInfo.class)).getCapacity(), equalTo(10.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v2_v5, CapacityInfo.class)).getCapacity(), equalTo(10.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v3_v2, CapacityInfo.class)).getCapacity(), equalTo(7.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v3_v5, CapacityInfo.class)).getCapacity(), equalTo(10.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v4_v3, CapacityInfo.class)).getCapacity(), equalTo(10.0));
        assertThat(((CapacityInfo<Double>) metaInfoRepository.getMetaInfo(v4_v5, CapacityInfo.class)).getCapacity(), equalTo(8.0));
    }

    @Test
    public void theAlgorithm_shouldRunTheCorrectNumberOfSteps() throws AlgorithmException {
        fullRun();

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCurrentStep(), equalTo(4));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, null, Double.POSITIVE_INFINITY, true)));
        assertThat(metaInfoRepository.getMetaInfo(v2, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 6, true)));
        assertThat(metaInfoRepository.getMetaInfo(v3, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 7, true)));
        assertThat(metaInfoRepository.getMetaInfo(v4, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v3, 7, false)));
        assertThat(metaInfoRepository.getMetaInfo(v5, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v3, 4, false)));

        assertThat(metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(3.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(7.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(7.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(0.0)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, null, Double.POSITIVE_INFINITY, true)));
        assertThat(metaInfoRepository.getMetaInfo(v2, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 6, true)));
        assertThat(metaInfoRepository.getMetaInfo(v3, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 3, true)));
        assertThat(metaInfoRepository.getMetaInfo(v4, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v3, 3, true)));
        assertThat(metaInfoRepository.getMetaInfo(v5, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v4, 3, false)));

        assertThat(metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(3.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(7.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(7.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(3.0)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, null, Double.POSITIVE_INFINITY, true)));
        assertThat(metaInfoRepository.getMetaInfo(v2, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 6, true)));
        assertThat(metaInfoRepository.getMetaInfo(v3, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v2, 6, true)));
        assertThat(metaInfoRepository.getMetaInfo(v4, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v3, 6, true)));
        assertThat(metaInfoRepository.getMetaInfo(v5, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v4, 5, false)));

        assertThat(metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(8.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(2.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(2.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(8.0)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, null, Double.POSITIVE_INFINITY, true)));
        assertThat(metaInfoRepository.getMetaInfo(v2, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, true, v1, 1, true)));
        assertThat(metaInfoRepository.getMetaInfo(v3, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v2, 1, true)));
        assertThat(metaInfoRepository.getMetaInfo(v4, FordFulkersonVertexInfo.class), equalTo((FordFulkersonVertexInfo) new DefaultFordFulkersonVertexInfo(true, false, v3, 1, true)));

        assertThat(metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(8.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v2, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(2.0)));
        assertThat(metaInfoRepository.getMetaInfo(v3_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(10.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v3, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(2.0)));
        assertThat(metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class), equalTo((WeightInfo) new DefaultWeightInfo<>(8.0)));
    }

    @Test
    public void v5_shouldNotBeLabeledAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        FordFulkersonVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(v5, FordFulkersonVertexInfo.class);
        assertThat(vertexInfo.isLabeled(), equalTo(false));
    }

    @Test
    public void getDeltaStar_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getDeltaStar(), equalTo(4.0));
    }

    @Test
    public void getDeltaStar_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getDeltaStar(), equalTo(3.0));
    }

    @Test
    public void getDeltaStar_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getDeltaStar(), equalTo(5.0));
    }

    @Test
    public void getDeltaStar_shouldBeNullAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getDeltaStar(), nullValue());
    }

    @Test
    public void getAugmentingPath_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getAugmentingPath(), contains(v1_v3, v3_v5));
    }

    @Test
    public void getAugmentingPath_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getAugmentingPath(), contains(v1_v3, v4_v3, v4_v5));
    }

    @Test
    public void getAugmentingPath_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getAugmentingPath(), contains(v1_v2, v3_v2, v4_v3, v4_v5));
    }

    @Test
    public void getAugmentingPath_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getAugmentingPath(), containsInAnyOrder(v2_v5, v3_v5, v4_v5));
    }

    @Test
    public void getMaxFlow_shouldBeNullAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getMaxFlow(), nullValue());
    }

    @Test
    public void getMaxFlow_shouldBeNullAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getMaxFlow(), nullValue());
    }

    @Test
    public void getMaxFlow_shouldBeNullAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getMaxFlow(), nullValue());
    }

    @Test
    public void getMaxFlow_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getMaxFlow(), equalTo(28.0));
    }

    @Test
    public void getV1Group_shouldBeNullAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV1Group_shouldBeNullAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV1Group_shouldBeNullAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV1Group_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), containsInAnyOrder(v1, v2, v3, v4));
    }

    @Test
    public void getV2Group_shouldBeNullAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV2Group_shouldBeNullAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV2Group_shouldBeNullAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV1Group(), nullValue());
    }

    @Test
    public void getV2Group_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getV2Group(), contains(v5));
    }

}
