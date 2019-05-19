# Simplifying event listeners with Java lambdas

The introduction of [lambdas](https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html) to Java 8 allow for much more compact notations when writing typical callback code, but combined with [generics](https://docs.oracle.com/javase/tutorial/java/generics/index.html) they allow for genuinely new ways to program which weren't possible before.

This demo shows a simpler way to implement the [observer](https://en.wikipedia.org/wiki/Observer_pattern) pattern also known as event listeners.

## The old way: events and listeners

This recipe consists of: events, listener interfaces, listener implementations and an event bus.

```java
class DepositEvent{

	Date date;
	int amount;
	String source;
	
	...
}



interface AccountListener{

	void onDeposit(DepositEvent event);
}



...

AccountListener listener = ...;
eventBus.addListener(DepositEvent.class, listener);

```

That's ok for a handful events, but writing complex applications with tens or even hundreds of event types can become cumbersome.
This way communication between components essentially consists of these steps:

#### observable gathers data to send to listener

```java
Date depositDate = new Date("02/03/2017");
int depositAmount = 150;
String source = "ATM Times Square";
```

#### observable packages data into an event
```java
DepositEvent event = new DepositEvent(depositDate, depositAmount, source);
```

### observable broadcasts event over an event bus
```java
eventBus.fireEvent(event);
```

### event bus locates applicable observers
```java
List<AccountListener> listeners = findListenersFor(event);
```


### event bus invokes receive method on observers with event argument
```java
for (AccountListener l:listeners)
	l.onDepositEvent(event);
```

### listener unpacks event

```java
Date depositDate = event.depositDate;
int amount = event.amount;
String source = event.source;
```

... and then does something with that data.


## The new way: lamdas and generics

Imagine the observable invokes directly the observer receive method with the data it expects, skipping the package/unpackage event
step. And now imagine that observer is a proxy which invokes the same method on all observer instances:


```java
proxyObserver.method(arg1, arg2, arg3);//{

	observer1.method(arg1, arg2, arg3);
	observer2.method(arg1, arg2, arg3);
	observer3.method(arg1, arg2, arg3);

//}
	...
```

Java [dynamic proxies](https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html) can do that, but some other
languages or environments (e.g. [GWT](http://www.gwtproject.org/)) can not. A similar effect can be, however, achieved with lambdas:

```java
eventBus.fireEvent(Types.transactionEvents, (l)->l.deposit(depositDate, 150, "ATM"));
```
Here, `l` is the lambda notation of an observer/listener interface. The compiler cannot know the type of l, other than it is
a generic listener interface, unless we bind it with a template. That's what `transactionEvents` does. It isn't really an event in the usual sense: it is a parametrised singleton (`final static`) which binds its type on the lambda `l` so that the
compiler recognises it as a specific listener interface. 

## Is the new way better?

For its narrow use case (invoking a method with immutable arguments on multiple objects) this way offers a compact notation
with reduced boiler plate. It should be mentioned, however, that this isn't a 100% functional equivalent to the event listener
pattern, as events can be seen as messages which encapsulate and serialise data from the sender to the recipient. If the sender
and recipient don't reside in the same JVM or are asynchronously coupled, the lambda/generics pattern won't work.