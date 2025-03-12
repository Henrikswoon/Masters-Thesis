package soot.MyAnalyzer;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.List;
import java.util.Map;

public class NameAnalyzer extends ForwardFlowAnalysis {
    FlowSet emptySet = new ArraySparseSet();
    Map<Unit, FlowSet> unitToGenerateSet;
    /**
     * Construct the analysis from a DirectedGraph representation of a Body.
     *
     * @param graph
     */
    public NameAnalyzer(UnitGraph graph) {
        super(graph);

        doAnalysis();
    }

    @Override
    protected void flowThrough(Object in, Object d, Object out) {

    }

    @Override
    protected Object newInitialFlow() {
        return null;
    }

    @Override
    protected void merge(Object in1, Object in2, Object out) {

    }

    @Override
    protected void copy(Object source, Object dest) {

    }
    /*
    int i = 0;
        for (SootClass applicationClass : Scene.v().getApplicationClasses()) {
        final String fullyQualifiedName = applicationClass.getName();
        System.out.printf("%d: %s\n", i++, fullyQualifiedName);
        for(SootMethod method : applicationClass.getMethods()){
            final String methodName = method.getName();
            System.out.printf("%d: %s\n", i++, methodName);
        }
    }
        System.out.printf("Read %d entries\n", i);
    */
}
