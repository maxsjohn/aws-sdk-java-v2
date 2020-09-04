/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.codegen.jmespath.parser.util;

import java.util.function.Function;
import software.amazon.awssdk.codegen.jmespath.parser.ParseResult;
import software.amazon.awssdk.codegen.jmespath.parser.Parser;
import software.amazon.awssdk.codegen.jmespath.parser.ParserContext;

public final class ConvertingParser<T, U> implements Parser<U> {
    private final Parser<T> parser;
    private final Function<T, U> converter;

    public ConvertingParser(Parser<T> parser, Function<T, U> converter) {
        this.parser = parser;
        this.converter = converter;
    }

    @Override
    public String name() {
        return parser.name();
    }

    @Override
    public ParseResult<U> parse(int startPosition, int endPosition, ParserContext context) {
        ParseResult<T> result = parser.parse(startPosition, endPosition, context);

        if (result.hasError()) {
            return ParseResult.error(result.error());
        } else {
            return ParseResult.success(converter.apply(result.result()));
        }
    }
}