package soot.MyAnalyzer;

import soot.*;
import soot.jbco.util.BodyBuilder;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.Map;

import static soot.SootClass.SIGNATURES;

public class Main {
    public static void main(String[] args){
        System.out.println("Entered MyAnalyzer...");
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.transform",
                        MyBodyTransformerWrapper.v())
        );
        Scene.v().addBasicClass("java.lang.RuntimeException", SIGNATURES);
        soot.Main.main(args);
        int i = 0,j = 0;
        for (SootClass c : Scene.v().getApplicationClasses()){
            System.out.printf("\n---%s---\n",c.getName());
            for(SootMethod m : c.getMethods()){
                System.out.printf("[c:%d,m:%d]\t%s\n",i,j,m.getName());
                j++;
            }
            i++;
        }
    }
}
