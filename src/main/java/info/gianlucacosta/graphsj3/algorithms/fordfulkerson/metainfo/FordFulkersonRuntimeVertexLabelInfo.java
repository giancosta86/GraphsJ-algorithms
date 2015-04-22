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

package info.gianlucacosta.graphsj3.algorithms.fordfulkerson.metainfo;

import info.gianlucacosta.arcontes.formatting.CommonDecimalFormat;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.text.DecimalFormat;

public class FordFulkersonRuntimeVertexLabelInfo implements LabelInfo {

    private final MetaInfoRepository metaInfoRepository;
    private final Vertex vertex;

    public FordFulkersonRuntimeVertexLabelInfo(MetaInfoRepository metaInfoRepository, Vertex vertex) {
        this.metaInfoRepository = metaInfoRepository;
        this.vertex = vertex;
    }

    @Override
    public String getLabel() {
        FordFulkersonVertexInfo vertexInfo = metaInfoRepository.getMetaInfo(vertex, FordFulkersonVertexInfo.class);

        String vertexName = metaInfoRepository.getMetaInfo(vertex, NameInfo.class).getName();

        if (!vertexInfo.isLabeled()) {
            return vertexName;
        } else {
            Vertex vk = vertexInfo.getVk();

            DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();

            return String.format("%s {%s%s, %s}%s",
                    vertexName,
                    vertexInfo.isPlusVk() ? "+" : "-",
                    vk != null ? metaInfoRepository.getMetaInfo(vk, NameInfo.class).getName() : "Ø",
                    decimalFormat.format(vertexInfo.getDelta()),
                    vertexInfo.isExplored() ? " @" : "");
        }
    }

}
