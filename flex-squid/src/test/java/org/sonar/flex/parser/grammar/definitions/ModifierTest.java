/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.flex.parser.grammar.definitions;

import com.google.common.base.Charsets;
import com.sonar.sslr.impl.Parser;
import org.junit.Before;
import org.junit.Test;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.parser.FlexParser;

import static com.sonar.sslr.test.parser.ParserMatchers.parse;
import static org.junit.Assert.assertThat;

public class ModifierTest {

  Parser<FlexGrammar> p = FlexParser.create(new FlexConfiguration(Charsets.UTF_8));
  FlexGrammar g = p.getGrammar();

  @Before
  public void init() {
    p.setRootRule(g.modifier);
  }

  @Test
  public void realLife() {
    assertThat(p, parse("public"));
    assertThat(p, parse("private"));
    assertThat(p, parse("protected"));
    assertThat(p, parse("internal"));
    assertThat(p, parse("static"));
    assertThat(p, parse("final"));
    assertThat(p, parse("dynamic"));
    assertThat(p, parse("native"));
    assertThat(p, parse("override"));
    assertThat(p, parse("intrinsic"));
  }

}