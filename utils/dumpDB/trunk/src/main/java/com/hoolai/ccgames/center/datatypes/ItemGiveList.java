/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.center.datatypes;

import java.util.LinkedList;

public class ItemGiveList {

	public static int MAX_CAPACITY = 200;

	public LinkedList< ItemGiveRecord > records = new LinkedList< ItemGiveRecord >();

	public void addRecord( ItemGiveRecord r ) {
		records.addFirst( r );
		while( records.size() > MAX_CAPACITY )
			records.removeLast();
	}
}
