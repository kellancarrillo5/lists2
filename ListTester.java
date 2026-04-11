import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList.
 * This is a set of black box tests that should work for any implementation
 * of this interface.
 * 
 * NOTE: One example test is given for each interface method using a new list to
 * get you started.
 * 
 * @author mvail, mhthomas, awinters
 */
@SuppressWarnings("deprecated")
public class ListTester {
	// possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList
	};

	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.doubleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement,
		ConcurrentModification, UnsupportedOperation,
		NoException, UnexpectedException,
		True, False, Pass, Fail,
		MatchingValue,
		ValidString
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = 1;
	private static final Integer ELEMENT_B = 2;
	private static final Integer ELEMENT_C = 3;
	private static final Integer ELEMENT_D = 4;
	private static final Integer ELEMENT_X = -1;// element that should appear in no lists
	private static final Integer ELEMENT_Z = -2;// element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; // initialized in constructor

	// tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	// control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 * -a : print results from all tests (default is to print failed tests, only)
	 * -s : hide Strings from toString() tests
	 * -m : hide section summaries in output
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();
	}

	/**
	 * tester constructor
	 * 
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
			case doubleLinkedList:
				SUPPORTS_LIST_ITERATOR = true;
				break;
			default:
				SUPPORTS_LIST_ITERATOR = false;
				break;
		}
	}

	/**
	 * Print test results in a consistent format
	 * 
	 * @param testDesc description of the test
	 * @param result   indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) {
			passes++;
		} else {
			failures++;
		}
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes * 100.0 / totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; // reset for next section
		secPasses = passes;
		secFails = failures;
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes * 100.0 / totalRun, failures);
	}

	/////////////////////
	// XXX runTests()
	/////////////////////

	/**
	 * Run tests to confirm required functionality from list constructors and
	 * methods
	 */
	private void runTests() {
		// Possible list contents after a scenario has been set up
		Integer[] LIST_A = { ELEMENT_A };
		String STRING_A = "A";
		Integer[] LIST_B = { ELEMENT_B };
		String STRING_B = "B";
		Integer[] LIST_BA = { ELEMENT_B, ELEMENT_A };
		String STRING_BA = "BA";
		Integer[] LIST_AB = { ELEMENT_A, ELEMENT_B };
		String STRING_AB = "AB";
		Integer[] LIST_CB = { ELEMENT_C, ELEMENT_B };
		String STRING_CB = "CB";
		Integer[] LIST_CA = { ELEMENT_C, ELEMENT_A };
		String STRING_CA = "CA";
		Integer[] LIST_AC = { ELEMENT_A, ELEMENT_C };
		String STRING_AC = "AC";
		Integer[] LIST_CAB = { ELEMENT_C, ELEMENT_A, ELEMENT_B };
		String STRING_CAB = "CAB";
		Integer[] LIST_DAB = { ELEMENT_D, ELEMENT_A, ELEMENT_B };
		String STRING_DAB = "DAB";
		Integer[] LIST_CDB = { ELEMENT_C, ELEMENT_D, ELEMENT_B };
		String STRING_CDB = "CDB";
		Integer[] LIST_CAD = { ELEMENT_C, ELEMENT_A, ELEMENT_D };
		String STRING_CAD = "CAD";
		Integer[] LIST_ABC = { ELEMENT_A, ELEMENT_B, ELEMENT_C };
		String STRING_ABC = "ABC";
		Integer[] LIST_ACB = { ELEMENT_A, ELEMENT_C, ELEMENT_B };
		String STRING_ACB = "ACB";

		// newly constructed empty list
		testEmptyList(newList, "newList");
		// empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addToRearA_A, "emptyList_addToRearA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addA_A, "emptyList_addA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_add0A_A, "emptyList_add0A_A", LIST_A, STRING_A);
		// 1-element to empty list
		testEmptyList(A_removeFirst_emptyList, "A_removeFirst_emptyList");
		testEmptyList(A_removeLast_emptyList, "A_removeLast_emptyList");
		testEmptyList(A_removeA_emptyList, "A_removeA_emptyList");
		testEmptyList(A_remove0_emptyList, "A_remove0_emptyList");
		// 1-element to 2-element
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_addToRearB_AB, "A_addToRearB_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_addAfterBA_AB, "A_addAfterBA_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_addB_AB, "A_addB_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_add0B_BA, "A_ad0B_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_add1B_AB, "A_add1B_AB", LIST_AB, STRING_AB);
		// 1-element to changed 1-element via set()
		testSingleElementList(A_set0B_B, "A_set0B_B", LIST_B, STRING_B);
		// 2-element to 1-element
		testSingleElementList(AB_removeLast_A, "AB_removeLast_A", LIST_A, STRING_A);
		testSingleElementList(BA_removeFirst_A, "BA_removeFirst_A", LIST_A, STRING_A);
		testSingleElementList(AB_removeFirst_B, "AB_removeFirst_B", LIST_B, STRING_B);
		testSingleElementList(AB_removeA_B, "AB_removeA_B", LIST_B, STRING_B);
		testSingleElementList(AB_removeB_A, "AB_removeB_A", LIST_A, STRING_A);
		testSingleElementList(AB_remove0_B, "AB_remove0_B", LIST_B, STRING_B);
		testSingleElementList(AB_remove1_A, "AB_remove1_A", LIST_A, STRING_A);
		testSingleElementList(BA_removeLast_B, "BA_removeLast_B", LIST_B, STRING_B);
		testSingleElementList(BA_removeA_B, "BA_removeA_B", LIST_B, STRING_B);
		testSingleElementList(BA_removeB_A, "BA_removeB_A", LIST_A, STRING_A);
		testSingleElementList(BA_remove0_A, "BA_remove0_A", LIST_A, STRING_A);
		testSingleElementList(BA_remove1_B, "BA_remove1_B", LIST_B, STRING_B);
		// 2-element to 3-element
		testThreeElementList(AB_addToFrontC_CAB, "AB_addToFrontC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_addToRearC_ABC, "AB_addToRearC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_addAfterBC_ABC, "AB_addAfterBC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_add1C_ACB, "AB_add1C_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(AB_add2C_ABC, "AB_add2C_ABC", LIST_ABC, STRING_ABC);
		// 2-element to changed 2-element via set()
		testTwoElementList(AB_set0C_CB, "AB_set0C_CB", LIST_CB, STRING_CB);
		testTwoElementList(AB_set1C_AC, "AB_set1C_AC", LIST_AC, STRING_AC);
		testTwoElementList(BA_set0C_CA, "BA_set0C_CA", LIST_CA, STRING_CA);
		// 3-element to 2-element
		testTwoElementList(CAB_removeFirst_AB, "CAB_removeFirst_AB", LIST_AB, STRING_AB);
		testTwoElementList(CAB_removeLast_CA, "CAB_removeLast_CA", LIST_CA, STRING_CA);
		testTwoElementList(CAB_removeA_CB, "CAB_removeA_CB", LIST_CB, STRING_CB);
		testTwoElementList(CAB_removeC_AB, "CAB_removeC_AB", LIST_AB, STRING_AB);
		testTwoElementList(CAB_removeB_CA, "CAB_removeB_CA", LIST_CA, STRING_CA);
		testTwoElementList(CAB_remove0_AB, "CAB_remove0_AB", LIST_AB, STRING_AB);
		testTwoElementList(CAB_remove1_CB, "CAB_remove1_CB", LIST_CB, STRING_CB);
		testTwoElementList(CAB_remove2_CA, "CAB_remove2_CA", LIST_CA, STRING_CA);
		// 3-element to changed 3-element via set()
		testThreeElementList(CAB_set0D_DAB, "CAB_set0D_DAB", LIST_DAB, STRING_DAB);
		testThreeElementList(CAB_set1D_CDB, "CAB_set1D_CDB", LIST_CDB, STRING_CDB);
		testThreeElementList(CAB_set2D_CAD, "CAB_set2D_CAD", LIST_CAD, STRING_CAD);
		// Iterator remove scenerios (arrayList HW 44-49)
		testEmptyList(A_iterRemoveA_empty, "A_iterRemoveA_empty");
		testSingleElementList(AB_iterRemoveA_B, "AB_iterRemoveA_B", LIST_B, STRING_B);
		testSingleElementList(AB_iterRemoveB_A, "AB_iterRemoveB_A", LIST_A, STRING_A);
		testTwoElementList(CAB_iterRemoveC_AB, "CAB_iterRemoveC_AB", LIST_AB, STRING_AB);
		testTwoElementList(CAB_iterRemoveA_CB, "CAB_iterRemoveA_CB", LIST_CB, STRING_CB);
		testTwoElementList(CAB_iterRemoveB_CA, "CAB_iterRemoveB_CA", LIST_CA, STRING_CA);
		// ListIterator remove() scenarios
		testEmptyList(A_listIterRemoveA_empty, "A_listIterRemoveA_empty");
		testSingleElementList(AB_listIterRemoveA_B, "AB_listIterRemoveA_B", LIST_B, STRING_B);
		testSingleElementList(AB_listIterRemoveB_A, "AB_listIterRemoveB_A", LIST_A, STRING_A);
		testTwoElementList(CAB_listIterRemoveC_AB, "CAB_listIterRemoveC_AB", LIST_AB, STRING_AB);
		testTwoElementList(CAB_listIterRemoveA_CB, "CAB_listIterRemoveA_CB", LIST_CB, STRING_CB);
		testTwoElementList(CAB_listIterRemoveB_CA, "CAB_listIterRemoveB_CA", LIST_CA, STRING_CA);
		testSingleElementList(AB_listIterPrevRemoveB_A, "AB_listIterPrevRemoveB_A", LIST_A, STRING_A);
		testTwoElementList(CAB_listIterPrevRemoveA_CB, "CAB_listIterPrevRemoveA_CB", LIST_CB, STRING_CB);
		// ListIterator set() scenarios
		testSingleElementList(A_listIterSetB_B, "A_listIterSetB_B", LIST_B, STRING_B);
		testTwoElementList(AB_listIterSetC_CB, "AB_listIterSetC_CB", LIST_CB, STRING_CB);
		testTwoElementList(AB_listIterSetC_AC, "AB_listIterSetC_AC", LIST_AC, STRING_AC);
		testThreeElementList(CAB_listIterSetD_CDB, "CAB_listIterSetD_CDB", LIST_CDB, STRING_CDB);
		testThreeElementList(CAB_listIterPrevSetD_CAD, "CAB_listIterPrevSetD_CAD", LIST_CAD, STRING_CAD);
		// ListIterator add() scenarios
		testSingleElementList(emptyList_listIterAddA_A, "emptyList_listIterAddA_A", LIST_A, STRING_A);
		testTwoElementList(A_listIterAddB_BA, "A_listIterAddB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_listIterAddB_AB, "A_listIterAddB_AB", LIST_AB, STRING_AB);
		testThreeElementList(AB_listIterAddC_ACB, "AB_listIterAddC_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(AB_listIterAddC_CAB, "AB_listIterAddC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_listIterAddC_ABC, "AB_listIterAddC_ABC", LIST_ABC, STRING_ABC);
		// Iterator concurrency tests
		test_IterConcurrency();
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: Comment out cases for any implementations not currently available
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
			// case goodList:
			// listToUse = new GoodList<Integer>();
			// break;
			// case badList:
			// listToUse = new BadList<Integer>();
			// break;
			// case arrayList:
			// listToUse = new IUArrayList<Integer>();
			// break;
			// case singleLinkedList:
			// listToUse = new IUSingleLinkedList<Integer>();
			// break;
			case doubleLinkedList:
				listToUse = new IUDoubleLinkedList<Integer>();
				break;
			default:
				listToUse = null;
		}
		return listToUse;
	}

	// The following creates a "lambda" reference that allows us to pass a scenario
	// builder method as an argument. You don't need to worry about how it works -
	// just make sure each scenario building method has a corresponding Scenario
	// assignment statement as in these examples.
	private Scenario<Integer> newList = () -> newList();

	/**
	 * Scenario: empty list -> addToFront(A) -> [A]
	 * 
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.addToFront(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A();

	/**
	 * Scenario: empty list --> addToRear(A) --> [A]
	 * 
	 * @return [A] after addToRear(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToRearA_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.addToRear(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_addToRearA_A = () -> emptyList_addToRearA_A();

	/**
	 * Scenario: empty list --> add(A) --> [A]
	 * 
	 * @return [A] after add(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addA_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.add(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_addA_A = () -> emptyList_addA_A();

	/**
	 * Scenerio: empty list --> add(0,A) --> [A]
	 * 
	 * @return [A] after add(0,A)
	 */
	private IndexedUnsortedList<Integer> emptyList_add0A_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.add(0, ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_add0A_A = () -> emptyList_add0A_A();

	/**
	 * Scenerio: [A] --> removeFirst() --> []
	 * 
	 * @return [] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> A_removeFirst_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> A_removeFirst_emptyList = () -> A_removeFirst_emptyList();

	/**
	 * Scenerio: [A] --> removeLast() --> []
	 * 
	 * @return [] after removeLast()
	 */
	private IndexedUnsortedList<Integer> A_removeLast_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> A_removeLast_emptyList = () -> A_removeLast_emptyList();

	/**
	 * Scenerio: [A] --> remove(A) --> []
	 * 
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.remove(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> A_removeA_emptyList = () -> A_removeA_emptyList();

	/**
	 * Scenerio: [A] --> remove(0) --> []
	 * 
	 * @return [] after remove(0)
	 */
	private IndexedUnsortedList<Integer> A_remove0_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.remove(0);
		return list;
	}

	private Scenario<Integer> A_remove0_emptyList = () -> A_remove0_emptyList();

	/**
	 * Scenario: [A] -> addToFront(B) -> [B,A]
	 * 
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addToFront(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();

	/**
	 * Scenario: [A] --> addToRear(B) --> [A,B]
	 * 
	 * @return [A,B] after addToRear(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addToRear(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();

	/**
	 * Scenario: [A] --> addAfter(B,A) --> [A,B]
	 * 
	 * @return [A,B] after addAfter(B,A)
	 */
	private IndexedUnsortedList<Integer> A_addAfterBA_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addAfter(ELEMENT_B, ELEMENT_A);
		return list;
	}

	private Scenario<Integer> A_addAfterBA_AB = () -> A_addAfterBA_AB();

	/**
	 * Scenario: [A] --> add(B) --> [A,B]
	 * 
	 * @return [A,B] after add(B)
	 */
	private IndexedUnsortedList<Integer> A_addB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.add(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_addB_AB = () -> A_addB_AB();

	/**
	 * Scenario: [A] --> add(0,B) --> [B,A]
	 * 
	 * @return [B,A] after add(0,B)
	 */
	private IndexedUnsortedList<Integer> A_add0B_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.add(0, ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_add0B_BA = () -> A_add0B_BA();

	/**
	 * Scenario: [A] --> add(1,B) --> [A,B]
	 * 
	 * @return [A,B] after add(1,B)
	 */
	private IndexedUnsortedList<Integer> A_add1B_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.add(1, ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_add1B_AB = () -> A_add1B_AB();

	/**
	 * Scenario: [A] --> set(0,B) --> [B]
	 * 
	 * @return [B] after set(0,B)
	 */
	private IndexedUnsortedList<Integer> A_set0B_B() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.set(0, ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_set0B_B = () -> A_set0B_B();

	/**
	 * [A,B] --> removeFirst() --> [B]
	 * 
	 * @return [B] after removerFirst()
	 */
	private IndexedUnsortedList<Integer> BA_removeFirst_A() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> BA_removeFirst_A = () -> BA_removeFirst_A();

	/**
	 * Scenario: [A,B] --> addToFront(C) --> [C,A,B]
	 * 
	 * @return [C,A,B] after addToFront(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToFront(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();

	/**
	 * Scenario: [A,B] --> set(0,C) --> [C,B]
	 * 
	 * @return [C,B] after set(0,C) on [A,B]
	 */
	private IndexedUnsortedList<Integer> AB_set0C_CB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.set(0, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_set0C_CB = () -> AB_set0C_CB();

	/**
	 * Scenario: [A,B,C] --> removeFirst() --> [B,C]
	 * 
	 * @return [B,C] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> CAB_removeFirst_AB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> CAB_removeFirst_AB = () -> CAB_removeFirst_AB();

	/**
	 * Scenario: [C,A,B] --> removeLast() --> [C,A]
	 * 
	 * @return [C,A] after removeLast()
	 */
	private IndexedUnsortedList<Integer> CAB_removeLast_CA() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> CAB_removeLast_CA = () -> CAB_removeLast_CA();

	/**
	 * Scenario: [C,A,B] --> remove(A) --> [C,B]
	 * 
	 * @return [C,B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> CAB_removeA_CB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> CAB_removeA_CB = () -> CAB_removeA_CB();

	/**
	 * Scenario: [A] -> iter.next(), iter.remove() -> []
	 * 
	 * @return [] after iterator removes A
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveA_empty() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> A_iterRemoveA_empty = () -> A_iterRemoveA_empty();

	/**
	 * Scenario: [A,B] -> iter.next()==A, iter.remove() -> [B]
	 * 
	 * @return [B] after iterator removes A
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveA_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> AB_iterRemoveA_B = () -> AB_iterRemoveA_B();

	/**
	 * Scenario: [A,B] -> iter.next()==A, iter.next()==B, iter.remove() -> [A]
	 * 
	 * @return [A] after iterator removes B
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveB_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> AB_iterRemoveB_A = () -> AB_iterRemoveB_A();

	/**
	 * Scenario: [C,A,B] -> iter.next()==C, iter.remove() -> [A,B]
	 * 
	 * @return [A,B] after iterator removes C
	 */
	private IndexedUnsortedList<Integer> CAB_iterRemoveC_AB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_iterRemoveC_AB = () -> CAB_iterRemoveC_AB();

	/**
	 * Scenario: [C,A,B] -> iter through C,A, iter.remove()==A -> [C,B]
	 * 
	 * @return [C,B] after iterator removes A
	 */
	private IndexedUnsortedList<Integer> CAB_iterRemoveA_CB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_iterRemoveA_CB = () -> CAB_iterRemoveA_CB();

	/**
	 * Scenario: [C,A,B] -> iter through C,A,B, iter.remove()==B -> [C,A]
	 * 
	 * @return [C,A] after iterator removes B
	 */
	private IndexedUnsortedList<Integer> CAB_iterRemoveB_CA() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_iterRemoveB_CA = () -> CAB_iterRemoveB_CA();

	/**
	 * Scenario: [C,A,B] -> set(1,D) -> [C,D,B]
	 * 
	 * @return [C,D,B] after set(1,D)
	 */
	private IndexedUnsortedList<Integer> CAB_set1D_CDB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.set(1, ELEMENT_D);
		return list;
	}

	private Scenario<Integer> CAB_set1D_CDB = () -> CAB_set1D_CDB();

	/**
	 * Scenario: [C,A,B] -> set(2,D) -> [C,A,D]
	 * 
	 * @return [C,A,D] after set(2,D)
	 */
	private IndexedUnsortedList<Integer> CAB_set2D_CAD() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.set(2, ELEMENT_D);
		return list;
	}

	private Scenario<Integer> CAB_set2D_CAD = () -> CAB_set2D_CAD();

	/**
	 * Scenario: [C,A,B] -> set(0,D) -> [D,A,B]
	 * 
	 * @return [D,A,B] after set(0,D)
	 */
	private IndexedUnsortedList<Integer> CAB_set0D_DAB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.set(0, ELEMENT_D);
		return list;
	}

	private Scenario<Integer> CAB_set0D_DAB = () -> CAB_set0D_DAB();

	/**
	 * Scenario: [A,B] -> addToRear(C) -> [A,B,C]
	 * 
	 * @return [A,B,C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToRear(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addToRearC_ABC = () -> AB_addToRearC_ABC();

	/**
	 * Scenario: [A,B] -> addAfter(C,B) -> [A,B,C]
	 * 
	 * @return [A,B,C] after addAfter(C,B)
	 */
	private IndexedUnsortedList<Integer> AB_addAfterBC_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_B);
		return list;
	}

	private Scenario<Integer> AB_addAfterBC_ABC = () -> AB_addAfterBC_ABC();

	/**
	 * Scenario: [A,B] -> add(1,C) -> [A,C,B]
	 * 
	 * @return [A,C,B] after add(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_add1C_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(1, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_add1C_ACB = () -> AB_add1C_ACB();

	/**
	 * Scenario: [A,B] -> add(2,C) -> [A,B,C]
	 * 
	 * @return [A,B,C] after add(2,C)
	 */
	private IndexedUnsortedList<Integer> AB_add2C_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(2, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_add2C_ABC = () -> AB_add2C_ABC();

	/**
	 * Scenario: [C,A,B] -> remove(C) -> [A,B]
	 * 
	 * @return [A,B] after remove(C)
	 */
	private IndexedUnsortedList<Integer> CAB_removeC_AB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> CAB_removeC_AB = () -> CAB_removeC_AB();

	/**
	 * Scenario: [C,A,B] -> remove(B) -> [C,A]
	 * 
	 * @return [C,A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> CAB_removeB_CA() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> CAB_removeB_CA = () -> CAB_removeB_CA();

	/**
	 * Scenario: [C,A,B] -> remove(0) -> [A,B]
	 * 
	 * @return [A,B] after remove(0)
	 */
	private IndexedUnsortedList<Integer> CAB_remove0_AB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(0);
		return list;
	}

	private Scenario<Integer> CAB_remove0_AB = () -> CAB_remove0_AB();

	/**
	 * Scenario: [C,A,B] -> remove(1) -> [C,B]
	 * 
	 * @return [C,B] after remove(1)
	 */
	private IndexedUnsortedList<Integer> CAB_remove1_CB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(1);
		return list;
	}

	private Scenario<Integer> CAB_remove1_CB = () -> CAB_remove1_CB();

	/**
	 * Scenario: [C,A,B] -> remove(2) -> [C,A]
	 * 
	 * @return [C,A] after remove(2)
	 */
	private IndexedUnsortedList<Integer> CAB_remove2_CA() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		list.remove(2);
		return list;
	}

	private Scenario<Integer> CAB_remove2_CA = () -> CAB_remove2_CA();

	/**
	 * Scenario: [A,B] -> removeFirst() -> [B]
	 * 
	 * @return [B] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> AB_removeFirst_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> AB_removeFirst_B = () -> AB_removeFirst_B();

	/**
	 * Scenario: [A,B] -> remove(A) -> [B]
	 * 
	 * @return [B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> AB_removeA_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> AB_removeA_B = () -> AB_removeA_B();

	/**
	 * Scenario: [A,B] -> remove(B) -> [A]
	 * 
	 * @return [A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> AB_removeB_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> AB_removeB_A = () -> AB_removeB_A();

	/**
	 * Scenario: [A,B] -> remove(0) -> [B]
	 * 
	 * @return [B] after remove(0)
	 */
	private IndexedUnsortedList<Integer> AB_remove0_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(0);
		return list;
	}

	private Scenario<Integer> AB_remove0_B = () -> AB_remove0_B();

	/**
	 * Scenario: [A,B] -> remove(1) -> [A]
	 * 
	 * @return [A] after remove(1)
	 */
	private IndexedUnsortedList<Integer> AB_remove1_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(1);
		return list;
	}

	private Scenario<Integer> AB_remove1_A = () -> AB_remove1_A();

	/**
	 * Scenario: [B,A] -> removeLast() -> [B]
	 * 
	 * @return [B] after removeLast()
	 */
	private IndexedUnsortedList<Integer> BA_removeLast_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> BA_removeLast_B = () -> BA_removeLast_B();

	/**
	 * Scenario: [A,B] -> removeLast() -> [A]
	 * 
	 * @return [A] after removeLast()
	 */
	private IndexedUnsortedList<Integer> AB_removeLast_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> AB_removeLast_A = () -> AB_removeLast_A();

	/**
	 * Scenario: [B,A] -> remove(A) -> [B]
	 * 
	 * @return [B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> BA_removeA_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.remove(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> BA_removeA_B = () -> BA_removeA_B();

	/**
	 * Scenario: [B,A] -> remove(B) -> [A]
	 * 
	 * @return [A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> BA_removeB_A() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.remove(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> BA_removeB_A = () -> BA_removeB_A();

	/**
	 * Scenario: [A,B] -> set(1,C) -> [A,C]
	 * 
	 * @return [A,C] after set(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_set1C_AC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.set(1, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_set1C_AC = () -> AB_set1C_AC();

	/**
	 * Scenario: [B,A] -> set(0,C) -> [C,A]
	 * 
	 * @return [C,A] after set(0,C)
	 */
	private IndexedUnsortedList<Integer> BA_set0C_CA() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.set(0, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> BA_set0C_CA = () -> BA_set0C_CA();

	/**
	 * Scenario: [B,A] -> remove(0) -> [A]
	 * 
	 * @return [A] after remove(0)
	 */
	private IndexedUnsortedList<Integer> BA_remove0_A() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.remove(0);
		return list;
	}

	private Scenario<Integer> BA_remove0_A = () -> BA_remove0_A();

	/**
	 * Scenario: [B,A] -> remove(1) -> [B]
	 * 
	 * @return [B] after remove(1)
	 */
	private IndexedUnsortedList<Integer> BA_remove1_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.remove(1);
		return list;
	}

	private Scenario<Integer> BA_remove1_B = () -> BA_remove1_B();

	/**
	 * Scenario: [A] -> listIter.next()==A, listIter.remove() -> []
	 *
	 * @return [] after ListIterator removes A
	 */
	private IndexedUnsortedList<Integer> A_listIterRemoveA_empty() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> A_listIterRemoveA_empty = () -> A_listIterRemoveA_empty();

	/**
	 * Scenario: [A,B] -> listIter.next()==A, listIter.remove() -> [B]
	 *
	 * @return [B] after ListIterator removes A (front)
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveA_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> AB_listIterRemoveA_B = () -> AB_listIterRemoveA_B();

	/**
	 * Scenario: [A,B] -> listIter.next()==A, listIter.next()==B, listIter.remove()
	 * -> [A]
	 *
	 * @return [A] after ListIterator removes B (rear)
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveB_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> AB_listIterRemoveB_A = () -> AB_listIterRemoveB_A();

	/**
	 * Scenario: [C,A,B] -> listIter.next()==C, listIter.remove() -> [A,B]
	 *
	 * @return [A,B] after ListIterator removes C (front of 3-element list)
	 */
	private IndexedUnsortedList<Integer> CAB_listIterRemoveC_AB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_listIterRemoveC_AB = () -> CAB_listIterRemoveC_AB();

	/**
	 * Scenario: [C,A,B] -> next()==C, next()==A, listIter.remove() -> [C,B]
	 *
	 * @return [C,B] after ListIterator removes A (middle of 3-element list)
	 */
	private IndexedUnsortedList<Integer> CAB_listIterRemoveA_CB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_listIterRemoveA_CB = () -> CAB_listIterRemoveA_CB();

	/**
	 * Scenario: [C,A,B] -> next() x3, listIter.remove() -> [C,A]
	 *
	 * @return [C,A] after ListIterator removes B (rear of 3-element list)
	 */
	private IndexedUnsortedList<Integer> CAB_listIterRemoveB_CA() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_listIterRemoveB_CA = () -> CAB_listIterRemoveB_CA();

	/**
	 * Scenario: [A,B] -> listIter past end, previous()==B, listIter.remove() -> [A]
	 * Tests remove() after previous() instead of next().
	 *
	 * @return [A] after ListIterator removes B via previous
	 */
	private IndexedUnsortedList<Integer> AB_listIterPrevRemoveB_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator(2); // cursor at end
		it.previous(); // returns B
		it.remove();
		return list;
	}

	private Scenario<Integer> AB_listIterPrevRemoveB_A = () -> AB_listIterPrevRemoveB_A();

	/**
	 * Scenario: [C,A,B] -> listIter past end, previous()==B, previous()==A,
	 * remove() -> [C,B]
	 * Tests remove() after two previous() calls (removes middle element).
	 *
	 * @return [C,B] after ListIterator removes A via previous
	 */
	private IndexedUnsortedList<Integer> CAB_listIterPrevRemoveA_CB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator(3); // cursor at end
		it.previous(); // returns B
		it.previous(); // returns A
		it.remove();
		return list;
	}

	private Scenario<Integer> CAB_listIterPrevRemoveA_CB = () -> CAB_listIterPrevRemoveA_CB();

	/**
	 * Scenario: [A] -> listIter.next()==A, listIter.set(B) -> [B]
	 *
	 * @return [B] after ListIterator sets A to B
	 */
	private IndexedUnsortedList<Integer> A_listIterSetB_B() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_listIterSetB_B = () -> A_listIterSetB_B();

	/**
	 * Scenario: [A,B] -> listIter.next()==A, listIter.set(C) -> [C,B]
	 *
	 * @return [C,B] after ListIterator sets front element to C
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetC_CB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_listIterSetC_CB = () -> AB_listIterSetC_CB();

	/**
	 * Scenario: [A,B] -> next()==A, next()==B, listIter.set(C) -> [A,C]
	 *
	 * @return [A,C] after ListIterator sets rear element to C
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetC_AC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.set(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_listIterSetC_AC = () -> AB_listIterSetC_AC();

	/**
	 * Scenario: [C,A,B] -> next()==C, next()==A, listIter.set(D) -> [C,D,B]
	 *
	 * @return [C,D,B] after ListIterator sets middle element to D
	 */
	private IndexedUnsortedList<Integer> CAB_listIterSetD_CDB() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.set(ELEMENT_D);
		return list;
	}

	private Scenario<Integer> CAB_listIterSetD_CDB = () -> CAB_listIterSetD_CDB();

	/**
	 * Scenario: [C,A,B] -> listIter past end, previous()==B, set(D) -> [C,A,D]
	 * Tests set() after previous().
	 *
	 * @return [C,A,D] after ListIterator sets rear element via previous
	 */
	private IndexedUnsortedList<Integer> CAB_listIterPrevSetD_CAD() {
		IndexedUnsortedList<Integer> list = AB_addToFrontC_CAB();
		ListIterator<Integer> it = list.listIterator(3); // cursor at end
		it.previous(); // returns B
		it.set(ELEMENT_D);
		return list;
	}

	private Scenario<Integer> CAB_listIterPrevSetD_CAD = () -> CAB_listIterPrevSetD_CAD();

	/**
	 * Scenario: [] -> listIter.add(A) -> [A]
	 *
	 * @return [A] after ListIterator adds A to empty list
	 */
	private IndexedUnsortedList<Integer> emptyList_listIterAddA_A() {
		IndexedUnsortedList<Integer> list = newList();
		ListIterator<Integer> it = list.listIterator();
		it.add(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_listIterAddA_A = () -> emptyList_listIterAddA_A();

	/**
	 * Scenario: [A] -> listIter at 0, listIter.add(B) -> [B,A]
	 * Cursor at front; add() prepends B.
	 *
	 * @return [B,A] after ListIterator adds B before A
	 */
	private IndexedUnsortedList<Integer> A_listIterAddB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator(0);
		it.add(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_listIterAddB_BA = () -> A_listIterAddB_BA();

	/**
	 * Scenario: [A] -> listIter at 1 (end), listIter.add(B) -> [A,B]
	 * Cursor at end; add() appends B.
	 *
	 * @return [A,B] after ListIterator adds B after A
	 */
	private IndexedUnsortedList<Integer> A_listIterAddB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator(1);
		it.add(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_listIterAddB_AB = () -> A_listIterAddB_AB();

	/**
	 * Scenario: [A,B] -> listIter.next()==A, listIter.add(C) -> [A,C,B]
	 * Adds C between A and B.
	 *
	 * @return [A,C,B] after ListIterator adds C in the middle
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddC_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.add(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_listIterAddC_ACB = () -> AB_listIterAddC_ACB();

	/**
	 * Scenario: [A,B] -> listIter at 0, listIter.add(C) -> [C,A,B]
	 * Adds C before the front.
	 *
	 * @return [C,A,B] after ListIterator adds C at front
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator(0);
		it.add(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_listIterAddC_CAB = () -> AB_listIterAddC_CAB();

	/**
	 * Scenario: [A,B] -> listIter at end, listIter.add(C) -> [A,B,C]
	 * Adds C after the rear.
	 *
	 * @return [A,B,C] after ListIterator adds C at rear
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddC_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator(2);
		it.add(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_listIterAddC_ABC = () -> AB_listIterAddC_ABC();

	/////////////////////////////////
	// XXX Tests for 0-element list
	/////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in an empty list
	 * 
	 * @param scenario     lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				// double-linked list
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	// XXX Tests for 1-element list
	//////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a single element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1",
					testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				// double-linked list
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove",
						testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)),
								Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(
						listIterAfterRemove(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)),
						Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous",
						testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove",
						testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add",
						testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0Set",
						testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd",
						testListIterAdd(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousSet",
						testListIterSet(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext",
						testIterHasNext(scenario.build().listIterator(1), Result.False));
				printTest(scenarioName + "_testListIter1Next",
						testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter1NextIndex",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove",
						testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set",
						testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	// XXX Tests for 2-element list
	/////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a two-element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// tests for scenarios ending in a 2-element list
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1),
					testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1),
					testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1),
					testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3",
					testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1),
					testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1",
					testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2",
					testRemoveIndex(scenario.build(), 2, null, Result.IndexOutOfBounds));
			// Iterator -newly initialized
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// Iterator -after one next()
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			// Iterator -after one next() then remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// Iterator -after two next()
			printTest(scenarioName + "_iterNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			// Iterator -after two next() then remove()
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				// double-linked list
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));
				// hasNext / hasPrevious at the start (before index 0)
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				// nextIndex / previousIndex at start
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				// next() returns contents[0]
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				// after one next(): hasNext true, hasPrevious true, indices correct
				printTest(scenarioName + "_listIterNext_testHasNext",
						testIterHasNext(listIterAfterNext(scenario.build().listIterator(), 1), Result.True));
				printTest(scenarioName + "_listIterNext_testHasPrevious",
						testListIterHasPrevious(listIterAfterNext(scenario.build().listIterator(), 1), Result.True));
				// after one next(): next() returns contents[1]
				printTest(scenarioName + "_listIterNext_testNext",
						testIterNext(listIterAfterNext(scenario.build().listIterator(), 1), contents[1],
								Result.MatchingValue));
				// after one next(): previous() returns contents[0]
				printTest(scenarioName + "_listIterNext_testPrevious",
						testListIterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), contents[0],
								Result.MatchingValue));
				// after two next(): hasNext false, hasPrevious true, indices correct
				printTest(scenarioName + "_listIterNextNext_testHasNext",
						testIterHasNext(listIterAfterNext(scenario.build().listIterator(), 2), Result.False));
				printTest(scenarioName + "_listIterNextNext_testHasPrevious",
						testListIterHasPrevious(listIterAfterNext(scenario.build().listIterator(), 2), Result.True));
				// after two next(): next() throws NoSuchElement
				printTest(scenarioName + "_listIterNextNext_testNextNoSuch",
						testIterNext(listIterAfterNext(scenario.build().listIterator(), 2), null,
								Result.NoSuchElement));
				// after two next(): previous() returns contents[1]
				printTest(scenarioName + "_listIterNextNext_testPrevious",
						testListIterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), contents[1],
								Result.MatchingValue));
				// previous() before any next() -> NoSuchElement
				printTest(scenarioName + "_testListIterPreviousNoSuch",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				// remove() before any movement -> IllegalState
				printTest(scenarioName + "_testListIterRemoveIllegal",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				// set() before any movement -> IllegalState
				printTest(scenarioName + "_testListIterSetIllegal",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				// after next(): remove() and set() succeed
				printTest(scenarioName + "_listIterNext_testRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_listIterNext_testSet",
						testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X,
								Result.NoException));
				// after next()+previous(): remove() and set() succeed
				printTest(scenarioName + "_listIterNextPrev_testRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_listIterNextPrev_testSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				// add() at various positions succeeds
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_listIterNext_testAdd",
						testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X,
								Result.NoException));
				// after add(): remove() -> IllegalState
				printTest(scenarioName + "_listIterAdd_testRemoveIllegal",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 0), Result.IllegalState));
				// two independent iterators can be created
				printTest(scenarioName + "_testListIterConcurrent",
						testListIterConcurrent(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIter0_1Concurrent",
						testListIterConcurrent(scenario.build(), 0, 1, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	// XXX Tests for 3-element list
	//////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a three-element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// tests for scenarios resulting in a 3-element list
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1),
					testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(2),
					testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1),
					testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(2),
					testContains(scenario.build(), contents[2], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1),
					testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(2),
					testAddAfter(scenario.build(), contents[2], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3",
					testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex4",
					testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet3", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testGet3", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1),
					testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(2),
					testIndexOf(scenario.build(), contents[2], 2));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1",
					testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2",
					testRemoveIndex(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove3",
					testRemoveIndex(scenario.build(), 3, null, Result.IndexOutOfBounds));
			// Iterator -newly initialized
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// Iterator -after one next()
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			// Iterator -after one next() then remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// Iterator - after next(), remove(), next()
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(
					iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), Result.True));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext",
					testIterNext(iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), contents[2],
							Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemoveNext_testIterRemove", testIterRemove(
					iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), Result.NoException));
			// Iterator -after two consecutive next()
			printTest(scenarioName + "_iterNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
			printTest(scenarioName + "_iterNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 2), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			// Iterator -after two next() then remove()
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.True));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			// Iterator - after two next(), remove(), next()
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterHasNext", testIterHasNext(
					iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), 1), Result.False));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterNext", testIterNext(
					iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterRemove", testIterRemove(
					iterAfterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), 1), Result.NoException));
			// Iterator -after three consecutive next()
			printTest(scenarioName + "_iterNextNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 3), Result.False));
			printTest(scenarioName + "_iterNextNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 3), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 3), Result.NoException));
			// Iterator -three next() then remove()
			printTest(scenarioName + "_iterNextNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				// double-linked list
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.NoException));
				printTest(scenarioName + "_testListIter4", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));
				// hasNext / hasPrevious at start
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				// nextIndex / previousIndex at start
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				// next() returns contents[0]
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				// after one next(): hasNext/hasPrevious, indices, next, previous
				printTest(scenarioName + "_listIterNext_testHasNext",
						testIterHasNext(listIterAfterNext(scenario.build().listIterator(), 1), Result.True));
				printTest(scenarioName + "_listIterNext_testHasPrevious",
						testListIterHasPrevious(listIterAfterNext(scenario.build().listIterator(), 1), Result.True));
				printTest(scenarioName + "_listIterNext_testNextIndex",
						testListIterNextIndex(listIterAfterNext(scenario.build().listIterator(), 1), 1,
								Result.MatchingValue));
				// after two next(): hasNext/hasPrevious, indices, next, previous
				printTest(scenarioName + "_listIterNextNext_testHasNext",
						testIterHasNext(listIterAfterNext(scenario.build().listIterator(), 2), Result.True));
				printTest(scenarioName + "_listIterNextNext_testHasPrevious",
						testListIterHasPrevious(listIterAfterNext(scenario.build().listIterator(), 2), Result.True));
				printTest(scenarioName + "_listIterNextNext_testNextIndex",
						testListIterNextIndex(listIterAfterNext(scenario.build().listIterator(), 2), 2,
								Result.MatchingValue));
				// after three next(): hasNext false, hasPrevious true, indices
				printTest(scenarioName + "_listIterNextNextNext_testHasNext",
						testIterHasNext(listIterAfterNext(scenario.build().listIterator(), 3), Result.False));
				printTest(scenarioName + "_listIterNextNextNext_testHasPrevious",
						testListIterHasPrevious(listIterAfterNext(scenario.build().listIterator(), 3), Result.True));
				// after three next(): next() throws NoSuchElement
				printTest(scenarioName + "_listIterNextNextNext_testNextNoSuch",
						testIterNext(listIterAfterNext(scenario.build().listIterator(), 3), null,
								Result.NoSuchElement));
				// after three next(): previous() returns contents[2]
				printTest(scenarioName + "_listIterNextNextNext_testPrevious",
						testListIterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), contents[2],
								Result.MatchingValue));
				// previous() before any next() -> NoSuchElement
				printTest(scenarioName + "_testListIterPreviousNoSuch",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				// remove() / set() before any movement -> IllegalState
				printTest(scenarioName + "_testListIterRemoveIllegal",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterSetIllegal",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				// after one next(): remove() and set() succeed
				printTest(scenarioName + "_listIterNext_testRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_listIterNext_testSet",
						testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X,
								Result.NoException));
				// after next()+previous(): remove() and set() succeed (previous direction)
				printTest(scenarioName + "_listIterNextPrev_testRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_listIterNextPrev_testSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				// add() at front, middle, end succeeds
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_listIterNext_testAdd",
						testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X,
								Result.NoException));
				printTest(scenarioName + "_listIterNextNext_testAdd",
						testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X,
								Result.NoException));
				printTest(scenarioName + "_listIterNextNextNext_testAdd",
						testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 3), ELEMENT_X,
								Result.NoException));
				// after add(): remove() -> IllegalState
				printTest(scenarioName + "_listIterAdd_testRemoveIllegal",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 0), Result.IllegalState));
				// two independent iterators can be created at various positions
				printTest(scenarioName + "_testListIterConcurrent",
						testListIterConcurrent(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIter0_1Concurrent",
						testListIterConcurrent(scenario.build(), 0, 1, Result.NoException));
				printTest(scenarioName + "_testListIter1_3Concurrent",
						testListIterConcurrent(scenario.build(), 1, 3, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/**
	 * Runs removeFirst() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeFirst();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs removeLast() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeLast();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs removeLast() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element        element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(element);
			if (retVal.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs first() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.first();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs last() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.last();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs contains() method on a given list and element and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs isEmpty() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs size() method on a given list and checks result against expectedResult
	 * 
	 * @param list         a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Runs toString() method on given list and attempts to confirm non-default or
	 * empty String
	 * difficult to test - just confirm that default address output has been
	 * overridden
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size() / 2 + 2)) { // elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addToFront() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront", e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addToRear() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addAfter() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element,
			Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs add(int, T) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element,
			Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs add(T) method on a given list and checks result against expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs set(int, T) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs get() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement,
			Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.get(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs remove(index) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement,
			Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs indexOf() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list          a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/**
	 * Runs iterator() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator hasNext() method and checks result against
	 * expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator next() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasNext()
	 * @param expectedValue  the Integer expected from next() or null if an
	 *                       exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.next();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator remove() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs iterator() method twice on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> it2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	// XXX HELPER METHODS FOR TESTING ITERATORS
	// Note: You can create other similar helpers if you want
	// something slightly different.
	//////////////////////////////////////////////////////////

	/**
	 * Helper for testing iterators. Return an Iterator that has been advanced
	 * numCallsToNext times.
	 * 
	 * @param list
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}

	/**
	 * Helper for testing iterators. Return an Iterator that has been advanced
	 * numCallsToNext times.
	 * 
	 * @param iterator       an iterator already positioned
	 * @param numCallsToNext
	 * @return Iterator after numCallsToNext additional next() calls
	 */
	private Iterator<Integer> iterAfterNext(Iterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/**
	 * Helper for testing iterators. Return an Iterator that has had remove() called
	 * once.
	 * 
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/**
	 * Runs listIterator() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator(index) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator's hasPrevious() method and checks result against
	 * expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator previous() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasPrevious()
	 * @param expectedValue  the Integer expected from next() or null if an
	 *                       exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.previous();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator add() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to add()
	 * @param element        new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterAdd(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterAdd", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator set() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to set()
	 * @param element        replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterSet(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator nextIndex() and checks result against expected Result
	 * 
	 * @param iterator       already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator previousIndex() and checks result against expected Result
	 * 
	 * @param iterator       already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex,
			Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator() method twice on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator(index) method twice on a given list and checks result
	 * against expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2,
			Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	// XXX HELPER METHODS FOR TESTING LISTITERATORS
	// Note: You can create other similar helpers if you want
	// something slightly different.
	//////////////////////////////////////////////////////////

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has been
	 * advanced numCallsToNext times.
	 * 
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has been backed
	 * up numCallsToPrevious times.
	 * 
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has had remove()
	 * called once.
	 * 
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> it1 = list.iterator();
			Iterator<Integer> it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(it2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	// Will add tests for double-linked list
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			// double-linked list
			// Two independent ListIterators on the same list do not affect each other unless one of them modifies the list.
			printTest("emptyList_testConcurrentListIter",
					testListIterConcurrent(newList(), Result.NoException));
			printTest("A_testConcurrentListIter",
					testListIterConcurrent(emptyList_addToFrontA_A(), Result.NoException));
			printTest("A_testConcurrentListIter0_1",
					testListIterConcurrent(emptyList_addToFrontA_A(), 0, 1, Result.NoException));
			printTest("AB_testConcurrentListIter",
					testListIterConcurrent(A_addToRearB_AB(), Result.NoException));
			printTest("AB_testConcurrentListIter0_1",
					testListIterConcurrent(A_addToRearB_AB(), 0, 1, Result.NoException));
			printTest("AB_testConcurrentListIter0_2",
					testListIterConcurrent(A_addToRearB_AB(), 0, 2, Result.NoException));

			// Not mutated list method calls do not invalidate a ListIterator
			IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
			ListIterator<Integer> it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasPreviousConcurrent",
					testListIterHasPrevious(it1, Result.False));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_contains_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_contains_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOf_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.True));
			// Mutating list methods DO invalidate a ListIterator
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasPreviousConcurrent",
					testListIterHasPrevious(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasPreviousConcurrent",
					testListIterHasPrevious(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasNextConcurrent",
					testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			// One ListIterator's mutations invalidate a second ListIterator on the same list
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			ListIterator<Integer> it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_listIter1NextRemove_testListIter2HasNextConcurrent",
					testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_listIter1NextRemove_testListIter2NextConcurrent",
					testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_listIter1NextSet_testListIter2HasNextConcurrent",
					testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_listIter1NextSet_testListIter2NextConcurrent",
					testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}
