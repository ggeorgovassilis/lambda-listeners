package lambdalisteners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class EventBus {

	Map<Type<? extends BaseListener>, List<? extends BaseListener>> listeners = new HashMap<Type<? extends BaseListener>, List<? extends BaseListener>>();

	public <T extends BaseListener> void addListener(Type<T> type, T listener) {
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) listeners.get(type);
		if (list == null) {
			list = new ArrayList<T>();
			listeners.put(type, list);
		}
		if (list.contains(listener))
			return;
		list.add(listener);
	}

	public <T extends BaseListener> void removeListener(Type<T> type, T listener) {
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) listeners.get(type);
		if (list != null)
			list.remove(listener);
	}

	public <T extends BaseListener> void removeListener(T listener) {
		for (Type<?> type : listeners.keySet()) {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) listeners.get(type);
			if (list != null)
				list.remove(listener);
		}
	}

	public <T extends BaseListener> void fireEvent(Type<T> type, EventCallback<T> callback) {
		@SuppressWarnings("unchecked")
		List<T> protoList = (List<T>) listeners.get(type);
		if (protoList == null)
			return;
		List<T> copy = new ArrayList<T>(protoList);
		Set<Throwable> errors = null;
		for (T h : copy)
			try {
				callback.call(h);
			} catch (Throwable e) {
				if (errors == null)
					errors = new HashSet<Throwable>();
				errors.add(e);
			}
		if (errors!=null)
			throw new UmbrellaException(errors);
	}

}
