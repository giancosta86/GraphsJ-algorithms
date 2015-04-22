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

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.conversions.vertexconverters.VertexesToNameBasedVertexWrappersConverter;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.arcontes.graphs.wrappers.vertexwrappers.NameBasedVertexWrapper;
import info.gianlucacosta.graphsj3.algorithms.cpm.metainfo.CpmVertexInfo;
import info.gianlucacosta.graphsj3.algorithms.cpm.metainfo.DefaultCpmVertexInfo;
import info.gianlucacosta.helios.collections.general.SortedCollection;
import info.gianlucacosta.helios.conversions.CollectionToCollectionConverter;
import info.gianlucacosta.helios.conversions.FilteringCollectionConverter;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import info.gianlucacosta.helios.predicates.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * CPM algorithm
 */
public class Cpm extends CommonAlgorithm {

    private final CpmGraphAnalyzer graphAnalyzer;
    private final List<Vertex> indexedVertexes;
    private final Collection<Activity> criticalActivities;
    private final VertexesToNameBasedVertexWrappersConverter vertexesToNameBasedVertexWrappersConverter;
    private final CollectionToCollectionConverter<Activity, Link> activitiesToLinksConverter;

    private Vertex startVertex;
    private Vertex stopVertex;

    public Cpm(GraphContext graphContext, CommonAlgorithmSettings algorithmSettings, AlgorithmInput algorithmInput, AlgorithmOutput algorithmOutput) {
        super(graphContext, algorithmSettings, algorithmInput, algorithmOutput);

        graphAnalyzer = new DefaultCpmGraphAnalyzer(graphContext);

        vertexesToNameBasedVertexWrappersConverter = new VertexesToNameBasedVertexWrappersConverter(graphContext.getMetaInfoRepository());

        indexedVertexes = new ArrayList<>();

        criticalActivities = new SortedCollection<>();

        activitiesToLinksConverter = new CollectionToCollectionConverter<Activity, Link>() {
            @Override
            protected Link convertItem(Activity activity) {
                return activity.getLink();
            }

        };
    }

    @Override
    public boolean doInit() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        AlgorithmInput algorithmInput = getAlgorithmInput();

        Graph graph = graphContext.getGraph();

        if (!graphAnalyzer.getUnconnectedVertexes().isEmpty()) {
            throw new AlgorithmException("All the vertexes in the graph must be connected to a link!");
        }

        startVertex = algorithmInput.askForVertex(graph, "Choose the start vertex:");
        if (startVertex == null) {
            return false;
        }

        if (!graphAnalyzer.getEnteringArcs(startVertex).isEmpty()) {
            throw new AlgorithmException("The start vertex cannot have incoming arcs");
        }

        stopVertex = algorithmInput.askForVertex(graph, "Choose the stop vertex:");
        if (stopVertex == null) {
            return false;
        }

        if (!graphAnalyzer.getExitingArcs(stopVertex).isEmpty()) {
            throw new AlgorithmException("The stop vertex cannot have outgoing arcs");
        }

        if (startVertex.equals(stopVertex)) {
            throw new AlgorithmException("The start vertex and the stop vertex cannot be the same!");
        }

        return true;
    }

    @Override
    protected AlgorithmStepOutcome doRunStep() throws AlgorithmException {
        switch (getCurrentStep()) {
            case 1:
                indexVertexes();
                break;

            case 2:
                calculateTMin();
                break;

            case 3:
                calculateTMax();
                return AlgorithmStepOutcome.FINISH;
        }

        return AlgorithmStepOutcome.CONTINUE;
    }

    private void indexVertexes() throws AlgorithmException {
        GraphContext graphContext = getGraphContext();
        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        final Collection<NameBasedVertexWrapper> vertexWrappersToIndex = vertexesToNameBasedVertexWrappersConverter.convert(graph.getVertexes());

        int vertexCollectionSize = graph.getVertexes().size();

        metaInfoRepository.putMetaInfo(startVertex, new DefaultCpmVertexInfo(0));
        indexedVertexes.add(startVertex);
        vertexWrappersToIndex.remove(new NameBasedVertexWrapper(startVertex, metaInfoRepository));

        metaInfoRepository.putMetaInfo(stopVertex, new DefaultCpmVertexInfo(vertexCollectionSize - 1));
        vertexWrappersToIndex.remove(new NameBasedVertexWrapper(stopVertex, metaInfoRepository));

        Predicate<Link> linkWithNonIndexedHeadPredicate = new Predicate<Link>() {
            @Override
            public boolean evaluate(Link link) {
                return !indexedVertexes.contains(graphAnalyzer.getHead(link));
            }

        };

        FilteringCollectionConverter<Link> enteringLinksFilter = new FilteringCollectionConverter<>(linkWithNonIndexedHeadPredicate);

        for (int index = 1; index <= graph.getVertexes().size() - 2; index++) {
            Vertex indexedVertex = null;

            for (NameBasedVertexWrapper vertexWrapper : new ArrayList<>(vertexWrappersToIndex)) {
                Vertex vertex = vertexWrapper.getVertex();
                Collection<Link> enteringLinksStartingFromNonIndexedVertexes = enteringLinksFilter.convert(graphAnalyzer.getEnteringArcs(vertex));

                if (enteringLinksStartingFromNonIndexedVertexes.isEmpty()) {
                    metaInfoRepository.putMetaInfo(vertex, new DefaultCpmVertexInfo(index));
                    indexedVertexes.add(vertex);
                    vertexWrappersToIndex.remove(vertexWrapper);
                    indexedVertex = vertex;
                    break;
                }
            }

            if (indexedVertex == null) {
                throw new AlgorithmException("Could not determine the next vertex to index");
            }
        }

        indexedVertexes.add(stopVertex);
    }

    private void calculateTMin() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        for (int index = 1; index < indexedVertexes.size(); index++) {
            Vertex currentVertex = indexedVertexes.get(index);
            CpmVertexInfo currentVertexInfo = metaInfoRepository.getMetaInfo(currentVertex, CpmVertexInfo.class);

            double tMin = 0;

            for (Link enteringLink : graphAnalyzer.getEnteringArcs(currentVertex)) {
                WeightInfo<Double> linkWeightInfo = metaInfoRepository.getMetaInfo(enteringLink, WeightInfo.class);

                Vertex head = graphAnalyzer.getHead(enteringLink);
                CpmVertexInfo headVertexInfo = metaInfoRepository.getMetaInfo(head, CpmVertexInfo.class);

                double val = headVertexInfo.getTMin() + linkWeightInfo.getWeight();
                if (val > tMin) {
                    tMin = val;
                }
            }

            metaInfoRepository.putMetaInfo(currentVertex,
                    new DefaultCpmVertexInfo(
                            currentVertexInfo.getIndex(),
                            tMin));
        }
    }

    private void calculateTMax() {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        CpmVertexInfo stopVertexInfo = metaInfoRepository.getMetaInfo(stopVertex, CpmVertexInfo.class);

        metaInfoRepository.putMetaInfo(stopVertex,
                new DefaultCpmVertexInfo(stopVertexInfo.getIndex(),
                        stopVertexInfo.getTMin(),
                        stopVertexInfo.getTMin()));

        for (int index = indexedVertexes.size() - 2; index >= 0; index--) {
            Vertex currentVertex = indexedVertexes.get(index);
            CpmVertexInfo currentVertexInfo = metaInfoRepository.getMetaInfo(currentVertex, CpmVertexInfo.class);

            double tMax = Double.POSITIVE_INFINITY;

            for (Link exitingLink : graphAnalyzer.getExitingArcs(currentVertex)) {
                Vertex tail = graphAnalyzer.getTail(exitingLink);
                CpmVertexInfo tailVertexInfo = metaInfoRepository.getMetaInfo(tail, CpmVertexInfo.class);

                WeightInfo<Double> linkWeightInfo = metaInfoRepository.getMetaInfo(exitingLink, WeightInfo.class);

                double val = tailVertexInfo.getTMax() - linkWeightInfo.getWeight();
                if (val < tMax) {
                    tMax = val;
                }
            }

            metaInfoRepository.putMetaInfo(currentVertex, new DefaultCpmVertexInfo(
                    currentVertexInfo.getIndex(),
                    currentVertexInfo.getTMin(),
                    tMax));
        }
    }

    @Override
    protected void doFinish() throws AlgorithmException {
        AlgorithmOutput algorithmOutput = getAlgorithmOutput();

        for (Activity activity : getActivities()) {
            if (activity.isCritical()) {
                criticalActivities.add(activity);
            }
        }

        if (!getActivities().isEmpty()) {
            final String activityTableFormat = "%25s%15s%15s%15s%15s%15s";

            algorithmOutput.printHeader("Activity Table");
            algorithmOutput.println(String.format(activityTableFormat, "Activity", "Duration", "EST", "LST", "S", "Critical"));

            for (Activity activity : getActivities()) {
                algorithmOutput.println(String.format(activityTableFormat, activity.getName(), activity.getDuration(), activity.getEst(), activity.getLst(), activity.getS(), activity.isCritical()));
            }
        } else {
            algorithmOutput.println("(no activities)");
        }

        algorithmOutput.println();
        algorithmOutput.println();

        if (!criticalActivities.isEmpty()) {
            algorithmOutput.printHeader("Critical activities");

            for (Activity criticalActivity : criticalActivities) {
                algorithmOutput.println(criticalActivity);
            }
        } else {
            algorithmOutput.println("(no critical activities)");
        }

    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public Vertex getStopVertex() {
        return stopVertex;
    }

    public Collection<Link> getCriticalLinks() {
        return Collections.unmodifiableCollection(activitiesToLinksConverter.convert(criticalActivities));
    }

    public Collection<Activity> getActivities() {
        Collection<Activity> result = new SortedCollection<>();

        GraphContext graphContext = getGraphContext();
        Graph graph = graphContext.getGraph();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        for (Link link : graph.getLinks()) {
            String linkName = metaInfoRepository.getMetaInfo(link, NameInfo.class).getName();

            WeightInfo<Double> linkWeightInfo = metaInfoRepository.getMetaInfo(link, WeightInfo.class);
            double linkWeight = linkWeightInfo.getWeight();
            double linkEST = graphAnalyzer.getEST(link);
            double linkLST = graphAnalyzer.getLST(link);

            if (!linkName.isEmpty()) {
                result.add(new Activity(linkName, linkWeight, linkEST, linkLST, link));
            }
        }

        return Collections.unmodifiableCollection(result);
    }

    public Collection<Activity> getCriticalActivities() {
        return Collections.unmodifiableCollection(criticalActivities);
    }

}
