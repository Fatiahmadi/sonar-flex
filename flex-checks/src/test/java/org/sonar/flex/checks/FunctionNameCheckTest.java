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

import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class FunctionNameCheckTest {

  private FunctionNameCheck check = new FunctionNameCheck();

  @Test
  public void defaultFormat() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/FunctionName.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Rename this \"DoSomething\" function to match the regular expression " + check.format)
      .next().atLine(16)
      .noMore();
  }

  @Test
  public void custom() {
    check.format = "^[A-Z][a-zA-Z0-9]*$";
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/FunctionName.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(4).withMessage("Rename this \"doSomething\" function to match the regular expression " + check.format)
      .next().atLine(16)
      .noMore();
  }
}
