/*********************************************************************
* Copyright (c) 2017 FrontEndART Software Ltd.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.scava.plugin.ui.recommendationaccept;

import java.util.List;
import java.util.stream.Collectors;

import org.scava.plugin.event.informative.IInformativeEvent;
import org.scava.plugin.event.informative.IInformativeEventSubscriber;
import org.scava.plugin.event.informative.InformativeEvent;
import org.scava.plugin.event.notifier.INotifierEvent;
import org.scava.plugin.event.notifier.INotifierEventSubscriber;
import org.scava.plugin.event.notifier.NotifierEvent;
import org.scava.plugin.recommendation.highlight.SourceCodeHighlighterController;
import org.scava.plugin.recommendation.highlight.SourceCodeHighlighterModel;
import org.scava.plugin.recommendation.highlight.SourceCodeHighlighterView;
import org.scava.plugin.ui.abstractmvc.AbstractController;
import org.scava.plugin.utils.Utils;
import org.scava.commons.highlight.IHighlightable;
import org.scava.commons.recommendation.RecommendationKind;
import org.scava.commons.recommendation.RecommendationSet;
import org.scava.commons.recommendation.source.SourceRecommendation;
import org.eclipse.core.resources.IProject;

public class RecommendationAcceptController extends
		AbstractController<RecommendationAcceptModel, RecommendationAcceptView> {
	private final IInformativeEvent<RecommendationSet> recommendationsAccepted = registerEvent(
			new InformativeEvent<>());
	private final INotifierEvent recommendationsCancelled = registerEvent(new NotifierEvent());
	
	private SourceCodeHighlighterController sourceCodeHighlighterController;
	
	public RecommendationAcceptController(RecommendationAcceptModel model, RecommendationAcceptView view,
			IProject project) {
		super(model, view);
		
		getView().getRecommendationsAccepted().subscribe(this::onRecommendationsAccepted);
		getView().getRecommendationsCancelled().subscribe(() -> recommendationsCancelled.invoke());
		
		getView().showRecommendations(getModel().getRecommendations().asList());
		
		initSourceCodeHighlighter(project);
	}
	
	public IInformativeEventSubscriber<RecommendationSet> getRecommendationsAccepted() {
		return recommendationsAccepted;
	}
	
	public INotifierEventSubscriber getRecommendationsCancelled() {
		return recommendationsCancelled;
	}
	
	private void onRecommendationsAccepted() {
		RecommendationSet recommendations = getModel().getRecommendations();
		recommendationsAccepted.invoke(recommendations);
	}
	
	private void initSourceCodeHighlighter(IProject project) {
		List<SourceRecommendation> sourceRecommendations = Utils.filterType(getModel().getRecommendations().asList(),
				RecommendationKind.SOURCE_REPLACE);
		List<IHighlightable> highlightables = sourceRecommendations.stream().map(IHighlightable.class::cast).collect(
				Collectors.toList());
		
		SourceCodeHighlighterModel sourceCodeHighlighterModel = new SourceCodeHighlighterModel(highlightables);
		SourceCodeHighlighterView sourceCodeHighlighterView = new SourceCodeHighlighterView(project);
		sourceCodeHighlighterController = new SourceCodeHighlighterController(sourceCodeHighlighterModel,
				sourceCodeHighlighterView);
		addSubController(sourceCodeHighlighterController);
	}
}
