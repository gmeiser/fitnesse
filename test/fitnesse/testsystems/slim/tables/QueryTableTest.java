// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.testsystems.slim.tables;

import fitnesse.testsystems.slim.SlimCommandRunningClient;
import org.junit.Test;

import java.util.Map;

import static util.ListUtility.list;

public class QueryTableTest extends QueryTableTestBase {

  protected String tableType() {
    return "query";
  }

  protected Class<QueryTable> queryTableClass() {
    return QueryTable.class;
  }

  @Test
  public void twoMatchingRowsOutOfOrder() throws Exception {
    assertQueryResults(
      "|3|6|\n" +
        "|2|4|\n",
            util.ListUtility.<Object>list(
              util.ListUtility.list(util.ListUtility.list("n", "2"), util.ListUtility.list("2n", "4")),
              util.ListUtility.list(util.ListUtility.list("n", "3"), util.ListUtility.list("2n", "6"))
            ),
      "[" +
        headRow +
        "[n, 2n], " +
        "[pass(3), pass(6)], " +
        "[pass(2), pass(4)]" +
        "]"
    );
  }

  /* When one row is missing and the other is only partially matched, choose the right one to be marked 'missing'. */
  @Test
  public void oneRowMissingOtherPartiallyMatched() throws Exception {
    makeQueryTableAndBuildInstructions("|" + tableType() + ":fixture|argument|\n" +
      "|x|n|2n|\n" +
      "|1|2|4|\n" +
      "|1|3|6|\n");
    Map<String, Object> pseudoResults = SlimCommandRunningClient.resultToMap(list(
      list("queryTable_id_0", "OK"),
      list("queryTable_id_1", "blah"),
      list("queryTable_id_2",
        util.ListUtility.<Object>list(
          util.ListUtility.list(util.ListUtility.list("x", "1"), util.ListUtility.list("n", "3"), util.ListUtility.list("2n", "5")))
      )));
    evaluateResults(pseudoResults, "[" +
        headRow +
        "[x, n, 2n], " +
        "[pass(1), pass(3), fail(a=5;e=6)], " +
        "[fail(e=1;missing), 2, 4]" +
        "]");
  }
}
