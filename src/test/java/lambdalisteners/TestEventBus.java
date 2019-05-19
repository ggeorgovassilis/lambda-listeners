package lambdalisteners;

import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
public class TestEventBus {

	EventBus bus;
	
	@Before
	public void setup() {
		bus = new EventBus();
	}
	
	/**
	 * Verifies that an event is received by all registered listeners
	 */
	@Test
	public void test() {
		AccountTransactionListener transactionListener1 = mock(AccountTransactionListener.class);
		AccountTransactionListener transactionListener2 = mock(AccountTransactionListener.class);
		bus.addListener(Types.transactionEvents, transactionListener1);
		bus.addListener(Types.transactionEvents, transactionListener2);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1988);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date depositDate = cal.getTime();

		cal.set(Calendar.YEAR, 1988);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		Date withdrawDate = cal.getTime();
		
		bus.fireEvent(Types.transactionEvents, (l)->l.deposit(depositDate, 150, "ATM"));
		
		verify(transactionListener1).deposit(depositDate, 150, "ATM");
		verify(transactionListener2).deposit(depositDate, 150, "ATM");

		bus.fireEvent(Types.transactionEvents, (l)->l.withdraw(withdrawDate, 50));
		verify(transactionListener1).withdraw(withdrawDate, 50);
		verify(transactionListener2).withdraw(withdrawDate, 50);
	}
}
