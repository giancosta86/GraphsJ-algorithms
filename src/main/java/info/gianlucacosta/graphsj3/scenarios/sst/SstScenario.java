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

package info.gianlucacosta.graphsj3.scenarios.sst;

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.fx.canvas.DefaultGraphCanvas;
import info.gianlucacosta.arcontes.fx.canvas.GraphCanvas;
import info.gianlucacosta.arcontes.fx.rendering.DefaultGraphRenderer;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.wrappers.LinkWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.VertexWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.linkwrappers.NameBasedEdgeWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.vertexwrappers.NameBasedVertexWrapper;
import info.gianlucacosta.graphsj3.algorithms.sst.Sst;
import info.gianlucacosta.graphsj3.algorithms.sst.metainfo.SstRuntimeVertexLabelInfo;
import info.gianlucacosta.graphsj3.scenarios.common.CommonScenario;
import info.gianlucacosta.graphsj3.scenarios.common.ScenarioSolution;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.util.Collection;
import java.util.Collections;

/**
 * SST scenario
 */
public class SstScenario extends CommonScenario {

    public SstScenario() {
        super(false);
    }

    @Override
    public CommonAlgorithm doCreateAlgorithm(GraphContext graphContext, CommonAlgorithmSettings algorithmSettings, AlgorithmInput algorithmInput, AlgorithmOutput algorithmOutput) {
        return new Sst(graphContext, algorithmSettings, algorithmInput, algorithmOutput);
    }

    @Override
    public GraphCanvas createGraphCanvas(final GraphContext graphContext) {

        return new DefaultGraphCanvas(
                new DefaultGraphRenderer(),
                new SstCanvasAgentsFactory(getSettings(), getInputService()));
    }

    @Override
    public void initRuntimeGraphContext(GraphContext runtimeGraphContext) {
        Graph graph = runtimeGraphContext.getGraph();
        MetaInfoRepository metaInfoRepository = runtimeGraphContext.getMetaInfoRepository();

        for (Vertex vertex : graph.getVertexes()) {
            metaInfoRepository.putMetaInfo(vertex, new SstRuntimeVertexLabelInfo(runtimeGraphContext.getMetaInfoRepository(), vertex, getSettings().getAlgorithmSettings()));
        }
    }

    @Override
    public VertexWrapper createInputVertexWrapper(GraphContext graphContext, Vertex vertex) {
        return new NameBasedVertexWrapper(vertex, graphContext.getMetaInfoRepository());
    }

    @Override
    public LinkWrapper createInputLinkWrapper(GraphContext graphContext, Link link) {
        return new NameBasedEdgeWrapper(link, graphContext.getMetaInfoRepository());
    }

    @Override
    protected ScenarioSolution getPartialSolution(final Algorithm algorithm, int step) {
        return new ScenarioSolution() {
            @Override
            public Collection<Vertex> getVertexes() {
                return Collections.emptySet();
            }

            @Override
            public Collection<Link> getLinks() {
                Sst sst = (Sst) algorithm;
                return sst.getTreeEdges();
            }

        };
    }

    @Override
    protected ScenarioSolution getCompleteSolution(final Algorithm algorithm) {
        return new ScenarioSolution() {
            @Override
            public Collection<Vertex> getVertexes() {
                return Collections.emptySet();
            }

            @Override
            public Collection<Link> getLinks() {
                Sst sst = (Sst) algorithm;
                return sst.getTreeEdges();
            }

        };
    }

}
