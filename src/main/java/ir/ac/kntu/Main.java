package ir.ac.kntu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Integer;

public class Main {
    //Interpreter identifier
    public static void interpreter(String fpp){
        ArrayList<String> fppInsider = fppSpliter(fpp.split("\n"));
        HashMap<String, Integer> mainData = new HashMap<>();
        String[] loopInitiator = new String[3];
        ArrayList<String> loopLines = new ArrayList<String>();
        for (int i=0;i<fppInsider.size();i++){
            if (fppInsider.get(i).equals(">")){
                input(fppInsider.get(i+1), fppInsider.get(i+2), mainData);
            } else if (fppInsider.get(i).equals("<")){
                print(fppInsider.get(i+1), mainData);
            } else if (fppInsider.get(i).contains("=") && !(fppInsider.get(i).contains("beEzaye")) && !(fppInsider.get(i).charAt(0) == '*')){

                init(fppInsider.get(i), mainData);
            } else if (fppInsider.get(i).contains("beEzaye")){
                loopInitiator = initLoop(fppInsider.get(i));
            } else if (fppInsider.get(i).charAt(0) == '*'){
                loopLines.add(fppInsider.get(i));
                if (i!=fppInsider.size()-1 && fppInsider.get(i+1).charAt(0) == '*'){
                    continue;
                }
                loop(loopInitiator, loopLines, mainData);
            }
        }
    }

    public static void interpreterInLoop(ArrayList<String> loopLines, HashMap<String, Integer> mainData, HashMap<String, Integer> loopData){
        for (int i=0;i<loopLines.size();i++){
            if (loopLines.get(i).contains(">")) {
                input(loopLines.get(i), loopLines.get(i+1), loopData);
            } else if (loopLines.get(i).contains("<")) {
                printInLoop(loopLines.get(i), mainData, loopData);
            } else if (loopLines.get(i).contains("=")){
                initInLoop(loopLines.get(i), mainData, loopData);
            }
        }
    }

    // Functions of interpreter
    public static  ArrayList<String> fppSpliter(String[] fppLines){
        ArrayList<String> fppElements = new ArrayList<String>();
        for (int i=0;i<fppLines.length;i++){
            if (fppLines[i].contains("=") || fppLines[i].contains("beEzaye") || fppLines[i].charAt(0)=='*'){
                fppElements.add(fppLines[i].strip());
            } else if (fppLines[i].contains(">")){
                int index = fppLines[i].indexOf('>');
                fppElements.add(">");
                fppElements.add(fppLines[i].substring(index+1).strip());
                fppElements.add(fppLines[i+1].strip());
            } else if (fppLines[i].contains("<")){
                int index = fppLines[i].indexOf('<');
                fppElements.add("<");
                fppElements.add(fppLines[i].substring(index+1).strip());
            }
        }
        return fppElements;
    }

    public static void input(String var, String toVal, HashMap<String, Integer> mainData){
        int val = Integer.parseInt(toVal);
        mainData.put(var, val);
    }

    public static void print(String key, HashMap<String, Integer> mainData){
        if (mainData.containsKey(key)){
            System.out.println(mainData.get(key));
        } else {
            System.out.println(key+" is not defined");
        }
    }

    //The only reason loop printer is different is because of loop database
    public static void printInLoop(String toKey, HashMap<String, Integer> mainData, HashMap<String, Integer> loopData){
        String key = identifyKey(toKey);
        if (mainData.containsKey(key)){
            System.out.println(mainData.get(key));
        } else if (loopData.containsKey(key)){
            System.out.println(loopData.get(key));
        } else {
            System.out.println(key+" is not defined");
        }
    }

    public static void init(String line, HashMap<String, Integer> mainData){
        ArrayList<String> calcStuff = calcStuff(line);
        String var = calcStuff.get(0);
        int val = addUp(0, calcStuff.get(2), mainData);
        for (int j=0;j<calcStuff.size();j++){
            if (calcStuff.get(j).equals("+")){
                val = addUp(val, calcStuff.get(j+1), mainData);
            }
            if (calcStuff.get(j).equals("-")){
                val = delUp(val, calcStuff.get(j+1), mainData);
            }
            if (calcStuff.get(j).equals("*")){
                val = mulUp(val, calcStuff.get(j+1), mainData);
            }
            if (calcStuff.get(j).equals("/")){
                val = devUp(val, calcStuff.get(j+1), mainData);
            }
        }
        mainData.put(var, val);
    }

    //Loop Specific functions
    public static void initInLoop(String line, HashMap<String, Integer> mainData, HashMap<String, Integer> loopData){
        ArrayList<String> calcStuff = calcStuff(line);
        String var = calcStuff.get(0);
        int val = 0;
        if (loopData.containsKey(var)){
            val += addUp(0, calcStuff.get(2), loopData);
        } else if (mainData.containsKey(var)) {
            val += addUp(0, calcStuff.get(2), mainData);
        }
        val = iterationOfLoopInit(val, calcStuff, mainData, loopData);
        if (mainData.containsKey(var)){
            mainData.put(var, val);
        } else if (loopData.containsKey(var)) {
            loopData.put(var, val);
        }
    }

    public static int iterationOfLoopInit(int val, ArrayList<String> calcStuff, HashMap<String, Integer> mainData, HashMap<String, Integer> loopData){
        for (int j=0;j<calcStuff.size();j++){
            if (calcStuff.get(j).equals("+")){
                if (loopData.containsKey(calcStuff.get(j+1))) {
                    val = addUp(val, calcStuff.get(j + 1), loopData);
                } else {
                    val = addUp(val, calcStuff.get(j + 1), mainData);
                }
            } else if (calcStuff.get(j).equals("-")){
                if (loopData.containsKey(calcStuff.get(j+1))) {
                    val = delUp(val, calcStuff.get(j + 1), loopData);
                } else {
                    val = delUp(val, calcStuff.get(j + 1), mainData);
                }
            } else if (calcStuff.get(j).equals("*")){
                if (loopData.containsKey(calcStuff.get(j+1))) {
                    val = mulUp(val, calcStuff.get(j + 1), loopData);
                } else {
                    val = mulUp(val, calcStuff.get(j + 1), mainData);
                }
            } else if (calcStuff.get(j).equals("/")){
                if (loopData.containsKey(calcStuff.get(j+1))) {
                    val = devUp(val, calcStuff.get(j + 1), loopData);
                } else {
                    val = devUp(val, calcStuff.get(j + 1), mainData);
                }
            }
        }
        return val;
    }

    public static String[] initLoop(String loopInitiator){
        String[] firstLineOfLoop = loopInitiator.strip().split("\\s+");
        String[] outData = new String[3];
        outData[0] = firstLineOfLoop[1];
        outData[1] = firstLineOfLoop[3];
        if (firstLineOfLoop[5].charAt(firstLineOfLoop[5].length()-1) == ':'){
            outData[2] = firstLineOfLoop[5].substring(0,firstLineOfLoop[5].length()-1);
        } else {
            outData[2] = firstLineOfLoop[5];
        }
        return outData;
    }

    public static void loop(String[] loopInitiator,ArrayList<String> loopLines, HashMap<String, Integer> mainData){
        HashMap<String, Integer> loopData = new HashMap<>();
        String u = loopInitiator[0];
        int startOfU = Integer.parseInt(loopInitiator[1]);
        int endOfU = Integer.parseInt(loopInitiator[2]);
        loopData.put(u, startOfU);
        ArrayList<String> newLoopLines = rmLoopSign(loopLines);
        //Interpret whatever is inside the loop
        for (int j=startOfU;j<endOfU;j++){
            loopData.replace(u, j);
            interpreterInLoop(newLoopLines, mainData, loopData);
        }
    }

    // Debugger functions
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isSymbol(String s){
        if (s.equals("=") || s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*")){
            return true;
        }
        return false;
    }

    public static ArrayList<String> rmLoopSign(ArrayList<String> loopLines){
        ArrayList<String> outData = new ArrayList<String>();
        for (int i=0;i<loopLines.size();i++){
            outData.add(loopLines.get(i).strip().substring(1));
        }
        return outData;
    }

    public static String identifyKey(String toKey){
        String[] toKey2 = toKey.strip().split("\\s+");
        String key = "";
        if (toKey2.length>1){
            key = toKey2[1];
        } else {
            key = toKey2[0].substring(1);
        }
        return key;
    }
    
    //init functions
    public static Integer addUp(int val, String toAdd, HashMap<String, Integer> mainData){
        if (toAdd.charAt(0) == '-'){
            val += delUp(val, toAdd.substring(1), mainData);
            return val;
        }
        if (mainData.containsKey(toAdd)){
            val += mainData.get(toAdd);
        } else if (isInteger(toAdd)){
            val += Integer.parseInt(toAdd);
        } else if (!isSymbol(toAdd)){
            System.out.println(toAdd+" is not defined");
            return null;
        }
        return val;
    }

    public static Integer delUp(int val, String toAdd, HashMap<String, Integer> mainData){
        if (mainData.containsKey(toAdd)){
            val -= mainData.get(toAdd);
        } else if (isInteger(toAdd)){
            val -= Integer.parseInt(toAdd);
        } else if (!isSymbol(toAdd)){
            System.out.println(toAdd+" is not defined");
            return null;
        }
        return val;
    }

    public static Integer devUp(int val, String toAdd, HashMap<String, Integer> mainData){
        if (mainData.containsKey(toAdd)){
            val /= mainData.get(toAdd);
        } else if (isInteger(toAdd)){
            val /= Integer.parseInt(toAdd);
        } else if (!isSymbol(toAdd)){
            System.out.println(toAdd+" is not defined");
            return null;
        }
        return val;
    }

    public static Integer mulUp(int val, String toAdd, HashMap<String, Integer> mainData){
        if (mainData.containsKey(toAdd)){
            val *= mainData.get(toAdd);
        } else if (isInteger(toAdd)){
            val *= Integer.parseInt(toAdd);
        } else if (!isSymbol(toAdd)){
            System.out.println(toAdd+" is not defined");
            return null;
        }
        return val;
    }

    public static ArrayList<String> calcStuff(String line){
        String[] inLine = line.split("=|\\+|-|\\*|/");
        ArrayList<String> calcStuff = new ArrayList<String>();
        ArrayList<String> symbols = new ArrayList<String>();
        for (int j=0;j<line.length();j++){
            if (isSymbol(String.valueOf(line.charAt(j)))){
                symbols.add(String.valueOf(line.charAt(j)));
            }
        }
        for (int j=0;j<inLine.length-1;j++){
            calcStuff.add(inLine[j].strip());
            calcStuff.add(symbols.get(j));
        }
        calcStuff.add(inLine[inLine.length-1].strip());
        if (calcStuff.get(2).equals("")){
            calcStuff.remove(2);
            calcStuff.set(3, calcStuff.get(2)+calcStuff.get(3));
            calcStuff.remove(2);
        }
        return calcStuff;
    }

    // Main function should be written to get the .fpp file and pass the contents to the interpreter()
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        StringBuilder fpps = new StringBuilder();
        String eval = in.nextLine();
        while (!eval.equals("exit_0")){
            fpps.append(eval+"\n");
            eval = in.nextLine().strip();
        }
        String fpp = String.valueOf(fpps);
        in.close();
        interpreter(fpp);
    }
}