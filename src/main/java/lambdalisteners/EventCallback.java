package lambdalisteners;

public interface EventCallback<T> {

	void call(T arg);
}
