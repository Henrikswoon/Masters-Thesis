package soot.MyAnalyzer;

import soot.*;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;

import java.util.*;

public class MyBodyTransformerWrapper extends BodyTransformer {
    private static final MyBodyTransformerWrapper instance = new MyBodyTransformerWrapper();
    static int nInternalTransform;
    static Jimple jimple = Jimple.v();
    static Unit assignStmt;
    static Unit invokeStmt;
    static Local r1;
    static Unit throwStmt;
    private MyBodyTransformerWrapper(){}
    public static MyBodyTransformerWrapper v(){
        nInternalTransform = 0;
        initializeInstructions();
        return instance;
    }


    private static void initializeInstructions(){
        try{
            String className = "java.lang.RuntimeException";
            SootClass c = new SootClass(className);
            System.out.println(c.printNameMethodsFields());
            RefType refException = c.getType();
            r1 = jimple.newLocal("$r1", refException);
            NewExpr newExpr = Jimple.v().newNewExpr(refException);
            assignStmt = jimple.newAssignStmt(r1, newExpr);
            SootMethod constructor = c.getMethod("<init>", Collections.emptyList());
            SpecialInvokeExpr invokeExpr = jimple.newSpecialInvokeExpr(r1, constructor.makeRef());
            invokeStmt = jimple.newInvokeStmt(invokeExpr);
            throwStmt = jimple.newThrowStmt(r1);
        } catch(ExceptionInInitializerError e){
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * @param b         the body on which to apply the transformation
     * @param phaseName the phasename for this transform; not typically used by implementations.
     * @param options   the actual computed options; a combination of default options and Scene specified options.
     *
     * At the head of each unique Body b, append
     *  \@SWRR
     *  throw new IllegalStateException(new SWRRException("Execution dissallowed at {body.method.getName()}));
     *
     *  Unit representing throws should not 'fall through'?, JThrowStmt should branch?
     */

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        nInternalTransform++;
        System.out.println(nInternalTransform + ": "+b.getMethod().getName());
        /**
         * java.lang.IllegalStateException $r1;                                         | Add local $r1
         * $r1 = new java.lang.IllegalStateException;                                   | Assign $r1 new IllegalStateException
         * specialinvoke $r1.<java.lang.IllegalStateException: void <init>()>();        | Invoke constructor
         * throw $r1;                                                                   | Throw constructed Exception
         */
        b.getLocals().addFirst(r1);
        b.getUnits().addAll(Arrays.asList(assignStmt,invokeStmt,throwStmt));
    }
}

    /*
    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        ArrayList<Integer> signatures = new ArrayList<>();
        hasRan = true;
        int i = 0;
        ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(b);
        for (Unit u : exceptionalUnitGraph) {
            Collection<ExceptionalUnitGraph.ExceptionDest> destCollection = exceptionalUnitGraph.getExceptionDests(u);
            if(!destCollection.isEmpty()){
                for(ExceptionalUnitGraph.ExceptionDest dest : destCollection){
                    try{
//                        System.out.printf("dest.getTrap().getBeginUnit().getjavaSourceStartLineNumber: %s\n",
//                                dest.getTrap().getBeginUnit().getJavaSourceStartLineNumber());
                        if(dest.getTrap() != null && !signatures.contains(dest.hashCode())){
                            signatures.add(dest.hashCode());
                            System.out.printf("\n[%d]---\n%s\n---[%d]\n", i, dest.toString(), i);
                            i++;
                        }
                    } catch (RuntimeException e) {
                        //if (dest.getHandlerNode() != null)
                            //System.err.println(dest.getHandlerNode());
                    }
                }
            }
        }
    }
    */
