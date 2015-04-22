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

public class DefaultCpmVertexInfo implements CpmVertexInfo {

    private final int index;
    private final double tMin;
    private final double tMax;

    public DefaultCpmVertexInfo(int index) {
        this(index, 0, 0);
    }

    public DefaultCpmVertexInfo(int index, double tMin) {
        this(index, tMin, 0);
    }

    public DefaultCpmVertexInfo(int index, double tMin, double tMax) {
        this.index = index;
        this.tMin = tMin;
        this.tMax = tMax;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public double getTMin() {
        return tMin;
    }

    @Override
    public double getTMax() {
        return tMax;
    }

}
