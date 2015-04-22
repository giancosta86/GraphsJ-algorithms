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

import info.gianlucacosta.arcontes.graphs.Vertex;

import java.util.Objects;

public class DefaultFordFulkersonVertexInfo implements FordFulkersonVertexInfo {

    private boolean labeled;
    private boolean plusVk;
    private Vertex vk;
    private double delta;
    private boolean explored;

    public DefaultFordFulkersonVertexInfo() {
        this(false, false, null, 0, false);
    }

    public DefaultFordFulkersonVertexInfo(FordFulkersonVertexInfo source) {
        this(source.isLabeled(), source.isPlusVk(), source.getVk(), source.getDelta(), source.isExplored());
    }

    public DefaultFordFulkersonVertexInfo(boolean labeled, boolean plusVk, Vertex vk, double delta, boolean explored) {
        this.labeled = labeled;
        this.plusVk = plusVk;
        this.vk = vk;
        this.delta = delta;
        this.explored = explored;
    }

    @Override
    public boolean isLabeled() {
        return labeled;
    }

    public void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

    @Override
    public boolean isPlusVk() {
        return plusVk;
    }

    public void setPlusVk(boolean plusVk) {
        this.plusVk = plusVk;
    }

    @Override
    public Vertex getVk() {
        return vk;
    }

    public void setVk(Vertex vk) {
        this.vk = vk;
    }

    @Override
    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FordFulkersonVertexInfo)) {
            return false;
        }

        FordFulkersonVertexInfo other = (FordFulkersonVertexInfo) obj;

        return (labeled == other.isLabeled())
                && (plusVk == other.isPlusVk())
                && Objects.equals(vk, other.getVk())
                && (delta == other.getDelta())
                && (explored == other.isExplored());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
