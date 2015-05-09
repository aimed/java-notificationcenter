package net.norocketlab.java.notificationcenter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Central Notification dispatcher
 * Allows us to notify listeners without having to write a lot of boilerplate code
 * that keeps a list of listeners, adds a listeners to them or removes a listener.
 * 
 * For a simple usage example look below.
 * 
 * NotificationCenter also allows you to listen to notifications, that are bound to 
 * a specific object. This means, that the listener will only get notified, if the
 * notification was posted with the same object. For an example have a look at the
 * Testcase.
 * 
 * @author Maximilian
 */
public class NotificationCenter {	
	/**
	 * A that represents notificationtypes and the concurrent listeners.
	 * The Keys represent the notificationtype as a class. 
	 * The Values represent a list of notificationlisteners for that specific notification
	 */
	private Map<Class<?>,List<Object>> events = new HashMap<Class<?>,List<Object>>();
	
	/**
	 * Allows us to safely remove listeners within notification callbacks
	 */
	private boolean isNotifying = false;
	
	/**
	 * These listeners will be removed from the central after the notification process ended
	 */
	private LinkedList<NotificationListener<?>> removeQueue = new LinkedList<NotificationListener<?>>();
	
	/**
	 * Adds a new listener
	 * @param listener
	 */
	public NotificationListener<?> addListener (NotificationListener<?> listener) 
	{
		return addListener(listener, null);
	}
	
	/**
	 * Adds a new listener
	 * @param listener
	 * @param boundTo  The object the listener will be bound to
	 */
	public NotificationListener<?> addListener (NotificationListener<?> listener, Object boundTo) 
	{
		Class<?> type = listener.getNotificationType();
		List<Object> listeners = events.get(type);
		
		// There are no listeners present, so we need to create a new list
		if (listeners == null)
		{
			listeners = new LinkedList<Object>();
			events.put(type, listeners);
		}
		
		// binds the listeners to the specified object
		listener.setBoundTo(boundTo);
		
		// adds the listener to the list of listeners
		listeners.add(listener);
		return listener;
	}
	
	/**
	 * Removes all instances of the listener
	 * @param listener
	 */
	public void removeListener (NotificationListener<?> listener)
	{
		removeListener(listener, null);
	}
	
	/**
	 * Removes all instances of the listener that are bound to the object or null,
	 * or all instances of the listeners, if boundTo is null.
	 * @param listener
	 * @param boundTo
	 */
	public void removeListener (NotificationListener<?> listener, Object boundTo)
	{
		// ensure we want to remove this listener
		if (listener.getBoundTo() != null && listener.getBoundTo() != boundTo) {
			return;
		}
		
		// don't remove listeners while notifications are beeing posted
		if (isNotifying)
		{
			removeQueue.add(listener);
			return;
		}
		
		Class<?> type = listener.getNotificationType();
		List<Object> listeners = events.get(type);
		
		if (listeners != null)
		{
			listeners.remove(listener);
			
			// remove list once it's empty
			if (listeners.isEmpty())
			{
				events.remove(type);
			}
		}
	}
	
	/**
	 * Triggers a new event
	 * @param notification The NotificationType
	 */
	public <NOTIFICATION extends Notification> void postNotification (NOTIFICATION notification)
	{
		postNotification(notification, null);
	}
	
	/**
	 * Triggers a new event
	 * @param notification The NotificationType
	 * @param object       The Object
	 */
	public <NOTIFICATION extends Notification> void postNotification (NOTIFICATION notification, Object object)
	{
		isNotifying = true;
		Class<?> type = notification.getClass();
		List<Object> listeners = events.get(type);
		
		if (listeners != null)
		{
			for (Object listener : listeners)
			{
				// this actually is typesafe
				@SuppressWarnings("unchecked")
				NotificationListener<NOTIFICATION> castedListener = (NotificationListener<NOTIFICATION>)listener;
				
				// only notify listeners, if they are bound to the object
				if (castedListener.getBoundTo() == object || castedListener.getBoundTo() == null)
				{
					(castedListener).onNotification(notification);
				}
			}
		}
		
		// notification process ended. free central and remove queued listeners
		isNotifying = false;
		emptyRemoveQueue();
	}
	
	/**
	 * Removes all listeners from the removequeue and empties the remove queue
	 */
	private void emptyRemoveQueue () {
		while (!removeQueue.isEmpty())
		{
			NotificationListener<?> listener = removeQueue.removeLast();
			removeListener(listener);
		}
	}
	
	// --- Static ---
	private static final NotificationCenter DEFAULT_CENTER = new NotificationCenter();

	/**
	 * Returns a default center
	 * @return
	 */
	public static NotificationCenter defaultCenter () 
	{
		return DEFAULT_CENTER;
	}
}
