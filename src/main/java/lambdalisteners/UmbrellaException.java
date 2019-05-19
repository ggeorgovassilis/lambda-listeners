package lambdalisteners;

import java.util.Set;

public class UmbrellaException extends RuntimeException{

	Set<Throwable> causes;
	
	public UmbrellaException(Set<Throwable> causes) {
		this.causes = causes;
	}
	
	Set<Throwable> getCauses(){
		return causes;
	}
}
