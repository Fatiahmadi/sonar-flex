/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

@Rule(
  key = "S1467",
  name = "Constructors should not dispatch events",
  priority = Priority.BLOCKER,
  tags = Tags.BUG)
@ActivatedByDefault
@SqaleConstantRemediation("10min")
public class ConstructorCallsDispatchEventCheck extends SquidCheck<LexerlessGrammar> {

  boolean isInClass;
  private Deque<ClassState> classStack = new ArrayDeque<>();

  private static class ClassState {
    String className;
    boolean isInConstructor;

    public ClassState(String className) {
      this.className = className;
    }
  }

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.PRIMARY_EXPR);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    classStack.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      isInClass = true;
      String className = Clazz.getName(astNode);
      classStack.push(new ClassState(className));
    } else if (isConstructor(astNode)) {
      classStack.peek().isInConstructor = true;
    } else if (isCallToDispatchEventInConstructor(astNode)) {
      getContext().createLineViolation(this, "Remove this event dispatch from the \"{0}\" constructor", astNode, classStack.peek().className);
    }
  }

  private boolean isConstructor(AstNode astNode) {
    return isInClass && astNode.is(FlexGrammar.FUNCTION_DEF) && Function.isConstructor(astNode, classStack.peek().className);
  }

  private boolean isCallToDispatchEventInConstructor(AstNode astNode) {
    return isInClass && classStack.peek().isInConstructor && astNode.is(FlexGrammar.PRIMARY_EXPR) && isCallToDispatchEvent(astNode);
  }

  private static boolean isCallToDispatchEvent(AstNode primaryExpr) {
    return "dispatchEvent".equals(primaryExpr.getTokenValue())
      && primaryExpr.getNextAstNode().is(FlexGrammar.ARGUMENTS)
      && primaryExpr.getNextAstNode().getFirstChild(FlexGrammar.LIST_EXPRESSION) != null;
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (isInClass && classStack.peek().isInConstructor && astNode.is(FlexGrammar.FUNCTION_DEF)) {
      classStack.peek().isInConstructor = false;
    } else if (isInClass && astNode.is(FlexGrammar.CLASS_DEF)) {
      classStack.pop();
      isInClass = classStack.isEmpty() ? false : true;
    }
  }
}
