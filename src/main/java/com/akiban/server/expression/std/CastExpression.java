/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.expression.std;

import com.akiban.server.expression.Expression;
import com.akiban.server.expression.ExpressionEvaluation;
import com.akiban.server.types.AkType;
import com.akiban.server.types.NullValueSource;
import com.akiban.server.types.ValueSource;
import com.akiban.server.types.conversion.Converters;
import com.akiban.server.types.util.ValueHolder;

public class CastExpression extends AbstractUnaryExpression
{
    public CastExpression(AkType type, Expression operand) {
        super(type, operand);
    }

    @Override
    protected String name() {
        return "CAST_" + valueType();
    }

    @Override
    public ExpressionEvaluation evaluation() {
        return new InnerEvaluation(valueType(), operandEvaluation());
    }

    private static final class InnerEvaluation extends AbstractUnaryExpressionEvaluation {
        public InnerEvaluation(AkType type, ExpressionEvaluation ev) {
            super(ev);
            this.type = type;
        }

        @Override
        public ValueSource eval() {
            ValueSource operandSource = operand();
            if (operandSource.isNull())
                return NullValueSource.only();
            if (type.equals(operandSource.getConversionType()))
                return operandSource;
            return Converters.convert(operandSource, holder());
        }

        private ValueHolder holder() {
            if (holder == null) {
                holder = new ValueHolder();
                holder.expectType(type);
            }
            return holder;
        }

        private final AkType type;
        private ValueHolder holder;
    }
}