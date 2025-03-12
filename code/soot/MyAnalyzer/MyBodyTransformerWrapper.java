package soot.MyAnalyzer;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JThrowStmt;
import soot.tagkit.AnnotationTag;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;

import java.util.*;

public class MyBodyTransformerWrapper extends BodyTransformer {
    private static MyBodyTransformerWrapper instance = new MyBodyTransformerWrapper();

    private MyBodyTransformerWrapper(){}
    public static MyBodyTransformerWrapper v(){
        return instance;
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

    Jimple jimple = Jimple.v();


    String className = "java.lang.RuntimeException";
    RefType refException = RefType.v(className);
    Local r1 = jimple.newLocal("$r1", refException);

    NewExpr newExpr = Jimple.v().newNewExpr(refException);
    Unit assignStmt = jimple.newAssignStmt(r1, newExpr);

    SootClass c = refException.getSootClass();
    SootMethod constructor = c.getMethod("<init>", Collections.emptyList());
    SpecialInvokeExpr invokeExpr = jimple.newSpecialInvokeExpr(r1, constructor.makeRef());
    Unit invokeStmt = jimple.newInvokeStmt(invokeExpr);

    Unit throwStmt = jimple.newThrowStmt(r1);


    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        System.out.printf("Instrumenting: %s\n",
                "Class: " + b.getClass().getName() + "\nMethod: " + b.getMethod().getName()
                );

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
