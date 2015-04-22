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

package info.gianlucacosta.graphsj3.scenarios.cpm;

import info.gianlucacosta.arcontes.fx.metainfo.agents.InteractiveDoubleWeightInfoAgent;
import info.gianlucacosta.arcontes.fx.metainfo.agents.InteractiveUniqueLinkNameInfoAgent;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultNameInfo;
import info.gianlucacosta.graphsj3.algorithms.cpm.metainfo.CpmLinkLabelInfo;
import info.gianlucacosta.graphsj3.scenarios.common.CommonScenarioSettings;
import info.gianlucacosta.graphsj3.scenarios.common.metainfo.CommonCanvasAgentsFactory;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.CompositeMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

public class CpmCanvasAgentsFactory extends CommonCanvasAgentsFactory {

    public CpmCanvasAgentsFactory(CommonScenarioSettings scenarioSettings, CommonInputService commonInputService) {
        super(scenarioSettings, commonInputService);
    }

    @Override
    public MetaInfoAgent<Link> getAgentForLinkCreation(final GraphContext graphContext, final Link link) {
        return new CompositeMetaInfoAgent<>(
                super.getAgentForLinkCreation(graphContext, link),
                new AbstractMetaInfoAgent<Link>(false) {
                    @Override
                    protected boolean doAct(MetaInfoRepository metaInfoRepository, Link link) {
                        metaInfoRepository.putMetaInfo(link, new DefaultNameInfo(""));
                        metaInfoRepository.putMetaInfo(link, new CpmLinkLabelInfo(metaInfoRepository, link));
                        return true;
                    }
                }
        );
    }

    @Override
    public MetaInfoAgent<Link> getAgentForLinkEditing(GraphContext graphContext, Link link) {
        return new CompositeMetaInfoAgent<>(
                new InteractiveUniqueLinkNameInfoAgent(graphContext, getCommonInputService(), "Activity name:", true),
                new InteractiveDoubleWeightInfoAgent<Link>(getCommonInputService(), "Duration:"));
    }

}
