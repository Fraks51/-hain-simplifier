package test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import simplifier.ChainSimplifier;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimplifierTest {
    private final static String simpleMap = "map{element}";
    private final static String simpleFilter = "filter{(0=0)}";
    private final static String falseFilter = "filter{(1=0)}%>%";

    @ParameterizedTest
    @MethodSource({"simpleChains", "polynomialChains", "logicChains"})
    public void simplifyTest(String inputString, String resultString) throws SyntaxException, TypeException {
        ChainSimplifier chainSimplifier = new ChainSimplifier();
        assertEquals(resultString, chainSimplifier.applyToChain(inputString).toString());
    }

    public static List<Arguments> simpleChains() {
        return List.of(
                Arguments.of("", simpleFilter + "%>%" + simpleMap),
                Arguments.of("filter{(10>element)}", "filter{(((-1*element)+10)>0)}%>%" + simpleMap),
                Arguments.of("map{(10+element)}", simpleFilter + "%>%map{(element+10)}"),
                Arguments.of("map{(element+10)}", simpleFilter + "%>%map{(element+10)}"),
                Arguments.of("map{20}", simpleFilter + "%>%map{20}"),
                Arguments.of("map{(10+10)}", simpleFilter + "%>%map{20}"),
                Arguments.of("filter{((element+21)>20)}", "filter{((element+1)>0)}%>%" + simpleMap),
                Arguments.of(simpleMap + "%>%map{(element*434)}", simpleFilter + "%>%map{(434*element)}"),
                Arguments.of("map{(element+1)}%>%map{(element*434)}", simpleFilter +"%>%map{((434*element)+434)}"),
                Arguments.of("filter{(4343=4342)}", "filter{(1=0)}%>%" + simpleMap),
                Arguments.of("map{(0*element)}", simpleFilter +"%>%map{0}"),
                Arguments.of("map{(element+2)}%>%filter{(33=33)}", simpleFilter + "%>%map{(element+2)}"),
                Arguments.of("map{(element+2)}%>%filter{(33=33)}%>%filter{(-322=-322)}",
                        simpleFilter + "%>%map{(element+2)}"),
                Arguments.of("map{(element*4)}%>%filter{(33=33)}%>%filter{(3>2)}",
                        simpleFilter + "%>%map{(4*element)}")
        );
    }

    public static List<Arguments> polynomialChains() {
        return List.of(
                Arguments.of("filter{((element+10)>((-1*element)+10))}", "filter{((2*element)>0)}%>%" + simpleMap),
                Arguments.of("map{(10+element)}%>%filter{(((element*3)-12)>32)}",
                        "filter{(((3*element)+-14)>0)}%>%map{(element+10)}"),
                Arguments.of("map{((element*element)+(element*3))}%>%map{((((element*element)*3)+element)-32)}",
                        simpleFilter + "%>%map{((3*(element*(element*(element*element))))+"
                                + "((18*(element*(element*element)))+((28*(element*element))+((3*element)+-32))))}"),
                Arguments.of("map{((3*(element*(element*(element*element))))+"
                        + "((18*(element*(element*element)))+((28*(element*element))+((3*element)+-32))))}",
                        simpleFilter + "%>%map{((3*(element*(element*(element*element))))+"
                                + "((18*(element*(element*element)))+((28*(element*element))+((3*element)+-32))))}"),
                Arguments.of("map{((((element*element)*-2121)+33)*((32*(element*element))+1))}"
                        ,"filter{(0=0)}%>%map{((-67872*(element*(element*(element*element))))" +
                                "+((-1065*(element*element))+33))}"),
                Arguments.of("filter{(((element*element)=element)|(element=0))}%>%map{(element+100)}" +
                        "%>%filter{(element>1)}", "filter{(((((element*element)+(-1*element))=0)|" +
                        "(element=0))&((element+99)>0))}%>%map{(element+100)}")

        );
    }

    public static List<Arguments> logicChains() {
        return List.of(
                Arguments.of("map{20}%>%filter{(element>3)}%>%filter{(element=20)}", simpleFilter + "%>%map{20}"),
                Arguments.of("filter{(element>-43)}%>%map{(element-323)}%>%filter{((element=322)|(element<0))}",
                        "filter{(((element+43)>0)&(((element+-645)=0)|((element+-323)<0)))}%>%map{(element+-323)}"),
                Arguments.of("map{((((element*element)*-2121)+33)*((32*(element*element))+1))}%>%" +
                        "filter{(100>((element*-22)*12))}%>%map{(element+0)}" +
                        "%>%filter{(element=22)}", "filter{((((-17918208*(element*(element*(element*element))))+" +
                        "((-281160*(element*element))+8812))>0)&(((-67872*(element*(element*(element*element))))+" +
                        "((-1065*(element*element))+11))=0))}%>%map{((-67872*(element*(element*(element*element))))+" +
                        "((-1065*(element*element))+33))}"),
                Arguments.of("filter{(element>0)}%>%filter{(element=0)}", falseFilter + simpleMap),
                Arguments.of("filter{(element>0)}%>%filter{(element<0)}", falseFilter + simpleMap),
                Arguments.of("filter{(element<0)}%>%filter{(element=0)}", falseFilter + simpleMap),
                Arguments.of("filter{(element<0)}%>%filter{(element<0)}", "filter{(element<0)}%>%" + simpleMap),
                Arguments.of("filter{((element>5)|(element<5))}%>%filter{((element>5)|(element=2))}",
                        "filter{(((element+-5)>0)|(((element+-5)<0)&((element+-2)=0)))}%>%" + simpleMap),
                Arguments.of("filter{((element>5)&(element>5))}", "filter{((element+-5)>0)}%>%" + simpleMap)
        );
    }

}
