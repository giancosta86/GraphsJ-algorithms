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

package info.gianlucacosta.graphsj3.algorithms.sst;

import info.gianlucacosta.arcontes.algorithms.AlgorithmException;
import info.gianlucacosta.arcontes.algorithms.AlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.ScriptedAlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.test.CommonAlgorithmTest;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultNameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultWeightInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.graphsj3.algorithms.sst.metainfo.DefaultSstVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.sst.metainfo.SstVertexInfo;
import info.gianlucacosta.helios.metainfo.DefaultMetaInfoRepository;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SstTest extends CommonAlgorithmTest<Sst> {

    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;
    private Vertex v5;
    private Vertex v6;
    private Vertex v7;
    private Link v1_v2;
    private Link v1_v3;
    private Link v2_v3;
    private Link v2_v4;
    private Link v2_v5;
    private Link v3_v4;
    private Link v4_v5;
    private Link v4_v7;
    private Link v5_v6;
    private Link v5_v7;
    private Link v6_v7;

    @Override
    protected GraphContext createGraphContext() {
        Graph graph = new DefaultGraph();

        v1 = new DefaultVertex();
        v2 = new DefaultVertex();
        v3 = new DefaultVertex();
        v4 = new DefaultVertex();
        v5 = new DefaultVertex();
        v6 = new DefaultVertex();
        v7 = new DefaultVertex();

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        v1_v2 = new DefaultLink(v1, v2);
        v1_v3 = new DefaultLink(v1, v3);

        v2_v3 = new DefaultLink(v2, v3);
        v2_v4 = new DefaultLink(v2, v4);
        v2_v5 = new DefaultLink(v2, v5);

        v3_v4 = new DefaultLink(v3, v4);

        v4_v5 = new DefaultLink(v4, v5);
        v4_v7 = new DefaultLink(v4, v7);

        v5_v6 = new DefaultLink(v5, v6);
        v5_v7 = new DefaultLink(v5, v7);

        v6_v7 = new DefaultLink(v6, v7);

        graph.addLink(v1_v2);
        graph.addLink(v1_v3);

        graph.addLink(v2_v3);
        graph.addLink(v2_v4);
        graph.addLink(v2_v5);

        graph.addLink(v3_v4);

        graph.addLink(v4_v5);
        graph.addLink(v4_v7);

        graph.addLink(v5_v6);
        graph.addLink(v5_v7);

        graph.addLink(v6_v7);

        MetaInfoRepository metaInfoRepository = new DefaultMetaInfoRepository();

        metaInfoRepository.putMetaInfo(v1, new DefaultNameInfo("V1"));
        metaInfoRepository.putMetaInfo(v2, new DefaultNameInfo("V2"));
        metaInfoRepository.putMetaInfo(v3, new DefaultNameInfo("V3"));
        metaInfoRepository.putMetaInfo(v4, new DefaultNameInfo("V4"));
        metaInfoRepository.putMetaInfo(v5, new DefaultNameInfo("V5"));
        metaInfoRepository.putMetaInfo(v6, new DefaultNameInfo("V6"));
        metaInfoRepository.putMetaInfo(v7, new DefaultNameInfo("V7"));

        metaInfoRepository.putMetaInfo(v1_v2, new DefaultWeightInfo<>(3.0));
        metaInfoRepository.putMetaInfo(v1_v3, new DefaultWeightInfo<>(2.0));

        metaInfoRepository.putMetaInfo(v2_v3, new DefaultWeightInfo<>(4.0));
        metaInfoRepository.putMetaInfo(v2_v4, new DefaultWeightInfo<>(3.0));
        metaInfoRepository.putMetaInfo(v2_v5, new DefaultWeightInfo<>(1.0));

        metaInfoRepository.putMetaInfo(v3_v4, new DefaultWeightInfo<>(5.0));

        metaInfoRepository.putMetaInfo(v4_v5, new DefaultWeightInfo<>(1.0));
        metaInfoRepository.putMetaInfo(v4_v7, new DefaultWeightInfo<>(2.0));

        metaInfoRepository.putMetaInfo(v5_v6, new DefaultWeightInfo<>(4.0));
        metaInfoRepository.putMetaInfo(v5_v7, new DefaultWeightInfo<>(4.0));

        metaInfoRepository.putMetaInfo(v6_v7, new DefaultWeightInfo<>(4.0));

        return new DefaultGraphContext(graph, metaInfoRepository);
    }

    @Override
    protected AlgorithmInput createAlgorithmInput() {
        return new ScriptedAlgorithmInput() {
            @Override
            protected Queue<Vertex> initInputVertexes() {
                LinkedList<Vertex> result = new LinkedList<>();

                result.add(v1);

                return result;
            }

        };
    }

    @Override
    protected Sst createAlgorithm() {
        return new Sst(getGraphContext(), getAlgorithmSettings(), getAlgorithmInput(), getAlgorithmOutput());
    }

    private void testStructuralMetaInfo() {
        testVertexNames();
        testLinkWeights();
    }

    private void testVertexNames() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, NameInfo.class).getName(), equalTo("V1"));
        assertThat(metaInfoRepository.getMetaInfo(v2, NameInfo.class).getName(), equalTo("V2"));
        assertThat(metaInfoRepository.getMetaInfo(v3, NameInfo.class).getName(), equalTo("V3"));
        assertThat(metaInfoRepository.getMetaInfo(v4, NameInfo.class).getName(), equalTo("V4"));
        assertThat(metaInfoRepository.getMetaInfo(v5, NameInfo.class).getName(), equalTo("V5"));
        assertThat(metaInfoRepository.getMetaInfo(v6, NameInfo.class).getName(), equalTo("V6"));
        assertThat(metaInfoRepository.getMetaInfo(v7, NameInfo.class).getName(), equalTo("V7"));
    }

    private void testLinkWeights() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class)).getWeight(), equalTo(3.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class)).getWeight(), equalTo(2.0));

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v3, WeightInfo.class)).getWeight(), equalTo(4.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v4, WeightInfo.class)).getWeight(), equalTo(3.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class)).getWeight(), equalTo(1.0));

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v4, WeightInfo.class)).getWeight(), equalTo(5.0));

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v4_v7, WeightInfo.class)).getWeight(), equalTo(2.0));

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v5_v6, WeightInfo.class)).getWeight(), equalTo(4.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v5_v7, WeightInfo.class)).getWeight(), equalTo(4.0));

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v6_v7, WeightInfo.class)).getWeight(), equalTo(4.0));
    }

    @Test
    public void theAlgorithm_shouldRunTheCorrectNumberOfSteps() throws AlgorithmException {
        fullRun();

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getCurrentStep(), equalTo(6));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v3, 5)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v2, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v2, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, Double.POSITIVE_INFINITY)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v2, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 4)));

    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep5() throws AlgorithmException {
        runSteps(5);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v2, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v4, 2)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep6() throws AlgorithmException {
        runSteps(6);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 3)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v1, 2)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v2, 1)));
        assertThat(metaInfoRepository.getMetaInfo(v6, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v5, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v7, SstVertexInfo.class), equalTo((SstVertexInfo) new DefaultSstVertexInfo(v4, 2)));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(2.0));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(5.0));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(6.0));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(7.0));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep5() throws AlgorithmException {
        runSteps(5);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(9.0));
    }

    @Test
    public void getTreeWeight_shouldBeCorrectAfterStep6() throws AlgorithmException {
        runSteps(6);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeWeight(), equalTo(13.0));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v3));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v2, v1_v3));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v2, v1_v3, v2_v5));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v2, v1_v3, v2_v5, v4_v5));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep5() throws AlgorithmException {
        runSteps(5);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v2, v1_v3, v2_v5, v4_v5, v4_v7));
    }

    @Test
    public void getTreeEdges_shouldBeCorrectAfterStep6() throws AlgorithmException {
        runSteps(6);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getTreeEdges(), containsInAnyOrder(v1_v2, v1_v3, v2_v5, v4_v5, v4_v7, v5_v6));
    }

}
