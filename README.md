# java-notificationcenter
A helper library to unify and centralize events and eventlisteners in java. Inspired by the Cocoa NSNotificationCenter.

```java
NotificationCenter nc = NotificationCenter.defaultCenter();
nc.addListener(new NotificationListener<SomeNotification>(SomeNotification.class) {
		@Override
		public void onNotification(SomeNotification notification) {				
		}
});
nc.postNotification(new SomeNotification(null, 0, 0));
	
```