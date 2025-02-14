package SWRRException;
public class SWRRException 
    extends Exception{
        public SWRRException(String errorMessage){
            super(errorMessage);
        }
        public SWRRException(String errorMessage, Throwable err){
            super(errorMessage, err);
        }
}