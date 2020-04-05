package test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import simplifier.ChainSimplifier;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest {
    private static List<String> correctCalls = List.of(
            "%>%map{(1+element)}", "%>%filter{(2>(-22+element))}",
            "%>%map{(2*(element+(2-1121)))}", "%>%filter{((111=element)|(212>element)}",
            ""
    );

    @ParameterizedTest
    @MethodSource({"callTypeExceptions", "expressionTypeExceptions"})
    public void typeExceptionTest(String inputString) throws SyntaxException, TypeException {
        ChainSimplifier chainSimplifier = new ChainSimplifier();
        for (String call : correctCalls) {
            assertThrows(TypeException.class, () -> chainSimplifier.applyToChain(inputString.concat(call)));
        }
    }

    public static List<String> callTypeExceptions() {
        return List.of(
                "map{(10>element)}",
                "map{(1>(element+(2*3)))}",
                "map{((23>element)|(element=10))}",
                "filter{((111+element)*(212-element))}",
                "filter{(11+element)}", "filter{element}",
                "map{((element>0)|(1=1))}",
                "filter{(-3232+((element*-223)+((element*(element*element))-(element+11))))}"
        );
    }

    public static List<String> expressionTypeExceptions() {
        return List.of(
                "map{(element|1)}",
                "filter{(element&(element>23))}",
                "filter{(-3232<((element*-223)&((element*(element>element))-(element+11))))}",
                "filter{(434>(0<element))}",
                "filter{(434=(0<element))}",
                "map{(1>(element+(2<3)))}",
                "filter{(((231>element)&(element<22))=((321>element)|(0<element)))}"
        );
    }

    @ParameterizedTest
    @MethodSource({"syntaxExceptions"})
    public void syntaxExceptionTest(String inputString){
        ChainSimplifier chainSimplifier = new ChainSimplifier();
        for (String call : correctCalls) {
            assertThrows(SyntaxException.class, () -> chainSimplifier.applyToChain(inputString.concat(call)));
        }
    }



    public static List<String> syntaxExceptions() {
        return List.of(
                "map{(10>element)", "filter{(1==2)}",
                "map{(1>(element+(2*3))}", "filter{(element)}",
                "ewe{(1+element)}",
                "map{(1+element)}%>filter{(element>1)}",
                "map{1+element}",
                "map{(1+element)}%>filter{element>1}",
                "map{-(1+element)}",
                "map{(-(1+element))}",
                "map{(1+element)}%>%filter{(element == 1)}",
                "map{(1+element)}%>%",
                "%>%map{element}", "mad{(2+element)}",
                "map {(1+2)}", "filter {(element >2)}",
                "map{(2+element*4)}", "map{(2 + element)}",
                "filter{((-22)<element)}", "map{-3} ",
                "map{(23+(element-(22*-33))}",
                "filter{((1114element)|(212>element)}",
                "filter{((111=element)|(212eelement)}",
                "filter{((111=element)|(212>eleme nt)}",
                "filter{((111=element)|(212%element)}",
                "map{(40/element)}",
                "map[]", "map{}", "filter{()}",
                ""
        );
    }
}
