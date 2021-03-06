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

public class TooManyLinesInFunctionCheckTest {
  private TooManyLinesInFunctionCheck check = new TooManyLinesInFunctionCheck();

  @Test
  public void testDefault() throws Exception {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/TooManyLinesInFunction.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
  }

  @Test
  public void custom() {
    check.max = 3;
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/TooManyLinesInFunction.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("This function has 6 lines, which is greater than the " + check.max + " lines authorized. Split it into smaller functions.")
      .next().atLine(2).withMessage("This function has 4 lines, which is greater than the " + check.max + " lines authorized. Split it into smaller functions.")
      .next().atLine(8)
      .next().atLine(13)
      .noMore();
  }
}
