/*********************************************************************
* Copyright (c) 2017 FrontEndART Software Ltd.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.scava.plugin.event.notifier;

import org.scava.plugin.event.IEventListener;

@FunctionalInterface
public interface INotifierEventListener extends IEventListener {
	void handle();
}
