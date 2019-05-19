package lambdalisteners;

import java.util.Date;

public interface AccountTransactionListener extends BaseListener{

	void deposit(Date date, int amount, String source);
	void withdraw(Date date, int amount);
	
}
