/*
 *  MIT Licence:
 *
 *  Copyright (c) 2020 Orthodox Engineering Ltd
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 *
 */

package org.orthodox.utl.navigators;

import org.beanplanet.core.lang.TypeUtil;
import org.orthodox.universel.ast.CastExpression;
import org.orthodox.universel.ast.InstanceMethodCall;
import org.orthodox.universel.ast.Node;
import org.orthodox.universel.ast.literals.StringLiteralExpr;
import org.orthodox.universel.ast.navigation.NameTest;
import org.orthodox.universel.ast.navigation.NavigationAxisAndNodeTest;
import org.orthodox.universel.ast.type.reference.PrimitiveType;
import org.orthodox.universel.ast.type.reference.ReferenceType;
import org.orthodox.universel.ast.type.reference.ResolvedTypeReferenceOld;
import org.orthodox.universel.ast.type.reference.TypeReference;
import org.orthodox.universel.exec.navigation.MappingNavigator;
import org.orthodox.universel.exec.navigation.Navigator;
import org.orthodox.universel.symanticanalysis.name.InternalNodeSequence;
import org.orthodox.utl.actions.ActionContext;

import static java.util.Collections.singletonList;
import static org.beanplanet.core.lang.TypeUtil.isPrimitiveType;
import static org.orthodox.universel.ast.TypeInferenceUtil.resolveType;

@Navigator
public class ActionContextNavigators {
    @SuppressWarnings("rawtypes") // Issue with Map<?, ?> so used Map raw type for now.
    @MappingNavigator(axis = "default", name = "*")
    public static Node actionContextNavigate(final ActionContext binding,
                                             final Node instanceAccessor,
                                             final NavigationAxisAndNodeTest<NameTest> step) {
        if (binding == null || !binding.hasVariable(step.getTokenImage().getImage())) return step;

        final Object navigatedPeekValue = binding.getVariable(step.getTokenImage().getImage());
//        return InternalNodeSequence.builder()
//                .add(new InstanceMethodCall(
//                        step.getTokenImage(),
//                        instanceAccessor,
//                        new ResolvedTypeReferenceOld(step.getTokenImage(), ActionContext.class),
//                        (TypeReference) resolveType(Object.class),
//                        "getVariable",
//                        singletonList((TypeReference) resolveType(String.class)),
//                        singletonList(new StringLiteralExpr(step.getTokenImage(), ""))
//                )).resultType(resolveType(navigatedPeekValue == null ? Object.class : navigatedPeekValue.getClass()))
//                .build();
        Node valueAccessor = new InstanceMethodCall(
                        step.getTokenImage(),
                        instanceAccessor,
                        new ResolvedTypeReferenceOld(step.getTokenImage(), ActionContext.class),
                        (TypeReference) resolveType(Object.class),
                        "getVariable",
                        singletonList((TypeReference) resolveType(String.class)),
                        singletonList(new StringLiteralExpr(step.getTokenImage(), ""))
                );
        if (navigatedPeekValue != null) {
            valueAccessor = new CastExpression(step.getTokenImage(),
                    isPrimitiveType(navigatedPeekValue.getClass()) ? new PrimitiveType(step.getTokenImage(), PrimitiveType.Primitive.valueOfTypeName(navigatedPeekValue.getClass().getName())) : new ReferenceType(step.getTokenImage(), new ResolvedTypeReferenceOld(step.getTokenImage(), navigatedPeekValue.getClass()), 0),
                    valueAccessor);
        }

        return valueAccessor;
    }
}
