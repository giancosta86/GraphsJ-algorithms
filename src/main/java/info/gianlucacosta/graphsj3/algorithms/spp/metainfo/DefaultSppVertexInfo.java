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

package info.gianlucacosta.graphsj3.algorithms.spp.metainfo;

import info.gianlucacosta.arcontes.graphs.Vertex;

import java.util.Objects;

public class DefaultSppVertexInfo implements SppVertexInfo {

    private final Vertex previousVertex;
    private final double pathLength;

    public DefaultSppVertexInfo(Vertex previousVertex, double pathLength) {
        this.previousVertex = previousVertex;
        this.pathLength = pathLength;
    }

    @Override
    public Vertex getPreviousVertex() {
        return previousVertex;
    }

    @Override
    public double getPathLength() {
        return pathLength;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SppVertexInfo)) {
            return false;
        }

        SppVertexInfo other = (SppVertexInfo) obj;

        return Objects.equals(previousVertex, other.getPreviousVertex())
                && (pathLength == other.getPathLength());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
