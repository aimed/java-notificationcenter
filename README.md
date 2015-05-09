# java-notificationcenter
A helper library to unify and centralize events and eventlisteners in java. Inspired by the Cocoa NSNotificationCenter.

```java
NotificationCenter nc = NotificationCenter.defaultCenter();
nc.addListener(new NotificationListener<StartNewGameNotification>(StartNewGameNotification.class) {
		@Override
		public void onNotification(StartNewGameNotification notification) {				
		}
});
nc.postNotification(new StartParameter(null, 0, 0));
	
```