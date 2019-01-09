package org.eclipse.scava.platform.communicationchannel.eclipseforums.utils;

import org.eclipse.scava.platform.Date;

public class EclipseForumUtils {

	public static Date convertStringToDate(String timestamp) {

		Long unixTimestamp = Long.parseLong(timestamp);
		Date platformDate = new Date(unixTimestamp*1000);
		
		return platformDate;
	}

	/**
	 * 
	 * Removes " (quotation marks from the first and last index of the string)
	 * 
	 * @param string
	 * @return fixedString
	 */
	public static String fixString(String string) {
		String fixedString = string.replaceAll("\"", "").replace("[", "").replace("]", "");
		return fixedString;
	}
}
