public static class A {  // NOK - no doc above

  /**
   * ASDoc
   */
  public var field1:String;   // OK
  public var fields2:String;  // NOK

  /**
   * @param param1 my doc
   */
  public function method1(param1:String):Boolean {}  // NOK - no doc for return type

  /**
   * @param
   */
  public function method2(param1:String):void {}     // NOK - no doc for param1

  public function method3(param1:String) {}          // NOK - no doc at all

  /**
   * my doc
   * @param param1 my doc
   * @return my doc
   */
  public function myMethod(param1:String):Boolean {} // OK
}


class B {                      // NOK - no doc above

  // comment
  function f1(p1, p2) {}       // NOK - but 1 issue only because no ASDoc at all

  /**
   * ASDoc
   */
  function f2(p1, p2) {}       // NOK - no doc for parameters p1, p2

  /*
   * Not ASDoc
   */
  function f3() {              // NOK - not ASDoc

  }

  /**
   * ASDoc
   */
  function f4() {              // OK
  }

}


/**
 * ASDoc
 */
class C {     // OK

    internal function f1() {}   // OK
    private function f2() {}    // OK
    protected function f3() {}  // OK

}

