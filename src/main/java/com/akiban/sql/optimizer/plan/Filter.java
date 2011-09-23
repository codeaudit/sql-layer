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

package com.akiban.sql.optimizer.plan;

import java.util.List;

/** Apply a conjunction of Boolean expressions to the input.
 * (What relational algebra and the operator system call Select).
 */
public class Filter extends BasePlanWithInput
{
    private List<ConditionExpression> conditions;

    public Filter(PlanNode input, List<ConditionExpression> conditions) {
        super(input);
        this.conditions = conditions;
    }

    public List<ConditionExpression> getConditions() {
        return conditions;
    }

    @Override
    public boolean accept(PlanVisitor v) {
        if (v.visitEnter(this)) {
            if (getInput().accept(v)) {
                if (v instanceof ExpressionRewriteVisitor) {
                    for (int i = 0; i < conditions.size(); i++) {
                        conditions.set(i, (ConditionExpression)conditions.get(i).accept((ExpressionRewriteVisitor)v));
                    }
                }
                else if (v instanceof ExpressionVisitor) {
                    for (ConditionExpression condition : conditions) {
                        if (!condition.accept((ExpressionVisitor)v)) {
                            break;
                        }
                    }
                }
            }
        }
        return v.visitLeave(this);
    }
    
    @Override
    public String summaryString() {
        return super.summaryString() + conditions.toString();
    }

    @Override
    protected void deepCopy(DuplicateMap map) {
        super.deepCopy(map);
        conditions = duplicateList(conditions, map);
    }

}