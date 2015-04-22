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

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.analysis.DefaultOrientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.analysis.OrientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.conversions.linkconverters.LinksToNameBasedArcWrappersConverter;
import info.gianlucacosta.arcontes.graphs.conversions.vertexconverters.VertexesToNameBasedVertexWrappersConverter;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.graphsj3.algorithms.spp.metainfo.DefaultSppVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.spp.metainfo.SppVertexInfo;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SPP algorithm
 */
public class Spp extends CommonAlgorithm {

    private final List<Vertex> pathVertexes;
    private final List<Link> pathLinks;
    private final VertexesToNameBasedVertexWrappersConverter vertexesToNameBasedVertexWrappersConverter;
    private final LinksToNameBasedArcWrappersConverter linksToNameBasedArcWrappersConverter;
    private final OrientedGraphAnalyzer graphAnalyzer;
    private List<Vertex> vList;
    private Vertex vBar;

    public Spp(GraphContext graphContext, CommonAlgorithmSettings algorithmSettings, AlgorithmInput algorithmInput, AlgorithmOutput algorithmOutput) {
        super(graphContext, algorithmSettings, algorithmInput, algorithmOutput);

        vertexesToNameBasedVertexWrappersConverter = new VertexesToNameBasedVertexWrappersConverter(graphContext.getMetaInfoRepository());
        linksToNameBasedArcWrappersConverter = new LinksToNameBasedArcWrappersConverter(graphContext.getMetaInfoRepository());

        graphAnalyzer = new DefaultOrientedGraphAnalyzer(graphContext);

        pathVertexes = new ArrayList<>();
        pathLinks = new ArrayList<>();
    }

    @Override
    public boolean doInit() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmInput algorithmInput = getAlgorithmInput();
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();
        CommonAlgorithmSettings algorithmSettings = getAlgorithmSettings();

        if (!graphAnalyzer.getUnconnectedVertexes().isEmpty()) {
            throw new AlgorithmException("All the vertexes in the graph must be connected to a link!");
        }

        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        vList = new ArrayList<>(graph.getVertexes());

        Vertex startVertex = algorithmInput.askForVertex(graph, "Choose start vertex:");
        if (startVertex == null) {
            return false;
        }

        pathVertexes.add(startVertex);
        vList.remove(startVertex);

        //Initializing the vertex labels
        metaInfoRepository.putMetaInfo(startVertex, new DefaultSppVertexInfo(null, 0));

        for (Vertex vertex : vList) {
            metaInfoRepository.putMetaInfo(vertex, new DefaultSppVertexInfo(startVertex, graphAnalyzer.getMinWeightBetween(startVertex, vertex)));
        }

        if (algorithmSettings.isVerbose()) {

            algorithmOutput.printHeader("Legend");
            algorithmOutput.println();
            algorithmOutput.println("Vbar", "Vertex added to the shortest path in the current step");
            algorithmOutput.println();
            algorithmOutput.printHeader("Before step 1");
            algorithmOutput.println();
            algorithmOutput.println("Path vertexes", vertexesToNameBasedVertexWrappersConverter.convert(pathVertexes));
            algorithmOutput.println("Path arcs", linksToNameBasedArcWrappersConverter.convert(pathLinks));
            algorithmOutput.println();
        }

        return true;
    }

    @Override
    protected AlgorithmStepOutcome doRunStep() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();
        CommonAlgorithmSettings algorithmSettings = getAlgorithmSettings();

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.printHeader("Step " + getCurrentStep());
        }

        if (getCurrentStep() > 1) {
            SppVertexInfo vBarVertexInfo = metaInfoRepository.getMetaInfo(vBar, SppVertexInfo.class);

            for (Vertex vertex : vList) {
                SppVertexInfo currentVertexInfo = metaInfoRepository.getMetaInfo(vertex, SppVertexInfo.class);

                double vertexPathLengthFromStart = vBarVertexInfo.getPathLength() + graphAnalyzer.getMinWeightBetween(vBar, vertex);

                if (vertexPathLengthFromStart < currentVertexInfo.getPathLength()) {
                    metaInfoRepository.putMetaInfo(vertex, new DefaultSppVertexInfo(vBar, vertexPathLengthFromStart));
                }
            }
        }

        double minPathLength = Double.POSITIVE_INFINITY;

        //Finding out the "vBar" vertex...
        for (Vertex vertex : vList) {
            SppVertexInfo currentVertexInfo = metaInfoRepository.getMetaInfo(vertex, SppVertexInfo.class);

            if (currentVertexInfo.getPathLength() < minPathLength) {
                vBar = vertex;
                minPathLength = currentVertexInfo.getPathLength();
            }
        }

        if (vBar == null) {
            throw new AlgorithmException("Cannot determine Vbar. Algorithm error");
        }

        //Updating the sets...
        pathVertexes.add(vBar);
        vList.remove(vBar);

        SppVertexInfo vBarVertexInfo = metaInfoRepository.getMetaInfo(vBar, SppVertexInfo.class);

        Link arcToAdd = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(vBarVertexInfo.getPreviousVertex(), vBar));

        if (arcToAdd == null) {
            throw new AlgorithmException("Cannot determine the arc to add");
        }

        pathLinks.add(arcToAdd);

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.println();
            algorithmOutput.println("At the end of the step:");
            algorithmOutput.println();
            algorithmOutput.println("Vbar", metaInfoRepository.getMetaInfo(vBar, NameInfo.class).getName());
            algorithmOutput.println("Path vertexes", vertexesToNameBasedVertexWrappersConverter.convert(pathVertexes));
            algorithmOutput.println("Path edges", linksToNameBasedArcWrappersConverter.convert(pathLinks));
            algorithmOutput.println();
        }

        if (vList.isEmpty()) {
            return AlgorithmStepOutcome.FINISH;
        }

        return AlgorithmStepOutcome.CONTINUE;
    }

    @Override
    protected void doFinish() throws AlgorithmException {
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();

        algorithmOutput.println("The edges used by the shortest paths are: " + linksToNameBasedArcWrappersConverter.convert(pathLinks));
    }

    public Collection<Vertex> getPathVertexes() {
        return Collections.unmodifiableCollection(pathVertexes);
    }

    public Collection<Link> getPathLinks() {
        return Collections.unmodifiableCollection(pathLinks);
    }

}
