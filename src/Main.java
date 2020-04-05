import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;
import simplifier.ChainSimplifier;


public class Main {
    public static void main(String[] args) {
        try {
            ChainSimplifier chainSimplifier = new ChainSimplifier();
            System.out.print(chainSimplifier.applyToChain(args[0]).toString());
        } catch (TypeException | SyntaxException e) {
            e.printStackTrace();
        }
    }
}
