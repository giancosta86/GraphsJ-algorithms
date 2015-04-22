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
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.metainfo.CapacityInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.LabelInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.text.DecimalFormat;

public class FordFulkersonLinkLabelInfo implements LabelInfo {

    private final MetaInfoRepository metaInfoRepository;
    private final Link link;

    public FordFulkersonLinkLabelInfo(MetaInfoRepository metaInfoRepository, Link link) {
        this.metaInfoRepository = metaInfoRepository;
        this.link = link;
    }

    @Override
    public String getLabel() {
        WeightInfo<Double> weightInfo = metaInfoRepository.getMetaInfo(link, WeightInfo.class);
        CapacityInfo<Double> capacityInfo = metaInfoRepository.getMetaInfo(link, CapacityInfo.class);

        DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();
        return String.format("%s/%s", decimalFormat.format(weightInfo.getWeight()), decimalFormat.format(capacityInfo.getCapacity()));
    }

}
