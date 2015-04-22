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

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.analysis.DefaultUnorientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.analysis.UnorientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.conversions.linkconverters.LinksToNameBasedEdgeWrappersConverter;
import info.gianlucacosta.arcontes.graphs.conversions.vertexconverters.VertexesToNameBasedVertexWrappersConverter;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.graphsj3.algorithms.sst.metainfo.DefaultSstVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.sst.metainfo.SstVertexInfo;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SST algorithm
 */
public class Sst extends CommonAlgorithm {

    private final List<Vertex> vList;
    private final List<Vertex> wList;
    private final List<Link> treeEdges;
    private final UnorientedGraphAnalyzer graphAnalyzer;
    private final VertexesToNameBasedVertexWrappersConverter vertexesToNameBasedVertexWrappersConverter;
    private final LinksToNameBasedEdgeWrappersConverter linksToNameBasedEdgeWrappersConverter;
    private Vertex vBar;

    public Sst(GraphContext graphContext, CommonAlgorithmSettings algorithmSettings, AlgorithmInput algorithmInput, AlgorithmOutput algorithmOutput) {
        super(graphContext, algorithmSettings, algorithmInput, algorithmOutput);

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
        vertexesToNameBasedVertexWrappersConverter = new VertexesToNameBasedVertexWrappersConverter(metaInfoRepository);
        linksToNameBasedEdgeWrappersConverter = new LinksToNameBasedEdgeWrappersConverter(metaInfoRepository);

        graphAnalyzer = new DefaultUnorientedGraphAnalyzer(graphContext);

        treeEdges = new ArrayList<>();

        vList = new ArrayList<>(graphContext.getGraph().getVertexes());
        wList = new ArrayList<>();
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

        Vertex v1 = algorithmInput.askForVertex(graphContext.getGraph(), "Initial vertex:");
        if (v1 == null) {
            return false;
        }

        graphContext.getMetaInfoRepository().putMetaInfo(v1, new DefaultSstVertexInfo(v1, 0));

        wList.add(v1);
        vList.remove(v1);
        vBar = v1;

        for (Vertex vertex : vList) {
            double distance = graphAnalyzer.getMinWeightBetween(v1, vertex);

            graphContext.getMetaInfoRepository().putMetaInfo(vertex, new DefaultSstVertexInfo(v1, distance));
        }

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.printHeader("Legend");
            algorithmOutput.println();
            algorithmOutput.println("V", "The graph vertexes");
            algorithmOutput.println("W", "Vertexes belonging to the tree in the current step");
            algorithmOutput.println("E", "Edges belonging to the tree in the current step");
            algorithmOutput.println("Vbar", "Vertex added to the tree in the current step");
            algorithmOutput.println();

            algorithmOutput.printHeader("Before step 1");

            algorithmOutput.println("W", vertexesToNameBasedVertexWrappersConverter.convert(wList));
            algorithmOutput.println("V \\ W", vertexesToNameBasedVertexWrappersConverter.convert(vList));
            algorithmOutput.println("E", linksToNameBasedEdgeWrappersConverter.convert(treeEdges));
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
            algorithmOutput.println();
        }

        for (Vertex vertex : vList) {
            double distanceFromVBar = graphAnalyzer.getMinWeightBetween(vBar, vertex);

            SstVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(vertex, SstVertexInfo.class);
            if (distanceFromVBar < vertexInfo.getDistanceFromBestVertex()) {
                metaInfoRepository.putMetaInfo(vertex, new DefaultSstVertexInfo(vBar, distanceFromVBar));
            }
        }

        //Now, I'll determine the "vBar" vertex        
        double minBest = Double.POSITIVE_INFINITY;
        vBar = null;

        for (Vertex vertex : vList) {
            SstVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(vertex, SstVertexInfo.class);

            if (vertexInfo.getDistanceFromBestVertex() < minBest) {
                vBar = vertex;
                minBest = vertexInfo.getDistanceFromBestVertex();
            }
        }

        if (vBar == null) {
            throw new AlgorithmException("Could not determine vBar! Error in the algorithm!");
        }

        //Adding vBar to the "W" set, and removing it from the "V" set
        wList.add(vBar);
        vList.remove(vBar);

        //Also adding the corresponding link to E
        SstVertexInfo vBarInfo = metaInfoRepository.getMetaInfo(vBar, SstVertexInfo.class);
        Link chosenEdge = CollectionItems.getFirst(graphAnalyzer.getEdgesWithMinWeightBetween(vBar, vBarInfo.getBestVertex()));
        treeEdges.add(chosenEdge);

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.println("At the end of the step:");
            algorithmOutput.println();
            algorithmOutput.println("Vbar", metaInfoRepository.getMetaInfo(vBar, NameInfo.class).getName());
            algorithmOutput.println("W", vertexesToNameBasedVertexWrappersConverter.convert(wList));
            algorithmOutput.println("V \\ W", vertexesToNameBasedVertexWrappersConverter.convert(vList));
            algorithmOutput.println("E", linksToNameBasedEdgeWrappersConverter.convert(treeEdges));

            algorithmOutput.println();
            algorithmOutput.println();
        }

        if (vList.isEmpty()) {
            return AlgorithmStepOutcome.FINISH;
        } else {
            return AlgorithmStepOutcome.CONTINUE;
        }
    }

    @Override
    public void doFinish() throws AlgorithmException {
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();

        algorithmOutput.println(String.format("The branches of the spanning tree are: %s", linksToNameBasedEdgeWrappersConverter.convert(treeEdges)));
        algorithmOutput.println();
        algorithmOutput.println(String.format("The total weight of the spanning tree is: %s", getTreeWeight()));
    }

    public double getTreeWeight() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        double result = 0;

        for (Link edge : treeEdges) {
            WeightInfo<Double> weightMetaInfo = metaInfoRepository.getMetaInfo(edge, WeightInfo.class);

            Double edgeWeight = weightMetaInfo.getWeight();

            result += edgeWeight;
        }

        return result;
    }

    public Collection<Link> getTreeEdges() {
        return Collections.unmodifiableCollection(treeEdges);
    }

}
