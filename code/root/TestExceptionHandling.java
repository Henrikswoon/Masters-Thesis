import SWRRException.SWRRException;
class TestExceptionHandling{
    public static void main(String[] args) throws SWRRException{
        System.out.println("Entering main...\n");
        int result_test_1 = testCase_redirect_after_catch();
        assert result_test_1 == 1;

        try {
            Integer result_test_2 = testCase_redirect_after_throw();
            assert false;
        } catch (SWRRException e) {
            System.out.println("[TEST 2:]\tCaught: " + e.getMessage());
            assert true;
        }
    }

    static int testCase_redirect_after_catch() throws SWRRException{
        int x = 0;
        try {
            throw new SWRRException("Test redirecting execution, does control flow continue after 'catch'?");
        } catch (SWRRException e) {
            System.out.println("[TEST 1:]\tCaught: "+e.getMessage());
            x = 2;
        }
        System.out.println("[TEST 1:]\t... Resuming execution directly after catch");
        x = 1;
        return x;
    }
    static Integer testCase_redirect_after_throw() throws SWRRException {
        throw new SWRRException("Test redirecting Execution from 'throw' statement at the top of method");
    }
}