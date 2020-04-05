package test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import simplifier.ChainSimplifier;
import simplifier.chain.Chain;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimplifierTest {
    @ParameterizedTest
    @MethodSource({"simpleChains", "polynomialChains"})
    public void simplifyTest(String inputString, String resultString) throws SyntaxException, TypeException {
        ChainSimplifier chainSimplifier = new ChainSimplifier();
        assertEquals(resultString, chainSimplifier.applyToChain(inputString).toString());
    }

    public static List<Arguments> simpleChains() {
        return List.of(
                Arguments.of("filter{(10>element)}", "filter{((element-10)>0)}"),
                Arguments.of("map{(10+element)}", "map{(element+10)}"),
                Arguments.of("map{(element+10)}", "map{(element+10)}"),
                Arguments.of("map{20}", "map{20}"),
                Arguments.of("map{(10+10)}", "map{20}"),
                Arguments.of("filter{((element+21)>20)}", "filter{((element+1)>0)}"),
                Arguments.of("map{element}%>%map{(element*434)}", "map{(434*element)}"),
                Arguments.of("map{(element+1)}%>%map{(element*434)}", "map{((434*element)+434)}"),
                Arguments.of("filter{(4343=4342)}", "filter{(1=0)}")
        );
    }

    public static List<Arguments> polynomialChains() {
        return List.of(
                Arguments.of("filter{((element+10)>((-1*element)+10))}", "filter{((2*element)>0)}"),
                Arguments.of("map{(10+element)}%>%filter{(((element*3)-12)>32)}",
                        "filter{(((3*element)+-14)>0)}%>%map{(element+10)}"),
                Arguments.of("map{((element*element)+(element*3))}%>%map{((((element*element)*3)+element)-32)}",
                        "map{((3*(element*(element*(element*element))))+"
                                + "((18*(element*(element*element)))+((28*(element*element))+((3*element)+-32))))}"),
                Arguments.of("map{20}%>%filter{(element>3)}%>%filter{(element=20)}", "filter{(0=0)}%>%map{20}"),
                Arguments.of("filter{(element>-43)}%>%map{(element-323)}%>%filter{((element=322)|(element<0))}",
                        "filter{(((element+43)>0)&(((element+-645)=0)|((element+-323)<0)))}%>%map{(element+-323)}")
        );
    }
}
