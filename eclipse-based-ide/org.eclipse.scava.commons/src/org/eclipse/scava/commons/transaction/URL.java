/*********************************************************************
* Copyright (c) 2017 FrontEndART Software Ltd.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*    Zsolt J�nos Szamosv�lgyi
*    Endre Tam�s V�radi
*    Gerg� Balogh
**********************************************************************/
package org.eclipse.scava.commons.transaction;

/**
 * Provides a representation of an URL.
 *
 */
public class URL extends Transaction{
	private final String URL;
	
	public URL(String uRL) {
		super(TransactionKind.URL);
		URL = uRL;
	}
	
	public String getURL() {
		return URL;
	}
	
}
