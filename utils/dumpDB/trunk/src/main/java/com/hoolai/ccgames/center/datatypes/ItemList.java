/**
 * Author: guanxin
 * Date: 2015-07-22
 */

package com.hoolai.ccgames.center.datatypes;

/**
 * 支持道具过期时间
 * 所以道具列表如果id相同，但过期时间不同，还不能合并
 * 目前道具是有序的，首先按照id，其次按照expire排序
 * 消费时，优先扣除过期时间早的
 * 目前看来，道具种类不算太多的时候，直接遍历搜索也不会比2分差多少
 */

public class ItemList {

	public ItemUnit[] items;

	public ItemList() {
		items = new ItemUnit[0];
	}
	
	public ItemList( ItemUnit[] i ) {
		items = i;
	}

	public int bsearch( int id ) {
		int beg = 0, end = items.length - 1;
		while( beg <= end ) {
			int mid = ( beg + end ) / 2;
			if( id == items[mid].itemID )
				return mid;
			else if( id > items[mid].itemID )
				beg = mid + 1;
			else
				end = mid - 1;
		}
		return -1;
	}

	public int lowerbound( int id ) {
		int beg = 0, end = items.length - 1;
		while( beg <= end ) {
			int mid = ( beg + end ) / 2;
			if( id > items[mid].itemID )
				beg = mid + 1;
			else
				end = mid - 1;
		}
		return end + 1;
	}
	
	// 检查x的过期时间是否比y小，0表示不过期
	private boolean expireLess( long x, long y ) {
		if( x == 0 ) return false;
		if( y == 0 ) return true;
		return x < y;
	}
	
	private boolean compLess( int id1, long expire1, int id2, long expire2 ) {
		return id1 < id2
				|| ( id1 == id2 && expireLess( expire1, expire2 ) );
	}

	/**
	 * @param itemID
	 *            道具ID
	 * @param itemCount
	 *            道具数量
	 * @return 更改是否成功，一般来说只有减少但是不足才会失败
	 */
	public boolean change( int itemID, int itemCount, long expire ) {
		if( itemCount == 0 )
			return true;
		
		// 如果是扣除道具，则忽略expire参数，优先扣除快到期的
		if( itemCount < 0 ) {
			boolean enough = false;
			
			int tot = 0;
			int i = 0, j;
			while( i < items.length && items[i].itemID < itemID ) ++i;
			j = i;
			while( j < items.length && items[j].itemID == itemID ) {
				tot += items[j].itemCount;
				if( tot + itemCount >= 0 ) {
					enough = true;
					break;
				}
				++j;
			}
			
			if( enough ) {
				// 恰好用光[i,j]区间道具
				if( tot + itemCount == 0 ) {
					int len = j + 1 - i;
					ItemUnit[] tmp = new ItemUnit[items.length - len];
					System.arraycopy( items, 0, tmp, 0, i );
					System.arraycopy( items, j + 1, tmp, i, tmp.length - i );
					items = tmp;
				}
				// 第[i]个位置就比需要扣除的多
				else if( items[i].itemCount + itemCount > 0 ) {
					items[i].itemCount += itemCount;
				}
				// [i,j-1]用光，j用了部分
				else {
					items[j].itemCount = tot + itemCount;
					int len = j - i;
					ItemUnit[] tmp = new ItemUnit[items.length - len];
					System.arraycopy( items, 0, tmp, 0, i );
					System.arraycopy( items, j, tmp, i, tmp.length - i );
					items = tmp;
				}
			}
			return enough;
		}
		
		// 这里是添加道具
		else {
			
			int i = 0;
			while( i < items.length
					&& compLess( items[i].itemID, items[i].expireTime, itemID, expire ) ) {
				++i;
			}
			
			if( i < items.length
					&& items[i].itemID == itemID
					&& items[i].expireTime == expire ) {
				items[i].itemCount += itemCount;
			}
			else {
				ItemUnit[] tmp = new ItemUnit[items.length + 1];
				System.arraycopy( items, 0, tmp, 0, i );
				tmp[i] = new ItemUnit( itemID, itemCount, expire );
				System.arraycopy( items, i, tmp, i + 1,
						tmp.length - ( i + 1 ) );
				items = tmp;
			}
		}

		return true;
	}

	public int getCount( int itemID ) {
		int ans = 0;
		int i = 0;
		while( i < items.length && items[i].itemID < itemID ) ++i;
		while( i < items.length && items[i].itemID == itemID ) {
			ans += items[i].itemCount;
			++i;
		}
		return ans;
	}
	
	public int getMemBytes() {
		return ItemUnit.getMemBytes() * items.length;
	}

	public void print() {
		System.out.printf( "ItemList : {" );
		for( int i = 0; i < items.length; ++i ) {
			System.out.printf( " %d,%d,%d",
					items[i].itemID, items[i].itemCount, items[i].expireTime );
		}
		System.out.printf( " }\n" );
	}

	public static void main( String[] args ) {
		ItemList list = new ItemList();
		list.change( 1, 100, 0 );
		list.change( 2, 100, 0 );
		list.change( 3, 100, 0 );
		list.print();
		list.change( 1, -50, 0 );
		list.change( 2, -100, 0 );
		list.change( 3, -100, 0 );
		list.print();
		list.change( 1, -50, 0 );
		list.change( 1, -50, 0 );
		list.print();
		
	}
}
