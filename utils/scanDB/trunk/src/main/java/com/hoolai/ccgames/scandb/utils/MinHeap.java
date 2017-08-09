package com.hoolai.ccgames.scandb.utils;

public class MinHeap < E extends Comparable<E> > {
	public  E[]     elements;
	
	public  MinHeap() {}
	
	public  MinHeap( E[] ele ) { this.elements = ele; }
	
	public  void  make_heap( int size ) {
		for( int i = size/2 - 1; i >= 0; --i )
			heapify( i, size );
	}
	
	public  void  swap( int x, int y ) {
		E  tmp = elements[x];
		elements[x] = elements[y];
		elements[y] = tmp;
	}
	
	public  void  heapify( int i, int size ) {
		int  max = i;
		int  left = 2*i + 1;
		int  right = 2*i + 2;
		if( left < size && elements[left].compareTo( elements[max] ) < 0 )
			max = left;
		if( right < size && elements[right].compareTo( elements[max] ) < 0 )
			max = right;
		
		if( max != i ) {
			swap( i, max );
			heapify( max, size );
		}
	}
	
	public  void  sort_heap( int size ) {
		int  sz = size;
		for( int i = size - 1; i > 0; --i ) {
			swap( 0, i );
			heapify( 0, --sz );
		}
	}
	
	public  void  push_heap( int size ) {
		if( elements[size].compareTo( elements[0] ) > 0 ) {
			swap( 0, size );
			heapify( 0, size );
		}
	}
	
	public  static  void  main( String [] args ) {
		int  N = 100;
	}
}
