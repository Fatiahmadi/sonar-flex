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

public class TooManyReturnCheckTest {

  private TooManyReturnCheck check = new TooManyReturnCheck();

  @Test
  public void defaults() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/TooManyReturn.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Reduce the number of returns of this function 4, down to the maximum allowed " +  + check.max + ".")
      .noMore();
  }

  @Test
  public void custom() {
    check.max = 2;

    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/TooManyReturn.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Reduce the number of returns of this function 4, down to the maximum allowed " +  + check.max + ".")
      .next().atLine(14)
      .next().atLine(23)
      .noMore();
  }
}
