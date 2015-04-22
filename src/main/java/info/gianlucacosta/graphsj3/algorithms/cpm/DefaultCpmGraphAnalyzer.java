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

import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.analysis.DefaultOrientedGraphAnalyzer;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.graphsj3.algorithms.cpm.metainfo.CpmVertexInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

public class DefaultCpmGraphAnalyzer extends DefaultOrientedGraphAnalyzer implements CpmGraphAnalyzer {

    public DefaultCpmGraphAnalyzer(GraphContext graphContext) {
        super(graphContext);
    }

    @Override
    public double getEST(Link link) {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        Vertex head = getHead(link);

        CpmVertexInfo headInfo = metaInfoRepository.getMetaInfo(head, CpmVertexInfo.class);

        return headInfo.getTMin();
    }

    @Override
    public double getLST(Link link) {
        GraphContext graphContext = getGraphContext();
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        WeightInfo<Double> linkWeightInfo = metaInfoRepository.getMetaInfo(link, WeightInfo.class);

        Vertex tail = getTail(link);
        CpmVertexInfo tailInfo = metaInfoRepository.getMetaInfo(tail, CpmVertexInfo.class);

        return tailInfo.getTMax() - linkWeightInfo.getWeight();
    }

}
