/**
 * Author: guanxin@hoolai.com
 * Date: 2012-05-21 16:33
 */

package com.bitop.common.skeleton.net;

import com.bitop.common.skeleton.base.SynEnvBase;
import com.bitop.common.skeleton.base.SynEnvTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class BusyBlockingQueue<E extends SynEnvTask> /*
                                                              * extends
															  * AbstractQueue<E>
															  */
        implements BlockingQueue< Runnable >, java.io.Serializable {

    private static final Logger logger = LoggerFactory.getLogger( BusyBlockingQueue.class );

    private static final long serialVersionUID = 1L;

    static class Node< E > {
        E item;
        Node< E > next;

        Node( E x ) {
            item = x;
        }

        Node( E x, Node< E > n ) {
            item = x;
            next = n;
        }
    }

    static class NodePool< E > { // 非回收对象池，node不会回收给系统

        Node< E > head = null;

        NodePool( int initCapa ) {
            for( int i = 0; i < initCapa; ++i ) {
                head = new Node< E >( null, head );
            }
        }

        Node< E > alloc() {
            synchronized( this ) {
                if( head != null ) {
                    Node< E > x = head;
                    head = head.next;
                    return x;
                }
            }
            return new Node< E >( null, head );
        }

        void free( Node< E > x ) {
            synchronized( this ) {
                x.next = head;
                head = x;
            }
        }

        void free( Node< E > h, Node< E > t ) {
            synchronized( this ) {
                t.next = head;
                head = h;
            }
        }

    }

    private final NodePool< E > pool;
    private final int capacity;
    private int count;
    private transient Node< E > head;
    private transient Node< E > tail;

    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public BusyBlockingQueue( int capa ) {
        this( capa, false );
    }

    public BusyBlockingQueue( int capa, boolean fair ) {
        if( capa <= 0 )
            throw new IllegalArgumentException();
        pool = new NodePool< E >( 8192 );
        capacity = capa;
        count = 0;
        head = pool.alloc();
        tail = pool.alloc();
        head.next = tail;
        lock = new ReentrantLock( fair );
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    private void addTail( E x ) {
        if( x == null ) {
            logger.error( "[BusyBlockingQueue::addTail] add null !" );
        }
        tail.item = x;
        tail.next = this.pool.alloc();
        tail = tail.next;
        ++count;
        notEmpty.signal();
    }

    private Runnable extract() {
        Node< E > pre = head;
        Node< E > now = head.next;
        for( int i = 0; i < count; ++i ) {
            E item = now.item;
            if( !item.isSynEnvProcessing() ) {
                item.setSynEnvProcessing( true );
                pre.next = now.next;
                pool.free( now );
                --count;
                notFull.signal();
                return item;
            }
            else {
                pre = now;
                now = now.next;
            }
        }
        return null;
    }

    public Runnable poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return ( count == 0 ) ? null : extract();
        }
        finally {
            lock.unlock();
        }
    }

    public Runnable peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return ( count == 0 ) ? null : head.next.item;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator< Runnable > iterator() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return new Itr();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return count;
        }
        finally {
            lock.unlock();
        }
    }

    private class Itr implements Iterator< Runnable > {

        private Node< E > pre;
        private Node< E > now;

        Itr() {
            pre = null;
            now = BusyBlockingQueue.this.head;
        }

        @Override
        public boolean hasNext() {
            return now != BusyBlockingQueue.this.tail
                    && now.next != BusyBlockingQueue.this.tail;
        }

        @Override
        public Runnable next() {
            final ReentrantLock lock = BusyBlockingQueue.this.lock;
            lock.lock();
            try {
                if( now == BusyBlockingQueue.this.tail
                        || now.next == BusyBlockingQueue.this.tail )
                    throw new NoSuchElementException();
                pre = now;
                now = now.next;
                return now.item;
            }
            finally {
                lock.unlock();
            }
        }

        @Override
        public void remove() {
            final ReentrantLock lock = BusyBlockingQueue.this.lock;
            lock.lock();
            try {
                if( pre == null || now == BusyBlockingQueue.this.tail )
                    throw new IllegalStateException();
                pre.next = now.next;
                BusyBlockingQueue.this.pool.free( now );
                --BusyBlockingQueue.this.count;
                now = pre.next;
            }
            finally {
                lock.unlock();
            }
        }

    }


    @Override
    public boolean add( Runnable e ) {
        if( offer( e ) )
            return true;
        else
            throw new IllegalStateException( "Queue full" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public boolean offer( Runnable e ) {
        if( e == null )
            throw new NullPointerException();

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if( count == capacity )
                return false;
            else {
                addTail( (E) e );
                return true;
            }
        }
        finally {
            lock.unlock();
        }
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void put( Runnable e ) throws InterruptedException {
        if( e == null )
            throw new NullPointerException();

        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            try {
                while( count == capacity )
                    notFull.await();
            }
            catch( InterruptedException ie ) {
                notFull.signal(); // propagate to non-interrupted thread
                throw ie;
            }
            addTail( (E) e );
        }
        finally {
            lock.unlock();
        }
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public boolean offer( Runnable e, long timeout, TimeUnit unit )
            throws InterruptedException {
        if( e == null )
            throw new NullPointerException();

        long nanos = unit.toNanos( timeout );
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for( ; ; ) {
                if( count != capacity ) {
                    addTail( (E) e );
                    return true;
                }
                if( nanos <= 0 )
                    return false;
                try {
                    nanos = notFull.awaitNanos( nanos );
                }
                catch( InterruptedException ie ) {
                    notFull.signal(); // propagate to non-interrupted thread
                    throw ie;
                }
            }
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Runnable take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        Runnable x = null;
        try {
            try {
                while( count == 0 || ( x = extract() ) == null )
                    notEmpty.await();
            }
            catch( InterruptedException ie ) {
                notEmpty.signal(); // propagate to non-interrupted thread
                throw ie;
            }
            return x;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Runnable poll( long timeout, TimeUnit unit )
            throws InterruptedException {
        long nanos = unit.toNanos( timeout );
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            Runnable x = null;
            for( ; ; ) {
                if( count != 0 && ( x = extract() ) != null )
                    return x;
                if( nanos <= 0 )
                    return null;
                try {
                    nanos = notEmpty.awaitNanos( nanos );
                }
                catch( InterruptedException ie ) {
                    notEmpty.signal(); // propagate to non-interrupted thread
                    throw ie;
                }
            }
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int remainingCapacity() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return capacity - count;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove( Object o ) {
        final ReentrantLock lock = this.lock;
        lock.lock();

        try {
            if( count == 0 )
                return false;
            Node< E > pre = head;
            Node< E > now = head.next;

            for( int i = 0; i < count; ++i ) {
                if( now.item.equals( o ) ) {
                    pre.next = now.next;
                    pool.free( now );
                    notFull.signal();
                    return true;
                }
                else {
                    pre = now;
                    now = now.next;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains( Object o ) {
        final ReentrantLock lock = this.lock;
        lock.lock();

        try {
            if( count == 0 )
                return false;
            Node< E > now = head.next;
            for( int i = 0; i < count; ++i ) {
                if( now.item.equals( o ) )
                    return true;
                else {
                    now = now.next;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int drainTo( Collection< ? super Runnable > c ) {
        if( c == null )
            throw new NullPointerException();
        // if (c == this) throw new IllegalArgumentException();

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            for( ; n < count; ++n ) {
                Node< E > tmp = head.next;
                c.add( tmp.item );
                pool.free( head );
                head = tmp;
            }
            if( n > 0 ) {
                count = 0;
                notFull.signalAll();
            }
            return n;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int drainTo( Collection< ? super Runnable > c, int maxElements ) {
        if( c == null )
            throw new NullPointerException();
        // if (c == this) throw new IllegalArgumentException();
        if( maxElements <= 0 )
            return 0;

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            int max = ( maxElements < count ) ? maxElements : count;
            for( ; n < max; ++n ) {
                Node< E > tmp = head.next;
                c.add( tmp.item );
                pool.free( head );
                head = tmp;
            }
            if( n > 0 ) {
                count = 0;
                notFull.signalAll();
            }
            return n;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Runnable remove() {
        Runnable x = poll();
        if( x != null )
            return x;
        else
            throw new NoSuchElementException();
    }

    @Override
    public Runnable element() {
        Runnable x = peek();
        if( x != null )
            return x;
        else
            throw new NoSuchElementException();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public < T > T[] toArray( T[] a ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsAll( Collection< ? > c ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll( Collection< ? extends Runnable > c ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll( Collection< ? > c ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll( Collection< ? > c ) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if( count == 0 )
                return;
            pool.free( head.next.next, tail );
            tail = head.next;
            tail.item = null;
            notFull.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    static class SynRoomTask2 extends SynEnvTask {
        public SynRoomTask2( SynEnvBase r ) {
            super( r );
        }

        @Override
        public void processTask() {
            try {
                Thread.sleep( 1000 );
            }
            catch( InterruptedException e ) {
                logger.error( e.getMessage(), e );
            }
        }

    }

    public static void main( String... args ) throws InterruptedException {
        BusyBlockingQueue< SynEnvTask > q = new BusyBlockingQueue< SynEnvTask >(
                100 );
        SynRoomTask2 t1 = new SynRoomTask2( new SynEnvBase( 1 ) );
        SynRoomTask2 t2 = new SynRoomTask2( new SynEnvBase( 2 ) );
        SynRoomTask2 t3 = new SynRoomTask2( new SynEnvBase( 3 ) );
        q.put( t1 );
        q.put( t2 );
        q.put( t3 );
        Iterator< Runnable > it = q.iterator();
        while( it.hasNext() ) {
            SynEnvTask x = (SynEnvTask) it.next();
            System.out.println( x.getSynEnv().getSynId() );
            it.remove();
        }
    }

}
