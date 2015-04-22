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

import info.gianlucacosta.arcontes.graphs.Link;

import java.util.Objects;

/**
 * A CPM activity
 */
public class Activity implements Comparable<Activity> {

    private final String name;
    private final double duration;
    private final double est;
    private final double lst;
    private final Link link;

    public Activity(String name, double duration, double est, double lst, Link link) {
        this.name = name;
        this.duration = duration;
        this.est = est;
        this.lst = lst;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public double getDuration() {
        return duration;
    }

    public double getEst() {
        return est;
    }

    public double getLst() {
        return lst;
    }

    public double getS() {
        return lst - est;
    }

    public boolean isCritical() {
        return getS() == 0;
    }

    public Link getLink() {
        return link;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Activity)) {
            return false;
        }

        Activity other = (Activity) obj;

        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Activity other) {
        return name.compareTo(other.name);
    }

}
