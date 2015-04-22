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

package info.gianlucacosta.graphsj3.algorithms.spp;

import info.gianlucacosta.arcontes.algorithms.AlgorithmException;
import info.gianlucacosta.arcontes.algorithms.AlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.ScriptedAlgorithmInput;
import info.gianlucacosta.arcontes.algorithms.test.CommonAlgorithmTest;
import info.gianlucacosta.arcontes.graphs.*;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultNameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultWeightInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.graphsj3.algorithms.spp.metainfo.DefaultSppVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.spp.metainfo.SppVertexInfo;
import info.gianlucacosta.helios.metainfo.DefaultMetaInfoRepository;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SppTest extends CommonAlgorithmTest<Spp> {

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
    private Link v3_v4;
    private Link v3_v5;
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

        v3_v4 = new DefaultLink(v3, v4);
        graph.addLink(v3_v4);

        v3_v5 = new DefaultLink(v3, v5);
        graph.addLink(v3_v5);

        v4_v5 = new DefaultLink(v4, v5);
        graph.addLink(v4_v5);

        MetaInfoRepository metaInfoRepository = new DefaultMetaInfoRepository();

        metaInfoRepository.putMetaInfo(v1, new DefaultNameInfo("V1"));
        metaInfoRepository.putMetaInfo(v2, new DefaultNameInfo("V2"));
        metaInfoRepository.putMetaInfo(v3, new DefaultNameInfo("V3"));
        metaInfoRepository.putMetaInfo(v4, new DefaultNameInfo("V4"));
        metaInfoRepository.putMetaInfo(v5, new DefaultNameInfo("V5"));

        metaInfoRepository.putMetaInfo(v1_v2, new DefaultWeightInfo<>(9.0));
        metaInfoRepository.putMetaInfo(v1_v3, new DefaultWeightInfo<>(4.0));
        metaInfoRepository.putMetaInfo(v1_v4, new DefaultWeightInfo<>(8.0));
        metaInfoRepository.putMetaInfo(v2_v5, new DefaultWeightInfo<>(1.0));
        metaInfoRepository.putMetaInfo(v3_v2, new DefaultWeightInfo<>(4.0));
        metaInfoRepository.putMetaInfo(v3_v4, new DefaultWeightInfo<>(3.0));
        metaInfoRepository.putMetaInfo(v3_v5, new DefaultWeightInfo<>(7.0));
        metaInfoRepository.putMetaInfo(v4_v5, new DefaultWeightInfo<>(3.0));

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
    protected Spp createAlgorithm() {
        return new Spp(getGraphContext(), getAlgorithmSettings(), getAlgorithmInput(), getAlgorithmOutput());
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
    }

    private void testLinkWeights() {
        GraphContext graphContext = getGraphContext();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v2, WeightInfo.class)).getWeight(), equalTo(9.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v3, WeightInfo.class)).getWeight(), equalTo(4.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v1_v4, WeightInfo.class)).getWeight(), equalTo(8.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v2_v5, WeightInfo.class)).getWeight(), equalTo(1.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v2, WeightInfo.class)).getWeight(), equalTo(4.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v4, WeightInfo.class)).getWeight(), equalTo(3.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v3_v5, WeightInfo.class)).getWeight(), equalTo(7.0));
        assertThat(((WeightInfo<Double>) metaInfoRepository.getMetaInfo(v4_v5, WeightInfo.class)).getWeight(), equalTo(3.0));
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

        assertThat(metaInfoRepository.getMetaInfo(v1, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(null, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 9)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 8)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, Double.POSITIVE_INFINITY)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(null, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 8)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 7)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 11)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();
        assertThat(metaInfoRepository.getMetaInfo(v1, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(null, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 8)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 7)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v4, 10)));
    }

    @Test
    public void theGraph_shouldHaveCorrectMetaDataAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

        assertThat(metaInfoRepository.getMetaInfo(v1, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(null, 0)));
        assertThat(metaInfoRepository.getMetaInfo(v2, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 8)));
        assertThat(metaInfoRepository.getMetaInfo(v3, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v1, 4)));
        assertThat(metaInfoRepository.getMetaInfo(v4, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v3, 7)));
        assertThat(metaInfoRepository.getMetaInfo(v5, SppVertexInfo.class), equalTo((SppVertexInfo) new DefaultSppVertexInfo(v2, 9)));
    }

    @Test
    public void getPathVertexes_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathVertexes(), containsInAnyOrder(v1, v3));
    }

    @Test
    public void getPathVertexes_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathVertexes(), containsInAnyOrder(v1, v3, v4));
    }

    @Test
    public void getPathVertexes_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathVertexes(), containsInAnyOrder(v1, v3, v4, v2));
    }

    @Test
    public void getPathVertexes_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathVertexes(), containsInAnyOrder(v1, v3, v4, v2, v5));
    }

    @Test
    public void getPathLinks_shouldBeCorrectAfterStep1() throws AlgorithmException {
        runSteps(1);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathLinks(), containsInAnyOrder(v1_v3));
    }

    @Test
    public void getPathLinks_shouldBeCorrectAfterStep2() throws AlgorithmException {
        runSteps(2);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathLinks(), containsInAnyOrder(v1_v3, v3_v4));
    }

    @Test
    public void getPathLinks_shouldBeCorrectAfterStep3() throws AlgorithmException {
        runSteps(3);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathLinks(), containsInAnyOrder(v1_v3, v3_v4, v3_v2));
    }

    @Test
    public void getPathLinks_shouldBeCorrectAfterStep4() throws AlgorithmException {
        runSteps(4);

        testStructuralMetaInfo();

        assertThat(getAlgorithm().getPathLinks(), containsInAnyOrder(v1_v3, v3_v4, v3_v2, v2_v5));
    }

}
