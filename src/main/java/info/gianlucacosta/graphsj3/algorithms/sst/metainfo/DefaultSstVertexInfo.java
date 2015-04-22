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

import info.gianlucacosta.arcontes.graphs.Vertex;

import java.util.Objects;

public class DefaultSstVertexInfo implements SstVertexInfo {

    private final Vertex bestVertex;
    private final double distanceFromBestVertex;

    public DefaultSstVertexInfo(Vertex bestVertex, double distanceFromBestVertex) {
        this.bestVertex = bestVertex;
        this.distanceFromBestVertex = distanceFromBestVertex;
    }

    @Override
    public Vertex getBestVertex() {
        return bestVertex;
    }

    @Override
    public double getDistanceFromBestVertex() {
        return distanceFromBestVertex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SstVertexInfo)) {
            return false;
        }

        SstVertexInfo other = (SstVertexInfo) obj;

        return Objects.equals(bestVertex, other.getBestVertex())
                && (distanceFromBestVertex == other.getDistanceFromBestVertex());
    }

    @Override
    public int hashCode() {
        return bestVertex.hashCode();
    }

}
