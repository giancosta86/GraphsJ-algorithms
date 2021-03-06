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

package info.gianlucacosta.graphsj3.scenarios.spp;

import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.graphsj3.scenarios.ScenarioDescriptor;

/**
 * SPP scenario descriptor
 */
public class SppScenarioDescriptor implements ScenarioDescriptor {

    @Override
    public String getName() {
        return "Dijkstra's shortest-path problem (SPP)";
    }

    @Override
    public Scenario createScenario() {
        return new SppScenario();
    }

}
