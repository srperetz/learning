package com.placester.test;

//import java.util.concurrent.locks.*;

// NOTE: we are aware that there is a PriorityQueue in
// java.util. Please do not use this. 
// If you are doing this test at home, please do not use any containers from
// java.util in your solution, as this is a test of data
// structure knowledge, rather than a test of java library knowledge.
// If you are doing it in the office, please ask the person testing you if you are going to
// use any built in collections other than arrays.

/*
 * The task is as follows: implement this class as you see fit, and get the unit test in
 * src/test/com/placester/test/PriorityQueueTest to pass. This class
 * must allow dynamic resizing as elements are added. What the
 * strategy is to do this is entirely up to you modulo the previously
 * stated constraints.
 * 
 * Feel free to use anything from Java.util.Arrays (e.g., you don't need to implement
 * your own sort if you don't want to).
 */
public class ThreadSafePriorityQueue<X> implements SimpleQueue<Priority<X>>
{
    private Node<Priority<X>> _head = null;
    private int               _size = 0;
    
    public ThreadSafePriorityQueue()
    {
        initialize();
    }
    
    
    public void initialize()
    {
    	// Nothing to do here...
    }
    
    @Override
    public synchronized int size()
    {
        return _size;
    }

    @Override
    public synchronized boolean isEmpty()
    {
        return _size == 0;
    }

    @Override
    public synchronized void clear()
    {
    	_head = null;
    	_size = 0;

    }

    @Override
    public synchronized boolean add(Priority<X> e)
    {
    	// If queue is empty, initialize it with the new item:
    	if (isEmpty())
    	{
    		_head = new Node<>(e);
    	}
    	// Otherwise add the new item to the right place in the list:
    	else
    	{
    		Node<Priority<X>> eNode = new Node<>(e);
    		Node<Priority<X>> n;
    		for (n = _head; n != null; n = n.getNext())
    		{
    			// If we found a node n that's lower priority than e, insert e before that node:
    			if (e.priority() < n.getItem().priority())
    			{
        			Node<Priority<X>> nPrev = n.getPrev();

        			// If n is _head, then e becomes the new _head:
        			if (nPrev == null) _head = eNode;
        			// Otherwise, point the node that was previously just before n to eNode:
        			else nPrev.setNext(eNode);

        			// Point eNode and n to each other:
        			n.setPrev(eNode);
        			eNode.setNext(n);

        			// Finally, point eNode to whatever was previously just before n (which may be null):
        			eNode.setPrev(nPrev);

        			break;
    			}
    			// If n is the last node (e is the lowest priority, then e becomes the new last node:
    			else if (n.getNext() == null)
    			{
        			n.setNext(eNode);
        			eNode.setPrev(n);
        			
        			break;
    			}
    		}
    	}

    	// Increment list size:
    	_size++;

        return true;
    }

    @Override
    public synchronized Priority<X> poll()
    {
    	// Empty list?
    	if (isEmpty()) return null;

		Priority<X> currentHead = _head.getItem();
	
		// Only 1 item in list? Just clear it, since it's now empty:
		if (size() == 1)
		{
			clear();
		}
		else
		{
			_head = _head.getNext();
			_head.setPrev(null);

			// Decrement list size:
			_size--;
		}

		return currentHead;
    }

    @Override
    public synchronized Priority<X> peek()
    {
        return isEmpty() ? null : _head.getItem();
    }

    @Override
    public synchronized boolean contains(Priority<X> x)
    {
    	// Can't contain a null elements in this collection:
    	if (x == null) throw new NullPointerException("Collection may not contain nulls");
		for (Node<Priority<X>> n = _head; n != null; n = n.getNext())
		{
			if (n.getItem().equals(x)) return true;
		}
		return false;
    }

    private class Node<T>
    {
    	Node<T> _next = null;
    	Node<T> _prev = null;
    	T       _item = null;
    	
    	Node(T t)
    	{
    		_item = t;
    	}
    	T getItem()
    	{
    		return _item;
    	}
    	Node<T> getNext()
    	{
    		return _next;
    	}
    	void setNext(Node<T> n)
    	{
    		_next = n;
    	}
    	Node<T> getPrev()
    	{
    		return _prev;
    	}
    	void setPrev(Node<T> p)
    	{
    		_prev = p;
    	}
    }
}
