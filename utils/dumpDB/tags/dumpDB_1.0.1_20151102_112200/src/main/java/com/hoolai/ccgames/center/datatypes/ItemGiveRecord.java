/**
 * Author: guanxin
 * Date: 2015-08-27
 */

package com.hoolai.ccgames.center.datatypes;

import com.hoolai.ccgames.center.utils.DataConverter;

public class ItemGiveRecord {

	public static final int SIZE_BYTES = 8 * 3 + 4 * 3;

	public long senderID;
	public long receiverID;
	public long time;
	public int itemID;
	public int itemCount;
	public int status;

	public ItemGiveRecord() {
	}

	public ItemGiveRecord( long senderID, long receiverID, long time,
			int itemID, int itemCount, int status ) {
		super();
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.time = time;
		this.itemID = itemID;
		this.itemCount = itemCount;
		this.status = status;
	}

	public int writeBytes( byte[] b, int pos ) {
		pos += DataConverter.writeBytes( senderID, b, pos );
		pos += DataConverter.writeBytes( receiverID, b, pos );
		pos += DataConverter.writeBytes( time, b, pos );
		pos += DataConverter.writeBytes( itemID, b, pos );
		pos += DataConverter.writeBytes( itemCount, b, pos );
		pos += DataConverter.writeBytes( status, b, pos );
		return SIZE_BYTES;
	}

	public int readBytes( byte[] b, int pos ) {
		senderID = DataConverter.readLong( b, pos );
		pos += 8;
		receiverID = DataConverter.readLong( b, pos );
		pos += 8;
		time = DataConverter.readLong( b, pos );
		pos += 8;
		itemID = DataConverter.readInt( b, pos );
		pos += 4;
		itemCount = DataConverter.readInt( b, pos );
		pos += 4;
		status = DataConverter.readInt( b, pos );
		pos += 4;
		return SIZE_BYTES;
	}

}
