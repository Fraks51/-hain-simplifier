package test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import simplifier.chain.Chain;
import simplifier.chain.FilterCall;
import simplifier.chain.MapCall;
import simplifier.exceptions.SyntaxException;
import simplifier.exceptions.TypeException;
import simplifier.expression.BinaryExpression;
import simplifier.expression.ConstExpression;
import simplifier.expression.ElementExpression;
import simplifier.expression.Operation;
import simplifier.parser.Parser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @ParameterizedTest
    @MethodSource({"testParseExpressions"})
    public void parserTest(String inputString, Chain chain) throws SyntaxException, TypeException {
        assertEquals(Parser.parseStringToChain(inputString), chain);
    }

    public static List<Arguments> testParseExpressions() throws TypeException {
        return List.of(
                Arguments.of(
                        "map{(1+element)}",
                        new Chain(
                                List.of(new MapCall(new BinaryExpression(
                                                Operation.SUM ,
                                                new ConstExpression(1),
                                                new ElementExpression()
                                        ))
                                )
                        )
                ),
                Arguments.of(
                        "filter{(22>44)}",
                        new Chain(
                                List.of(new FilterCall(new BinaryExpression(
                                                Operation.BETTER ,
                                                new ConstExpression(22),
                                                new ConstExpression(44)
                                        ))
                                )
                        )
                ),
                Arguments.of(
                        "filter{((111=element)|(212>element))}",
                        new Chain(
                                List.of(new FilterCall(
                                        new BinaryExpression(
                                                Operation.OR,
                                                new BinaryExpression(
                                                        Operation.EQUAL,
                                                        new ConstExpression(111),
                                                        new ElementExpression()
                                                ),
                                                new BinaryExpression(
                                                        Operation.BETTER,
                                                        new ConstExpression(212),
                                                        new ElementExpression()
                                                )
                                        )
                                ))
                        )
                )
        );
    }
}
