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

package info.gianlucacosta.graphsj3.algorithms.sst.metainfo;

import info.gianlucacosta.arcontes.algorithms.CommonAlgorithmSettings;
import info.gianlucacosta.arcontes.formatting.CommonDecimalFormat;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.text.DecimalFormat;

public class SstRuntimeVertexLabelInfo implements LabelInfo {

    private final MetaInfoRepository metaInfoRepository;
    private final Vertex vertex;
    private final CommonAlgorithmSettings algorithmSettings;

    public SstRuntimeVertexLabelInfo(MetaInfoRepository metaInfoRepository, Vertex vertex, CommonAlgorithmSettings algorithmSettings) {
        this.metaInfoRepository = metaInfoRepository;
        this.vertex = vertex;
        this.algorithmSettings = algorithmSettings;
    }

    @Override
    public String getLabel() {
        String vertexName = metaInfoRepository.getMetaInfo(vertex, NameInfo.class).getName();

        SstVertexInfo sstVertexInfo;
        try {
            sstVertexInfo = metaInfoRepository.getMetaInfo(vertex, SstVertexInfo.class);
        } catch (MetaInfoException ex) {
            return vertexName;
        }

        String bestVertexName;

        Vertex bestVertex = sstVertexInfo.getBestVertex();
        if (bestVertex != null) {
            bestVertexName = metaInfoRepository.getMetaInfo(bestVertex, NameInfo.class).getName();
        } else {
            bestVertexName = "Ø";
        }

        DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();

        if (algorithmSettings.isVerbose()) {
            return String.format("%s {%s, %s}",
                    vertexName,
                    bestVertexName,
                    decimalFormat.format(sstVertexInfo.getDistanceFromBestVertex()));
        } else {
            return String.format("%s {%s}", vertexName, bestVertexName);
        }
    }

}
