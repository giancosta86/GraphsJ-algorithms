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

package info.gianlucacosta.graphsj3.algorithms.cpm.metainfo;

import info.gianlucacosta.arcontes.algorithms.metainfo.CurrentStepInfo;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

public class CpmRuntimeVertexLabelInfo implements LabelInfo {

    private final GraphContext graphContext;
    private final Vertex vertex;

    public CpmRuntimeVertexLabelInfo(GraphContext graphContext, Vertex vertex) {
        this.graphContext = graphContext;
        this.vertex = vertex;
    }

    @Override
    public String getLabel() {
        MetaInfoRepository metaInfoRepository = graphContext.getMetaInfoRepository();

        String vertexName = metaInfoRepository.getMetaInfo(vertex, NameInfo.class).getName();

        CpmVertexInfo vertexInfo;

        try {
            vertexInfo = metaInfoRepository.getMetaInfo(vertex, CpmVertexInfo.class);
        } catch (MetaInfoException ex) {
            return vertexName;
        }

        int index = vertexInfo.getIndex();
        double tMin = vertexInfo.getTMin();
        double tMax = vertexInfo.getTMax();

        int currentStep = metaInfoRepository.getMetaInfo(graphContext, CurrentStepInfo.class).getCurrentStep();

        switch (currentStep) {
            case 1:
                return String.format("%s (%s)", vertexName, index);

            case 2:
                return String.format("%s (%s) [%s]", vertexName, index, tMin);

            case 3:
                return String.format("%s (%s) [%s, %s]", vertexName, index, tMin, tMax);

            default:
                return vertexName;
        }
    }

}
