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

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.analysis.DefaultOrientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.analysis.OrientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.conversions.linkconverters.LinksToHeadsConverter;
import info.gianlucacosta.arcontes.graphs.conversions.linkconverters.LinksToNameBasedArcWrappersConverter;
import info.gianlucacosta.arcontes.graphs.conversions.linkconverters.LinksToTailsConverter;
import info.gianlucacosta.arcontes.graphs.conversions.vertexconverters.VertexesToNameBasedVertexWrappersConverter;
import info.gianlucacosta.arcontes.graphs.metainfo.*;
import info.gianlucacosta.arcontes.graphs.wrappers.LinkWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.linkwrappers.NameBasedArcWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.vertexwrappers.NameBasedVertexWrapper;
import info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo.DefaultFordFulkersonVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo.FordFulkersonVertexInfo;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Ford-Fulkerson's flow algorithm
 */
public class FordFulkerson extends CommonAlgorithm {

    private Vertex sourceVertex;
    private Vertex sinkVertex;
    private List<Link> augmentingPath;
    private Double deltaStar;
    private Double maxFlow;
    private Collection<Vertex> v1Group;
    private Collection<Vertex> v2Group;
    private final OrientedGraphAnalyzer graphAnalyzer;
    private final VertexesToNameBasedVertexWrappersConverter vertexesToNameBasedVertexWrappersConverter;
    private final LinksToNameBasedArcWrappersConverter linksToNameBasedArcWrappersConverter;
    private final LinksToHeadsConverter linksToHeadsConverter;
    private final LinksToTailsConverter linksToTailsConverter;

    public FordFulkerson(GraphContext graphContext, CommonAlgorithmSettings algorithmSettings, AlgorithmInput algorithmInput, AlgorithmOutput algorithmOutput) {
        super(graphContext, algorithmSettings, algorithmInput, algorithmOutput);

        graphAnalyzer = new DefaultOrientedGraphAnalyzer(graphContext);

        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();
        vertexesToNameBasedVertexWrappersConverter = new VertexesToNameBasedVertexWrappersConverter(metaInfoRepository);
        linksToNameBasedArcWrappersConverter = new LinksToNameBasedArcWrappersConverter(metaInfoRepository);

        linksToHeadsConverter = new LinksToHeadsConverter(graphAnalyzer);
        linksToTailsConverter = new LinksToTailsConverter(graphAnalyzer);

        augmentingPath = new ArrayList<>();
    }

    @Override
    public boolean doInit() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmInput algorithmInput = getAlgorithmInput();

        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        if (!graphAnalyzer.getUnconnectedVertexes().isEmpty()) {
            throw new AlgorithmException("All the vertexes in the graph must be connected to a link!");
        }

        sourceVertex = algorithmInput.askForVertex(graph, "Choose the source vertex:");
        if (sourceVertex == null) {
            return false;
        }

        if (!graphAnalyzer.getEnteringArcs(sourceVertex).isEmpty()) {
            throw new AlgorithmException("The source vertex cannot have incoming arcs");
        }

        sinkVertex = algorithmInput.askForVertex(graph, "Choose the sink vertex:");
        if (sinkVertex == null) {
            return false;
        }

        if (!graphAnalyzer.getExitingArcs(sinkVertex).isEmpty()) {
            throw new AlgorithmException("The sink vertex cannot have outgoing arcs");
        }

        if (sourceVertex.equals(sinkVertex)) {
            throw new AlgorithmException("The source vertex and the sink vertex cannot be the same!");
        }

        for (Vertex vertex : graph.getVertexes()) {
            metaInfoRepository.putMetaInfo(vertex, new DefaultFordFulkersonVertexInfo());
        }

        return true;
    }

    @Override
    protected AlgorithmStepOutcome doRunStep() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();
        CommonAlgorithmSettings algorithmSettings = getAlgorithmSettings();

        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        int currentStep = getCurrentStep();

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.println();
            algorithmOutput.printHeader("Step " + currentStep);
            algorithmOutput.println();
        }

        //--------------
        //FIRST SUB-STEP
        //--------------
        deltaStar = null;

        for (Vertex vertex : graph.getVertexes()) {
            DefaultFordFulkersonVertexInfo vertexInfo = new DefaultFordFulkersonVertexInfo(
                    metaInfoRepository.getMetaInfo(vertex, FordFulkersonVertexInfo.class));

            vertexInfo.setLabeled(false);
            vertexInfo.setExplored(false);

            metaInfoRepository.putMetaInfo(vertex, vertexInfo);
        }

        metaInfoRepository.putMetaInfo(sourceVertex, new DefaultFordFulkersonVertexInfo(true, true, null, Double.POSITIVE_INFINITY, false));

        //The internal iterations begin here
        for (int intraIndex = 1; intraIndex <= graph.getVertexes().size(); intraIndex++) {

            Vertex vi = null;

            Collection<NameBasedVertexWrapper> vertexWrappers = vertexesToNameBasedVertexWrappersConverter.convert(graph.getVertexes());

            //Here, I get the first vertex available, and set it as "Vi"
            for (NameBasedVertexWrapper vertexWrapper : vertexWrappers) {

                Vertex vertex = vertexWrapper.getVertex();

                FordFulkersonVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(vertex, FordFulkersonVertexInfo.class);

                if (vertexInfo.isLabeled() && !vertexInfo.isExplored()) {
                    vi = vertex;
                    break;
                }
            }

            if (vi == null) {
                break;
            }

            DefaultFordFulkersonVertexInfo viInfo
                    = new DefaultFordFulkersonVertexInfo(
                    metaInfoRepository.getMetaInfo(vi, FordFulkersonVertexInfo.class));
            viInfo.setExplored(true);
            metaInfoRepository.putMetaInfo(vi, viInfo);

            Collection<Vertex> tailsExitingFromVi = linksToTailsConverter.convert(graphAnalyzer.getExitingArcs(vi));

            //Here, I set the label for the vertexes belonging to the "gamma+" of Vi
            for (Vertex vj : tailsExitingFromVi) {
                DefaultFordFulkersonVertexInfo vjVertexInfo = new DefaultFordFulkersonVertexInfo(
                        metaInfoRepository.getMetaInfo(vj, FordFulkersonVertexInfo.class));

                if (vjVertexInfo.isLabeled()) {
                    continue;
                }

                Link linkIJ = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(vi, vj));
                WeightInfo<Double> ijWeightInfo = metaInfoRepository.getMetaInfo(linkIJ, WeightInfo.class);
                CapacityInfo<Double> ijCapacityInfo = metaInfoRepository.getMetaInfo(linkIJ, CapacityInfo.class);

                if (ijWeightInfo.getWeight() < ijCapacityInfo.getCapacity()) {
                    vjVertexInfo.setLabeled(true);
                    vjVertexInfo.setPlusVk(true);
                    vjVertexInfo.setVk(vi);

                    vjVertexInfo.setDelta(Math.min(
                            viInfo.getDelta(),
                            ijCapacityInfo.getCapacity() - ijWeightInfo.getWeight()));

                    metaInfoRepository.putMetaInfo(vj, vjVertexInfo);
                }
            }

            Collection<Vertex> headsEnteringIntoVi = linksToHeadsConverter.convert(graphAnalyzer.getEnteringArcs(vi));

            //Here, I set the label for the vertexes belonging to the "gamma-" of Vi
            for (Vertex vj : headsEnteringIntoVi) {
                DefaultFordFulkersonVertexInfo vjVertexInfo = new DefaultFordFulkersonVertexInfo(
                        metaInfoRepository.getMetaInfo(vj, FordFulkersonVertexInfo.class));

                if (vjVertexInfo.isLabeled()) {
                    continue;
                }

                Link linkJI = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(vj, vi));
                WeightInfo<Double> jiWeightInfo = metaInfoRepository.getMetaInfo(linkJI, WeightInfo.class);

                if (jiWeightInfo.getWeight() > 0) {
                    vjVertexInfo.setLabeled(true);
                    vjVertexInfo.setPlusVk(false);
                    vjVertexInfo.setVk(vi);
                    vjVertexInfo.setDelta(Math.min(
                            viInfo.getDelta(),
                            jiWeightInfo.getWeight()));

                    metaInfoRepository.putMetaInfo(vj, vjVertexInfo);
                }
            }

            FordFulkersonVertexInfo sinkVertexInfo = metaInfoRepository.getMetaInfo(sinkVertex, FordFulkersonVertexInfo.class);
            if (sinkVertexInfo.isLabeled()) {
                break;
            }

        }

        //The internal iteration has just ended: I can show the resulting arc weights now, as well as the labels
        if (algorithmSettings.isVerbose()) {
            algorithmOutput.println("Arcs at the end of the step: ");

            Collection<NameBasedArcWrapper> arcWrappers = linksToNameBasedArcWrappersConverter.convert(graph.getLinks());

            for (LinkWrapper arcWrapper : arcWrappers) {
                Link link = arcWrapper.getLink();

                String linkLabel = metaInfoRepository.getMetaInfo(link, LabelInfo.class).getLabel();

                algorithmOutput.println(String.format("%s -- %s", arcWrapper, linkLabel));
            }

            algorithmOutput.println();
            algorithmOutput.println();
        }

        FordFulkersonVertexInfo sinkVertexInfo = metaInfoRepository.getMetaInfo(sinkVertex, FordFulkersonVertexInfo.class);
        if (!sinkVertexInfo.isLabeled()) {
            return AlgorithmStepOutcome.FINISH;
        }

        //---------------
        //SECOND SUB-STEP
        //---------------
        //Now I set up the augmenting path
        augmentingPath = new ArrayList<>();

        Vertex x = sinkVertex;

        //I update link flows now, by using the "delta*"
        while (!x.equals(sourceVertex)) {
            FordFulkersonVertexInfo xVertexInfo = metaInfoRepository.getMetaInfo(x, FordFulkersonVertexInfo.class);

            Vertex y = xVertexInfo.getVk();

            Link augmentingLink;
            if (xVertexInfo.isPlusVk()) {
                augmentingLink = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(y, x));
            } else {
                augmentingLink = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(x, y));
            }

            x = y;
            augmentingPath.add(augmentingLink);
        }

        if (algorithmSettings.isVerbose()) {
            Collections.reverse(augmentingPath);
            algorithmOutput.println("Augmenting path", linksToNameBasedArcWrappersConverter.convert(augmentingPath));
            algorithmOutput.println();
        }

        //--------------
        //THIRD SUB-STEP
        //--------------
        sinkVertexInfo = metaInfoRepository.getMetaInfo(sinkVertex, FordFulkersonVertexInfo.class);

        //Here, I'll get the "delta*" variable for this iteration
        deltaStar = sinkVertexInfo.getDelta();

        x = sinkVertex;

        if (algorithmSettings.isVerbose()) {
            algorithmOutput.println("Delta*", deltaStar);
        }

        //Updating link flows now, by using "delta*"
        while (!x.equals(sourceVertex)) {
            FordFulkersonVertexInfo xVertexInfo = metaInfoRepository.getMetaInfo(x, FordFulkersonVertexInfo.class);

            Vertex y = xVertexInfo.getVk();

            Link augmentingLink;

            if (xVertexInfo.isPlusVk()) {
                augmentingLink = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(y, x));

                WeightInfo<Double> weightInfo = metaInfoRepository.getMetaInfo(augmentingLink, WeightInfo.class);
                metaInfoRepository.putMetaInfo(augmentingLink, new DefaultWeightInfo<>(weightInfo.getWeight() + deltaStar));
            } else {
                augmentingLink = CollectionItems.getFirst(graphAnalyzer.getArcsWithMinWeightBetween(x, y));
                WeightInfo<Double> weightInfo = metaInfoRepository.getMetaInfo(augmentingLink, WeightInfo.class);
                metaInfoRepository.putMetaInfo(augmentingLink, new DefaultWeightInfo<>(weightInfo.getWeight() - deltaStar));
            }

            x = y;
        }

        return AlgorithmStepOutcome.CONTINUE;
    }

    @Override
    protected void doFinish() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();

        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        //I can get the resulting sets of vertexes
        v1Group = new ArrayList<>();
        v2Group = new ArrayList<>();

        for (Vertex vertex : graph.getVertexes()) {
            FordFulkersonVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(vertex, FordFulkersonVertexInfo.class);

            if (vertexInfo.isLabeled()) {
                v1Group.add(vertex);
            } else {
                v2Group.add(vertex);
            }
        }

        //Determining the maximum flow value
        maxFlow = 0.0;

        augmentingPath = new ArrayList<>();
        for (Link link : graph.getLinks()) {
            if (v1Group.contains(graphAnalyzer.getHead(link)) && v2Group.contains(graphAnalyzer.getTail(link))) {
                CapacityInfo<Double> capacityInfo = metaInfoRepository.getMetaInfo(link, CapacityInfo.class);

                maxFlow += capacityInfo.getCapacity();
                augmentingPath.add(link);
            }
        }

        //Printing out the results
        algorithmOutput.println("V1", vertexesToNameBasedVertexWrappersConverter.convert(v1Group));
        algorithmOutput.println("V2", vertexesToNameBasedVertexWrappersConverter.convert(v2Group));

        algorithmOutput.println();

        String sourceName = metaInfoRepository.getMetaInfo(sourceVertex, NameInfo.class).getName();
        String sinkName = metaInfoRepository.getMetaInfo(sinkVertex, NameInfo.class).getName();

        algorithmOutput.println(String.format("Maximum flow from '%s' to '%s'", sourceName, sinkName), maxFlow);
    }

    public Vertex getStartVertex() {
        return sourceVertex;
    }

    public Vertex getStopVertex() {
        return sinkVertex;
    }

    public Collection<Link> getAugmentingPath() {
        if (augmentingPath == null) {
            return null;
        }

        return Collections.unmodifiableCollection(augmentingPath);
    }

    public Double getDeltaStar() {
        return deltaStar;
    }

    public Double getMaxFlow() {
        return maxFlow;
    }

    public Collection<Vertex> getV1Group() {
        if (v1Group == null) {
            return null;
        }

        return Collections.unmodifiableCollection(v1Group);
    }

    public Collection<Vertex> getV2Group() {
        if (v2Group == null) {
            return null;
        }

        return Collections.unmodifiableCollection(v2Group);
    }

}
