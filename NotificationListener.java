package net.norocketlab.java.notificationcenter;

import java.lang.ref.WeakReference;

/**
 * Implements a generic notification listeners
 * @author Maximilian
 *
 * @param <NOTIFICATION> The Notification to be expected
 */
public abstract class NotificationListener<NOTIFICATION extends Notification> {
	/**
	 * This listener will listen to notifications of this type
	 */
	private Class<NOTIFICATION> notificationType;
	
	/**
	 * This listener will only listen to notifications associated with this object
	 * If this is null, the listener will respond to all notifications of it's type.
	 */
	private WeakReference<Object> boundTo = null;
	
	/**
	 * Constructor
	 * @param notificationType Notification to listen to
	 */
	public NotificationListener (Class<NOTIFICATION> notificationType)
	{
		this.notificationType = notificationType;
	}
	
	/**
	 * The Notification this listener listens to
	 * @return
	 */
	public Class<NOTIFICATION> getNotificationType ()
	{
		return notificationType;
	}
	
	/**
	 * The Object this listeners is bound to
	 * @return
	 */
	public Object getBoundTo () 
	{
		return boundTo.get();
	}
	
	/**
	 * The Object this listeners is bound to
	 * @param object
	 */
	public void setBoundTo (Object object)
	{
		this.boundTo = new WeakReference<Object>(object);
	}
	
	/**
	 * This method will be called once the notification is posted
	 * @param notification The notification.
	 */
	public abstract void onNotification (NOTIFICATION notification);
}
